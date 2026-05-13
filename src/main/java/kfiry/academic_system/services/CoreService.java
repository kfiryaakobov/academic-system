package kfiry.academic_system.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

import org.springframework.stereotype.Service;

import kfiry.academic_system.datamodels.Course;
import kfiry.academic_system.datamodels.CourseGraph;
import kfiry.academic_system.datamodels.TimeSlot;
import kfiry.academic_system.datamodels.User;
import kfiry.academic_system.utilities.SchedulerHelper;
import kfiry.academic_system.utilities.TopologicalHelper;

@Service
public class CoreService {
    private MongoService mongoService;
    //private Map<Course, TimeSlot> globalSchedule = new HashMap<>();

    public CoreService(MongoService mongoService) {
        this.mongoService = mongoService;
    }

    public CourseGraph buildGraphForUser(String username) {
        User user = mongoService.getUser(username);
        if (user == null) {
            throw new RuntimeException("User not found: " + username);
        }
        List<Course> userCourses = getCoursesForUser(user);
        Set<String> userCourseIds = new HashSet<>(user.getCourseIds());
        CourseGraph graph = new CourseGraph();
        // הוספת קורסים לגרף
        for (Course course : userCourses) {
            graph.addCourse(course);
        }
        // הוספת קשרי prerequisite
        for (Course course : userCourses) {
            if (course.getPrerequisites() == null)
                continue;
            for (String prereqId : course.getPrerequisites()) {
                if (userCourseIds.contains(prereqId)) {
                    Course prereqCourse = findCourseById(userCourses, prereqId);
                    if (prereqCourse != null) {
                        graph.addPrerequisite(prereqCourse, course);
                    }
                }
            }
        }
        return graph;
    }

    public List<Course> getCoursesForUser(User user) {
        return mongoService.getCoursesByIds(user.getCourseIds());
    }

    private Course findCourseById(List<Course> courses, String id) {
        for (Course c : courses) {
            if (c.getCourseID().equals(id)) {
                return c;
            }
        }
        return null;
    }

    public Map<Course, TimeSlot> runAlgorithm(User u) {
        CourseGraph graph = buildGraphForUser(u.getUsername());
        List<Course> orderedCourses = topologicalSort(graph);
        SchedulerHelper scheduler = new SchedulerHelper();
        scheduler.setTopologicCourses(orderedCourses);

        boolean success = SchedulingBacktrackingAndPruning(scheduler, 0);

        if (!success) {
            return new HashMap<>();
        }

        return scheduler.getAssignments();
    }

    public List<String> runCoreAndReturnStrings(User u) {
        List<String> result = new ArrayList<>();
        // 1. בניית גרף למשתמש
        CourseGraph graph = buildGraphForUser(u.getUsername());
        // 2. מיון טופולוגי
        List<Course> orderedCourses = topologicalSort(graph);
        // 3. יצירת scheduler
        SchedulerHelper scheduler = new SchedulerHelper();
        scheduler.setTopologicCourses(orderedCourses);
        // 4. הרצת backtracking
        boolean success = SchedulingBacktrackingAndPruning(scheduler, 0);

        if (!success) {
            result.add("No valid schedule found.");
            return result;
        }
        result.add("Scheduling succeeded!");
        result.add("--- FINAL SCHEDULE ---");
        // 5. בניית פלט
        for (Course course : orderedCourses) {
            TimeSlot slot = scheduler.getAssignment(course);
            if (slot != null) {
                result.add(
                        course.getName() + " | " +
                                slot.getDay() + " " +
                                slot.getStartHour() + ":00-" +
                                slot.getEndHour() + ":00");
            }
        }
        return result;
    }

    public static List<Course> topologicalSort(CourseGraph graph) {
        Set<Course> visited = new HashSet<>();
        Stack<Course> stack = new Stack<>();
        TopologicalHelper helper = new TopologicalHelper();

        for (Course course : graph.getGraph().keySet()) {
            if (!visited.contains(course))
                helper.DFS(graph, stack, course, visited);
        }

        List<Course> topologicalList = new ArrayList<Course>();
        Course outStack;
        while (!stack.empty()) {
            outStack = stack.pop();
            topologicalList.add(outStack);
        }
        return topologicalList;
    }

    public static boolean SchedulingBacktrackingAndPruning(SchedulerHelper scheduler, int index) {
        if (scheduler.isComplete())
            return true;
        Course currentCourse = scheduler.getNextCourse(index);
        for (TimeSlot slot : scheduler.getPossibleTimeSlots(currentCourse)) {
            if (!scheduler.canAssign(currentCourse, slot)) // Pruring
                continue;

            scheduler.assign(currentCourse, slot);
            if (SchedulingBacktrackingAndPruning(scheduler, index + 1))
                return true;

            scheduler.unassign(currentCourse);
        }
        return false;
    }
}
