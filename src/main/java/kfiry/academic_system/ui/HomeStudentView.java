package kfiry.academic_system.ui;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;
import com.vaadin.flow.theme.lumo.LumoUtility;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;

import kfiry.academic_system.datamodels.Course;
import kfiry.academic_system.datamodels.ScheduleDocument;
import kfiry.academic_system.datamodels.User;
import kfiry.academic_system.services.CoreService;
import kfiry.academic_system.services.HomeStudentService;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

@Route(value = "/home", layout = AppLayoutStudent.class)
public class HomeStudentView extends HorizontalLayout implements BeforeEnterObserver {

    private HomeStudentService homeServiceStudent;
    private List<Course> studentCourses;
    private CoreService coreService;
    private Div calendarGrid;

    public HomeStudentView(HomeStudentService homeServiceStudent, CoreService coreService) {
        // הגדרות כלליות למסך
        this.homeServiceStudent = homeServiceStudent;
        this.coreService = coreService;
        setSizeFull();
        setPadding(true);
        setSpacing(true);
        getStyle().set("background-color", "#f8fafd"); // צבע רקע נקי כמו בתמונה

        // 1. Sidebar שמאל ( לוח שנה, כפתור להפעלת האלגוריתם)
        VerticalLayout leftSidebar = createLeftSidebar();

        // 2. חלק מרכזי (כותרת עליונה + מקום לטבלה)
        VerticalLayout centerArea = createCenterArea();

        // 3. Sidebar ימין (קורסים שלי, סיכום, איור)
        VerticalLayout rightSidebar = createRightSidebar();

        // הוספה למסך הראשי
        add(leftSidebar, centerArea, rightSidebar);

        // קביעת יחסי רוחב כדי שהמרכז יהיה רחב יותר
        setFlexGrow(0.25, leftSidebar);
        setFlexGrow(0.5, centerArea);
        setFlexGrow(0.25, rightSidebar);
    }

    private VerticalLayout createLeftSidebar() {
        VerticalLayout layout = new VerticalLayout();
        layout.setWidth("300px");
        layout.setPadding(false);

        // לוח שנה קטן
        DatePicker calendar = new DatePicker("לוח שנה");
        calendar.setValue(LocalDate.now());
        calendar.setWidthFull();
        calendar.addClassNames(LumoUtility.Margin.Top.MEDIUM);

        Button runAlgButton = new Button("הצג מערכת שעות");

        runAlgButton.setWidthFull();
        runAlgButton.addClickListener(clickEvent -> privateSchedule());

        runAlgButton.getStyle()
                .set("border-radius", "25px")// מעגל את הפינות של הכפתור
                .set("border", "none")// מוריד את המסגרת של הכפתור מסביב
                .set("color", "black")// צבע הטקסט - שחור
                .set("font-size", "16px")// גודל הטקסט
                .set("font-weight", "bold")/// עובי הטקסט
                .set("background", "linear-gradient(90deg, #0fcebb, #11a1c6)");// רקט הכפתור - נותן רקע עם מעבר צבעים.
                                                                               // מתחיל צהוב חזק והולך ומתחזק

        layout.add(calendar, runAlgButton);
        return layout;
    }

