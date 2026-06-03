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
import kfiry.academic_system.datamodels.ScheduleDocument;
import kfiry.academic_system.datamodels.TimeSlot;
import kfiry.academic_system.datamodels.User;
import kfiry.academic_system.repositories.ScheduleRepository;
import kfiry.academic_system.utilities.SchedulerHelper;
import kfiry.academic_system.utilities.TopologicalHelper;

@Service
public class CoreService {
    private MongoService mongoService;
    private ScheduleRepository scheduleRepo;

    public CoreService(MongoService mongoService, ScheduleRepository scheduleRepo) {
        this.mongoService = mongoService;
        this.scheduleRepo = scheduleRepo;
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

    public ScheduleDocument getGlobalSchedule() {
        return scheduleRepo.findTopByOrderByIdDesc().orElse(null);
    }

    // גרסא שלישית - גרסא שבה האדמין מריץ את השיבוץ הגלובלי עבור כל המשתמשים.
    // והמשתמשים מסתמכים על המערכת של האדמין
    public Map<Course, TimeSlot> runGlobalAlgorithm(List<Course> allCourses) {
        CourseGraph graph = new CourseGraph();
        for (Course course : allCourses) {
            graph.addCourse(course);
        }
        for (Course course : allCourses) {
            if (course.getPrerequisites() == null)
                continue;
            for (String prereqId : course.getPrerequisites()) {
                Course prereqCourse = findCourseById(allCourses, prereqId);
                if (prereqCourse != null) {
                    graph.addPrerequisite(prereqCourse, course);
                }
            }
        }
        List<Course> orderedCourses = topologicalSort(graph);
        SchedulerHelper scheduler = new SchedulerHelper();
        scheduler.setTopologicCourses(orderedCourses);
        boolean success = SchedulingBacktrackingAndPruning(scheduler, 0);
        if (!success) {
            return new HashMap<>();
        }
        return scheduler.getAssignments();
    }

    // גרסא שנייה של הרלגוריתם - גרסא זו עבדה אך אם היה 2 סטודנטים עם קורסים זהים הם
    // היו משובצים בשעות שונות - ולכן נפסלה
    public Map<Course, TimeSlot> runAlgorithm(User u) {
        CourseGraph graph = buildGraphForUser(u.getUsername());
        List<Course> orderedCourses = topologicalSort(graph);
        SchedulerHelper scheduler = new SchedulerHelper();
        scheduler.setTopologicCourses(orderedCourses);

        boolean success = SchedulingBacktrackingAndPruning(scheduler, 0);

        if (!success) {
            return new HashMap<>();
        }
        for (Map.Entry<Course, TimeSlot> entry : scheduler.getAssignments().entrySet()) {
            Course c = entry.getKey();
            TimeSlot t = entry.getValue();

            System.out.println(
                    c.getCourseID() + " | " +
                            t.getDay() + " " +
                            t.getStartHour() + "-" +
                            t.getEndHour());
        }
        return scheduler.getAssignments();
    }

    // גרסא ראשונה של האלגוריתם המרכזי - הגרסא הדפיסה את השעות כמחרוזת והייתה הוכחה
    // לכך שהאלגוריתם עובד
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

    // /**
    //  * פונקציה רקורסיבית המבצעת שיבוץ קורסים במערכת השעות בעזרת אלגוריתם
    //  * Backtracking ו-Pruning (גיזום).
    //  * * @param scheduler אובייקט עזר המנהל את מצב השיבוצים והמערכת
    //  * 
    //  * @param index האינדקס של הקורס הנוכחי ברשימה הטופולוגית שאותו מנסים לשבץ כעת
    //  * @return true אם נמצא שיבוץ מלא וחוקי לכל הקורסים, אחרת false
    //  */
    // public static boolean SchedulingBacktrackingAndPruning(SchedulerHelper scheduler, int index) {

    //     // תנאי עצירה (Base Case): בודק אם כל הקורסים שובצו בהצלחה.
    //     // אם כן, האלגוריתם מצא פתרון מלא ומחזיר אמת (true) כדי להתחיל לקרוס חזרה למעלה.
    //     if (scheduler.isComplete())
    //         return true;

    //     // שליפת הקורס הנוכחי שאותו יש לשבץ בשלב זה, בהתאם לאינדקס שקיבלנו ברקורסיה
    //     Course currentCourse = scheduler.getNextCourse(index);

    //     // לולאה העוברת על כל חלונות הזמן הפוטנציאליים והאפשריים עבור הקורס
    //     // הנוכחי
    //     for (TimeSlot slot : scheduler.getPossibleTimeSlots(currentCourse)) {

    //         // שלב הגיזום (Pruning): בדיקה האם השיבוץ הנוכחי חוקי (למשל: המרצה פנוי ואין
    //         // חפיפה עם קורס אחר)
    //         // אם השיבוץ אינו חוקי, מדלגים מיד לחלון הזמן הבא בלולאה וחוסכים ריצה רקורסיבית
    //         // מיותרת
    //         if (!scheduler.canAssign(currentCourse, slot))
    //             continue;

    //         // שלב הבחירה (Choose): ביצוע שיבוץ זמני של הקורס הנוכחי בחלון הזמן שנמצא חוקי
    //         scheduler.assign(currentCourse, slot);

    //         // שלב החיפוש הרקורסיבי (Explore): קריאה לפונקציה שוב כדי לשבץ את הקורס הבא בתור
    //         // (index + 1)
    //         // אם הקריאה הרקורסיבית החזירה אמת, זה אומר שהבחירה שלנו הייתה טובה והובילה
    //         // לפתרון מלא, ולכן נחזיר אמת
    //         if (SchedulingBacktrackingAndPruning(scheduler, index + 1))
    //             return true;

    //         // שלב הנסיגה לאחור (Backtrack): אם הגענו לשורה הזו, סימן שהשיבוץ הנוכחי הוביל
    //         // ל"מבוי סתום" בהמשך הדרך.
    //         // לכן, נבטל את השיבוץ של הקורס הנוכחי כדי לנקות את המערכת, והלולאה תמשיך ותנסה
    //         // את חלון הזמן הבא עבורו.
    //         scheduler.unassign(currentCourse);
    //     }

    //     // אם הלולאה סיימה לעבור על כל חלונות הזמן האפשריים לקורס הנוכחי ואף אחד מהם לא
    //     // הניב פתרון מלא,
    //     // סימן שהטעות היא בשלב מוקדם יותר. נחזיר שקר (false) כדי שהשלב הקודם ברקורסיה
    //     // יבצע Backtrack בעצמו וינסה להשתנות.
    //     return false;
    // }
}
