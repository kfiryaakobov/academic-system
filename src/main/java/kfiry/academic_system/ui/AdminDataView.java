package kfiry.academic_system.ui;

import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;

import kfiry.academic_system.datamodels.Admin;
import kfiry.academic_system.datamodels.Course;
import kfiry.academic_system.datamodels.Lecturer;
import kfiry.academic_system.datamodels.ScheduleDocument;
import kfiry.academic_system.datamodels.User;
import kfiry.academic_system.services.AdminService;
import kfiry.academic_system.services.CourseService;
import kfiry.academic_system.services.LecturerService;
import kfiry.academic_system.services.ScheduleService;
import kfiry.academic_system.services.UserService;

import java.util.Map;

@Route(value = "/admin/view-all-data", layout = AppLayoutAdmin.class)
public class AdminDataView extends VerticalLayout implements BeforeEnterObserver {

    private UserService userService;
    private CourseService courseService;
    private LecturerService lecturerService;
    private AdminService adminService;
    private ScheduleService scheduleService;

    private VerticalLayout gridContainer; // מיכל שיחזיק את הטבלה הנבחרת

    public AdminDataView(UserService userService, CourseService courseService,
            LecturerService lecturerService, AdminService adminService,
            ScheduleService scheduleService) {
        this.userService = userService;
        this.courseService = courseService;
        this.lecturerService = lecturerService;
        this.adminService = adminService;
        this.scheduleService = scheduleService;

        // הגדרות עיצוב כלליות
        getElement().setAttribute("dir", "rtl");
        setSizeFull();
        setPadding(true);
        getStyle().set("background-color", "#f8f9fa");
        getStyle().set("overflow-y", "auto");
        getStyle().set("box-sizing", "border-box");

        H2 mainHeader = new H2("תצוגת נתוני המערכת");
        mainHeader.getStyle().set("margin", "0 0 20px 0").set("color", "#1a2a3a");
        setAlignSelf(Alignment.CENTER, mainHeader);

        // תפריט לבחירת הקטגוריה לתצוגה
        ComboBox<String> viewSelector = new ComboBox<>("בחר נתונים להצגה");
        viewSelector.setItems("סטודנטים", "מרצים", "קורסים", "מנהלים", "שיבוץ גלובלי");
        viewSelector.setWidth("300px");
        setAlignSelf(Alignment.CENTER, viewSelector);

        // המיכל שבתוכו נציג את הטבלאות
        gridContainer = new VerticalLayout();
        gridContainer.setWidthFull();
        gridContainer.setPadding(false);

        // מאזין שמופעל בכל פעם שהמנהל בוחר משהו בתפריט
        viewSelector.addValueChangeListener(event -> showGrid(event.getValue()));

        add(mainHeader, viewSelector, gridContainer);
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        Admin admin = (Admin) VaadinSession.getCurrent().getAttribute("admin");
        if (admin == null) {
            // זורק למסך הלוגין אם אין משתמש מחובר
            event.forwardTo(LoginLecturerView.class);
        }
    }

    // פונקציה שמחליפה את התצוגה בהתאם לבחירה
    private void showGrid(String selection) {
        gridContainer.removeAll(); // מנקה את הטבלה הקודמת

        if (selection == null)
            return;

        switch (selection) {
            case "סטודנטים":
                gridContainer.add(createStudentsGridCard("#4A90E2"));
                break;
            case "מרצים":
                gridContainer.add(createLecturersGridCard("#50C878"));
                break;
            case "קורסים":
                gridContainer.add(createCoursesGridCard("#FFB347"));
                break;
            case "מנהלים":
                gridContainer.add(createAdminsGridCard("#9B59B6"));
                break;
            case "שיבוץ גלובלי":
                gridContainer.add(createScheduleGridCard("#E74C3C"));
                break;
        }
    }

    // --- כרטיס סטודנטים ---
    private VerticalLayout createStudentsGridCard(String borderColor) {
        VerticalLayout card = createBaseCard("רשימת סטודנטים", borderColor);

        Grid<User> grid = new Grid<>(User.class, false);
        grid.addColumn(user -> user.getUsername()).setHeader("שם משתמש");
        grid.addColumn(user -> user.getEmail()).setHeader("אימייל");
        grid.addColumn(user -> user.getPassword()).setHeader("סיסמא");
        grid.addColumn(user -> user.getPhone()).setHeader("טלפון");
        grid.addColumn(user -> user.getAge()).setHeader("גיל");
        grid.addColumn(user -> String.join(", ", user.getCourseIds())).setHeader("קורסים");

        grid.setItems(userService.getAllUsers());
        grid.setHeight("400px"); // גובה קבוע כדי לאפשר גלילה פנימית של הטבלה

        card.add(grid);
        return card;
    }