    private VerticalLayout createCenterArea() {
        VerticalLayout layout = new VerticalLayout();
        layout.setPadding(false);

        // --- חישוב תאריכים דינמי לשבוע הנוכחי ---
        LocalDate today = LocalDate.now();
        // מציאת יום ראשון האחרון (היום או בעבר)
        LocalDate startOfWeek = today.with(TemporalAdjusters.previousOrSame(DayOfWeek.SUNDAY));
        LocalDate endOfWeek = startOfWeek.plusDays(5); // יום ו'

        // פורמט עברי לכותרת (למשל: 12-17 במאי 2024)
        DateTimeFormatter monthYearFormatter = DateTimeFormatter.ofPattern("MMMM yyyy", new Locale("he", "IL"));
        String weekRangeTitle = String.format("%d-%d %s",
                startOfWeek.getDayOfMonth(),
                endOfWeek.getDayOfMonth(),
                endOfWeek.format(monthYearFormatter));

        // 1. כותרת עליונה (Header)
        HorizontalLayout header = new HorizontalLayout();
        header.setWidthFull();
        header.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        header.setAlignItems(FlexComponent.Alignment.CENTER);

        H3 title = new H3(weekRangeTitle + " (השבוע הנוכחי) ");
        HorizontalLayout dateNav = new HorizontalLayout(title);
        dateNav.setAlignItems(FlexComponent.Alignment.CENTER);

        header.add(dateNav);

        // 2. בניית גריד לוח השנה
        calendarGrid = new Div();
        calendarGrid.setSizeFull();
        calendarGrid.addClassNames(LumoUtility.Background.BASE, LumoUtility.BorderRadius.LARGE,
                LumoUtility.BoxShadow.SMALL);
        calendarGrid.getStyle()
                .set("display", "grid")
                .set("grid-template-columns", "60px repeat(6, 1fr)")
                .set("grid-template-rows", "50px repeat(11, 80px)")
                .set("overflow", "auto")
                .set("border", "1px solid #e2e8f0")
                .set("direction", "rtl"); // חשוב לעברית!

        // 3. הוספת כותרות ימים (א-ו) דינמיות
        String[] dayNames = { "", "א'", "ב'", "ג'", "ד'", "ה'", "ו'" };
        for (int i = 0; i < dayNames.length; i++) {
            String headerText = "";
            if (i > 0) {
                LocalDate dateForDay = startOfWeek.plusDays(i - 1);
                headerText = dayNames[i] + " " + dateForDay.getDayOfMonth() + "/" + dateForDay.getMonthValue();
            }

            Div dayHeader = new Div(new Span(headerText));
            dayHeader.getStyle().set("grid-column", String.valueOf(i + 1))
                    .set("grid-row", "1")
                    .set("display", "flex").set("align-items", "center").set("justify-content", "center")
                    .set("font-weight", "bold").set("border-bottom", "1px solid #edf2f7");
            calendarGrid.add(dayHeader);
        }

        // הוספת תוויות שעות (08:00 - 18:00)
        for (int hour = 8; hour <= 18; hour++) {
            Div timeLabel = new Div(new Span(String.format("%02d:00", hour)));
            timeLabel.getStyle().set("grid-column", "1")
                    .set("grid-row", String.valueOf(hour - 6)) // התאמה לשורה
                    .set("padding-right", "10px").set("font-size", "0.8rem").set("color", "#718096");
            calendarGrid.add(timeLabel);
        }

        layout.add(header, calendarGrid);
        layout.setFlexGrow(1, calendarGrid);
        return layout;
    }

    // פונקציית עזר ליצירת כרטיס אירוע בתוך הגריד
    private Div createEventCard(String title, String time, String room, String color, int col, int startRow,
            int rowSpan) {
        Div card = new Div();
        card.addClassNames(LumoUtility.Padding.SMALL, LumoUtility.BorderRadius.MEDIUM);
        card.getStyle()
                .set("background-color", color)
                .set("grid-column", String.valueOf(col))
                .set("grid-row", startRow + " / span " + rowSpan)
                .set("margin", "2px")
                .set("display", "flex")
                .set("flex-direction", "column")
                .set("gap", "4px")
                .set("border", "1px solid rgba(0,0,0,0.05)");

        Span t = new Span(title);
        t.getStyle().set("font-weight", "bold").set("font-size", "0.85rem");
        Span tm = new Span(time);
        tm.getStyle().set("font-size", "0.75rem");
        card.add(t, tm);
        return card;
    }

    private VerticalLayout createRightSidebar() {
        VerticalLayout layout = new VerticalLayout();
        layout.setWidth("300px");
        layout.setPadding(false);

        // כרטיס "הקורסים שלי"
        VerticalLayout myCourses = new VerticalLayout();
        myCourses.addClassNames(
                LumoUtility.Background.BASE,
                LumoUtility.BorderRadius.LARGE,
                LumoUtility.Padding.MEDIUM,
                LumoUtility.BoxShadow.SMALL);

        // --- שלב א: השגת המשתמש ---
        User user = (User) VaadinSession.getCurrent().getAttribute("user");

        if (user != null) {
            H3 coursesTitle = new H3("הקורסים שלי");
            coursesTitle.addClassName(LumoUtility.FontSize.MEDIUM);
            myCourses.add(coursesTitle);

            // שמירה בסשן
            VaadinSession.getCurrent().setAttribute("email", user.getEmail());
            VaadinSession.getCurrent().setAttribute("username", user.getUsername());

            // שלב ב: שליפת קורסים
            studentCourses = homeServiceStudent.getCoursesByStudent(user.getEmail());

            if (studentCourses != null) {
                Random random = new Random();

                for (Course course : studentCourses) {
                    // יצירת צבע אקראי
                    String randomColor = String.format("#%06x", random.nextInt(0xffffff + 1));
                    // הוספת השורה
                    myCourses.add(createCourseRow(course.getName(), randomColor));
                }
            }
        } else {
            // במקרה שאין משתמש מחובר
            myCourses.add(new Span("שגיאה: משתמש לא מחובר"));

        }

        // כרטיס סיכום
        VerticalLayout summary = new VerticalLayout();
        summary.addClassNames(
                LumoUtility.Background.BASE,
                LumoUtility.BorderRadius.LARGE,
                LumoUtility.Padding.MEDIUM,
                LumoUtility.BoxShadow.SMALL,
                LumoUtility.Margin.Top.MEDIUM);

        H3 summaryTitle = new H3("סיכום");
        summaryTitle.addClassName(LumoUtility.FontSize.MEDIUM);

        int courseCount = studentCourses.size();
        int totalDuration = homeServiceStudent.calcDuration(studentCourses);
        HorizontalLayout statsContainer = new HorizontalLayout();
        statsContainer.setWidthFull();
        statsContainer.setSpacing(true);
        statsContainer.add(
                createStatCard(String.valueOf(courseCount), "קורסים", "#ecfdf5"),
                createStatCard(String.valueOf(totalDuration), "נק\"ז", "#f5f3ff"));

        summary.add(summaryTitle, statsContainer);

        layout.add(myCourses, summary);
        return layout;
    }

