package kfiry.academic_system.services;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Stack;

import org.springframework.stereotype.Service;

import kfiry.academic_system.datamodels.Course;
import kfiry.academic_system.datamodels.CourseGraph;
import kfiry.academic_system.datamodels.TimeSlot;

import kfiry.academic_system.utilities.SchedulerHelper;
import kfiry.academic_system.utilities.TopologicalHelper;

@Service
public class CoreService {
    private MongoService mongoService;

    public CoreService(MongoService mongoService) {
        this.mongoService = mongoService;
        // mongoService.initDataIfNeeded();
    }

    public void init() {
        mongoService.initDataIfNeeded();
    }

    public List<String> runCoreAndReturnStrings() {
        List<String> result = new ArrayList<>();
        CourseGraph graph = mongoService.getGraph();
        List<Course> topologicalListStudent = topologicalSort(graph);

        SchedulerHelper scheduler = new SchedulerHelper();
        scheduler.setTopologicCourses(topologicalListStudent);
        boolean success = ScheduleringBacktrackingAndPruning(scheduler, 0);

        if (success) {
            result.add("Scheduling succeeded!");
        } else {
            result.add("No valid schedule found.");
            return result;
        }

        result.add("--- FINAL SCHEDULE ---");
        for (Course c : topologicalListStudent) {
            TimeSlot slot = scheduler.getAssignment(c);
            if (slot != null) {
                result.add(c.getName() + " | " +
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

    public static boolean ScheduleringBacktrackingAndPruning(SchedulerHelper scheduler, int index) {
        if (scheduler.isComplete())
            return true;
        Course currentCourse = scheduler.getNextCourse(index);
        for (TimeSlot slot : scheduler.getPossibleTimeSlots(currentCourse)) {
            if (!scheduler.canAssign(currentCourse, slot)) // Pruring
                continue;

            scheduler.assign(currentCourse, slot);
            if (ScheduleringBacktrackingAndPruning(scheduler, index + 1))
                return true;

            scheduler.unassign(currentCourse);
        }
        return false;
    }
}