    // --- כרטיס מרצים ---
    private VerticalLayout createLecturersGridCard(String borderColor) {
        VerticalLayout card = createBaseCard("רשימת מרצים", borderColor);

        Grid<Lecturer> grid = new Grid<>(Lecturer.class, false);
        grid.addColumn(Lecturer::getID).setHeader("תעודת זהות");
        grid.addColumn(Lecturer::getPassword).setHeader("סיסמא");
        grid.addColumn(Lecturer::getName).setHeader("שם");
        grid.addColumn(l -> l.getAvailableSlots().size()).setHeader("מס' סלוטים");

        grid.setItems(lecturerService.getAllLectuurer());
        grid.setHeight("400px");

        card.add(grid);
        return card;
    }

    // --- כרטיס קורסים ---
    private VerticalLayout createCoursesGridCard(String borderColor) {
        VerticalLayout card = createBaseCard("רשימת קורסים", borderColor);

        Grid<Course> grid = new Grid<>(Course.class, false);
        grid.addColumn(Course::getCourseID).setHeader("מזהה");
        grid.addColumn(Course::getName).setHeader("שם הקורס");
        grid.addColumn(Course::getDuration).setHeader("סמסטר פתיחה");
        grid.addColumn(c -> c.getLecturer() != null ? c.getLecturer().getName() : "ללא").setHeader("מרצה");
        grid.addColumn(c -> String.join(", ", c.getPrerequisites())).setHeader("קדימויות");

        grid.setItems(courseService.getAllCourses());
        grid.setHeight("400px");

        card.add(grid);
        return card;
    }

    // --- כרטיס מנהלים ---
    private VerticalLayout createAdminsGridCard(String borderColor) {
        VerticalLayout card = createBaseCard("רשימת מנהלים", borderColor);

        Grid<Admin> grid = new Grid<>(Admin.class, false);
        grid.addColumn(Admin::getId).setHeader("ת.ז / משתמש");
        grid.addColumn(Admin::getPassword).setHeader("סיסמא");
        grid.addColumn(Admin::getName).setHeader("שם");

        grid.setItems(adminService.getAllAdmins());
        grid.setHeight("400px");

        card.add(grid);
        return card;
    }

    // --- כרטיס שיבוץ גלובלי ---
    private VerticalLayout createScheduleGridCard(String borderColor) {
        VerticalLayout card = createBaseCard("שיבוץ מערכת גלובלי", borderColor);

        // יצירת גריד שמקבל רשומות מתוך המפה (Entry)
        Grid<Map.Entry<String, String>> grid = new Grid<>();
        grid.addColumn(Map.Entry::getKey).setHeader("מזהה קורס");
        grid.addColumn(Map.Entry::getValue).setHeader("זמן שיבוץ (יום ושעות)");

        // משיכת השיבוץ מהסרוויס המתאים
        ScheduleDocument scheduleDoc = scheduleService.getSchedule();
        if (scheduleDoc != null && scheduleDoc.getCourseToSlot() != null) {
            // הזרקת המפה אל תוך הגריד
            grid.setItems(scheduleDoc.getCourseToSlot().entrySet());
        }

        grid.setHeight("400px");
        card.add(grid);
        return card;
    }

    // --- פונקציית העזר העיצובית ---
    private VerticalLayout createBaseCard(String title, String borderColor) {
        VerticalLayout card = new VerticalLayout();
        card.setWidthFull(); // הקלף יתפוס את כל רוחב המסך הזמין

        card.getStyle()
                .set("background-color", "white")
                .set("border-radius", "12px")
                .set("box-shadow", "0 4px 8px rgba(0,0,0,0.05)")
                .set("border-top", "6px solid " + borderColor)
                .set("padding", "20px");

        H4 cardTitle = new H4(title);
        cardTitle.getStyle().set("margin", "0 0 15px 0");

        card.add(cardTitle);
        return card;
    }
}