    private HorizontalLayout createCourseRow(String name, String color) {
        Span dot = new Span();
        dot.setWidth("8px");
        dot.setHeight("8px");
        dot.getStyle().set("background-color", color).set("border-radius", "50%");

        Span nameSpan = new Span(name);
        nameSpan.addClassName(LumoUtility.FontSize.SMALL);

        HorizontalLayout row = new HorizontalLayout(nameSpan, dot);
        row.setWidthFull();
        row.setJustifyContentMode(FlexComponent.JustifyContentMode.BETWEEN);
        row.setAlignItems(FlexComponent.Alignment.CENTER);
        row.getChildren().forEach(component -> {
            if (component instanceof Icon) {
                Icon icon = (Icon) component;
                icon.setSize("14px");
            }
        });
        return row;
    }

    private Div createStatCard(String value, String label, String bgColor) {
        Div card = new Div();
        card.addClassNames(
                LumoUtility.Padding.SMALL,
                LumoUtility.BorderRadius.MEDIUM,
                LumoUtility.TextAlignment.CENTER);
        card.getStyle().set("background-color", bgColor);
        card.setWidth("33%");

        Span val = new Span(value);
        val.addClassNames(LumoUtility.FontSize.LARGE, LumoUtility.FontWeight.BOLD);
        val.getStyle().set("display", "block");

        Span lbl = new Span(label);
        lbl.getStyle().set("font-size", "0.6rem");

        card.add(val, lbl);
        return card;
    }

    private void privateSchedule() {
        User user = (User) VaadinSession.getCurrent().getAttribute("user");
        if (user == null || studentCourses == null)
            return;

        // 1. שולפים את המערכת הכללית ממסד הנתונים (במקום להריץ את האלגוריתם מחדש)
        ScheduleDocument globalSchedule = coreService.getGlobalSchedule();

        if (globalSchedule == null || globalSchedule.getCourseToSlot() == null) {
            System.out.println("No global schedule found in DB!");
            return;
        }

        Map<String, String> globalAssignments = globalSchedule.getCourseToSlot();

        // 2. עוברים על הקורסים של הסטודנט ומציירים אותם על הלוח לפי השעות הכלליות
        for (Course course : studentCourses) {
            String courseId = course.getCourseID();

            // בודקים מתי הקורס משובץ במערכת הכללית
            if (globalAssignments.containsKey(courseId)) {
                String timeString = globalAssignments.get(courseId); // לדוגמה: "MONDAY 8:00-9:00"

                // חילוץ היום והשעות מתוך המחרוזת שנשמרה ב-DB
                try {
                    String[] parts = timeString.split(" ");
                    DayOfWeek day = DayOfWeek.valueOf(parts[0]);

                    String[] hours = parts[1].split("-");
                    int startHour = Integer.parseInt(hours[0].split(":")[0]);
                    int endHour = Integer.parseInt(hours[1].split(":")[0]);

                    int column = convertDayToColumn(day);
                    int startRow = startHour - 6;
                    int rowSpan = endHour - startHour;

                    calendarGrid.add(
                            createEventCard(
                                    course.getName(),
                                    timeString.split(" ")[1], // רק השעות (למשל "8:00-9:00")
                                    course.getLecturer() != null ? course.getLecturer().getName() : "Unknown",
                                    generateLightColor(),
                                    column,
                                    startRow,
                                    rowSpan));
                } catch (Exception e) {
                    System.out.println("Error parsing time string for course: " + courseId + " -> " + timeString);
                }
            }
        }
    }

    private int convertDayToColumn(DayOfWeek day) {
        return switch (day) {
            case SUNDAY -> 2;
            case MONDAY -> 3;
            case TUESDAY -> 4;
            case WEDNESDAY -> 5;
            case THURSDAY -> 6;
            case FRIDAY -> 7;
            default -> 2;
        };
    }

    private String generateLightColor() {
        Random random = new Random();
        int red = 150 + random.nextInt(106);
        int green = 150 + random.nextInt(106);
        int blue = 150 + random.nextInt(106);

        return String.format(
                "#%02x%02x%02x",
                red, green, blue);
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        User user = (User) VaadinSession.getCurrent().getAttribute("user");
        if (user == null) {
            // אם אין משתמש מחובר בסשן, המערכת תזרוק אותו אוטומטית למסך ההתחברות הראשי
            event.forwardTo(LoginView.class);
        }
    }
}