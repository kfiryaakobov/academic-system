package kfiry.academic_system.ui;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;
import com.vaadin.flow.theme.lumo.LumoUtility;

import kfiry.academic_system.datamodels.Admin;
import kfiry.academic_system.datamodels.Course;
import kfiry.academic_system.datamodels.ScheduleDocument;
import kfiry.academic_system.services.CoreService;
import kfiry.academic_system.services.HomeAdminService;
import kfiry.academic_system.services.ScheduleService;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

@Route(value = "/homeAdmin", layout = AppLayoutAdmin.class)
public class HomeAdminView extends HorizontalLayout implements BeforeEnterObserver {

    private final CoreService coreService;
    private final HomeAdminService homeAdminService;
    private final ScheduleService scheduleService;
    private Div calendarGrid;

    public HomeAdminView(CoreService coreService, HomeAdminService homeAdminService, ScheduleService scheduleService) {
        this.coreService = coreService;
        this.homeAdminService = homeAdminService;
        this.scheduleService = scheduleService;

        setSizeFull();
        setPadding(true);
        setSpacing(true);
        getStyle().set("background-color", "#f8fafd");
        getStyle().set("overflow", "hidden"); // מונע הופעת פס גלילה ראשי

        VerticalLayout leftSidebar = createLeftSidebar();
        VerticalLayout centerArea = createCenterArea();
        VerticalLayout rightSidebar = createRightSidebar();

        add(leftSidebar, centerArea, rightSidebar);

        setFlexGrow(0.25, leftSidebar);
        setFlexGrow(0.5, centerArea);
        setFlexGrow(0.25, rightSidebar);

        // טעינת המערכת הכללית מיד עם עליית המסך
        showGlobalSchedule();
    }

    // ==========================================
    // הגנת כניסה: חוסם משתמשים שלא התחברו
    // ==========================================
    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        Admin admin = (Admin) VaadinSession.getCurrent().getAttribute("admin");
        if (admin == null) {
            // זורק למסך הלוגין אם אין משתמש מחובר
            event.forwardTo(LoginLecturerView.class);
        }
    }

    private VerticalLayout createLeftSidebar() {
        VerticalLayout layout = new VerticalLayout();
        layout.setWidth("300px");
        layout.setPadding(false);

        DatePicker calendar = new DatePicker("לוח שנה");
        calendar.setValue(LocalDate.now());
        calendar.setWidthFull();

        Button refreshBtn = new Button("חשב מערכת מחדש");
        refreshBtn.setWidthFull();
        refreshBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_ERROR);
        refreshBtn.getStyle().set("margin-top", "20px").set("border-radius", "25px");

        refreshBtn.addClickListener(e -> {
            scheduleService.generateGlobalSchedule();
            Notification.show("המערכת הכללית חושבה ונשמרה מחדש!", 3000, Notification.Position.TOP_CENTER);
            showGlobalSchedule();
        });

        layout.add(calendar, refreshBtn);
        return layout;
    }

    private VerticalLayout createCenterArea() {
        VerticalLayout layout = new VerticalLayout();
        layout.setPadding(false);

        LocalDate today = LocalDate.now();
        LocalDate startOfWeek = today.with(TemporalAdjusters.previousOrSame(DayOfWeek.SUNDAY));
        LocalDate endOfWeek = startOfWeek.plusDays(5);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM yyyy", new Locale("he", "IL"));

        // --- סידור כותרת ממורכזת ---
        HorizontalLayout headerContainer = new HorizontalLayout();
        headerContainer.setWidthFull();
        headerContainer.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        headerContainer.getStyle().set("margin-bottom", "10px");

        H3 title = new H3();
        title.getStyle().set("display", "flex").set("gap", "6px").set("margin", "0").set("align-items", "center");

        Span dates = new Span(startOfWeek.getDayOfMonth() + "-" + endOfWeek.getDayOfMonth());
        dates.getStyle().set("direction", "ltr"); // מכריח את המספרים והמקף להישאר בסדר הנכון

        Span monthYear = new Span(endOfWeek.format(formatter));
        Span suffix = new Span("(לו\"ז כללי)");

        // הסדר פה חשוב: הראשון שמוסיפים יהיה הכי ימני במסך
        title.add(dates, monthYear, suffix);
        headerContainer.add(title);

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
                .set("direction", "rtl");

        String[] dayNames = { "", "א'", "ב'", "ג'", "ד'", "ה'", "ו'" };
        for (int i = 0; i < dayNames.length; i++) {
            String headerText = "";
            if (i > 0) {
                LocalDate dateForDay = startOfWeek.plusDays(i - 1);
                headerText = dayNames[i] + " " + dateForDay.getDayOfMonth() + "/" + dateForDay.getMonthValue();
            }
            Div dayHeader = new Div(new Span(headerText));
            dayHeader.getStyle().set("grid-column", String.valueOf(i + 1)).set("grid-row", "1")
                    .set("display", "flex").set("align-items", "center").set("justify-content", "center")
                    .set("font-weight", "bold").set("border-bottom", "1px solid #edf2f7");
            calendarGrid.add(dayHeader);
        }

        for (int hour = 8; hour <= 18; hour++) {
            Div timeLabel = new Div(new Span(String.format("%02d:00", hour)));
            timeLabel.getStyle().set("grid-column", "1").set("grid-row", String.valueOf(hour - 6))
                    .set("padding-right", "10px").set("font-size", "0.8rem").set("color", "#718096");
            calendarGrid.add(timeLabel);
        }

        layout.add(headerContainer, calendarGrid);
        layout.setFlexGrow(1, calendarGrid);
        return layout;
    }

    private VerticalLayout createRightSidebar() {
        VerticalLayout layout = new VerticalLayout();
        layout.setWidth("300px");
        layout.setPadding(false);

        VerticalLayout stats = new VerticalLayout();
        stats.addClassNames(LumoUtility.Background.BASE, LumoUtility.BorderRadius.LARGE, LumoUtility.Padding.MEDIUM,
                LumoUtility.BoxShadow.SMALL);

        H3 statsTitle = new H3("קורסים במערכת");
        statsTitle.addClassName(LumoUtility.FontSize.MEDIUM);
        statsTitle.getStyle().set("margin-top", "0");

        List<Course> allCourses = homeAdminService.getAllCourses();
        stats.add(statsTitle);
        stats.add(new Span("סה\"כ: " + (allCourses != null ? allCourses.size() : 0) + " קורסים"));

        // אזור נגלל לקורסים עם נקודות צבעוניות
        VerticalLayout coursesListContainer = new VerticalLayout();
        coursesListContainer.setPadding(false);
        coursesListContainer.setSpacing(true); // קצת מרווח בין השורות
        coursesListContainer.getStyle()
                .set("overflow-y", "auto")
                .set("max-height", "400px")
                .set("margin-top", "10px")
                .set("padding-top", "10px")
                .set("border-top", "1px solid #e2e8f0");

        if (allCourses != null) {
            Random random = new Random();
            for (Course c : allCourses) {
                String randomColor = String.format("#%06x", random.nextInt(0xffffff + 1));
                String displayName = c.getName() + " (" + c.getCourseID() + ")";
                coursesListContainer.add(createCourseRow(displayName, randomColor));
            }
        }

        stats.add(coursesListContainer);
        layout.add(stats);
        return layout;
    }

    // הפונקציה שמוסיפה את הנקודה הצבעונית ליד שם הקורס
    private HorizontalLayout createCourseRow(String name, String color) {
        Span dot = new Span();
        dot.setWidth("8px");
        dot.setHeight("8px");
        dot.getStyle()
                .set("background-color", color)
                .set("border-radius", "50%")
                .set("flex-shrink", "0"); // שומר שהנקודה לא תתכווץ

        Span nameSpan = new Span(name);
        nameSpan.getStyle().set("font-size", "0.85rem");

        HorizontalLayout row = new HorizontalLayout(nameSpan, dot);
        row.setWidthFull();
        row.setJustifyContentMode(FlexComponent.JustifyContentMode.BETWEEN);
        row.setAlignItems(FlexComponent.Alignment.CENTER);
        return row;
    }

    private void showGlobalSchedule() {
        calendarGrid.getChildren()
                .filter(c -> c.getStyle().get("grid-row") != null && !c.getStyle().get("grid-row").equals("1")
                        && !c.getStyle().get("grid-column").equals("1"))
                .forEach(c -> calendarGrid.remove(c));

        ScheduleDocument globalSchedule = coreService.getGlobalSchedule();
        if (globalSchedule == null || globalSchedule.getCourseToSlot() == null)
            return;

        Map<String, String> assignments = globalSchedule.getCourseToSlot();

        // שליפת כל הקורסים מראש כדי למצוא אותם לפי ה-courseID
        List<Course> allCourses = homeAdminService.getAllCourses();

        for (Map.Entry<String, String> entry : assignments.entrySet()) {
            String courseId = entry.getKey();
            String timeString = entry.getValue();

            // מציאת הקורס הנכון מתוך הרשימה - פותר את בעיית ה"לא ידוע"
            Course course = null;
            if (allCourses != null) {
                course = allCourses.stream()
                        .filter(c -> c.getCourseID().equals(courseId))
                        .findFirst()
                        .orElse(null);
            }

            String courseName = (course != null) ? course.getName() : "לא ידוע";
            String displayTitle = courseName + " (" + courseId + ")";
            String lecturer = (course != null && course.getLecturer() != null) ? course.getLecturer().getName()
                    : "טרם שובץ";

            try {
                String[] parts = timeString.split(" ");
                DayOfWeek day = DayOfWeek.valueOf(parts[0]);
                String[] hours = parts[1].split("-");
                int startHour = Integer.parseInt(hours[0].split(":")[0]);
                int endHour = Integer.parseInt(hours[1].split(":")[0]);

                calendarGrid.add(createEventCard(
                        displayTitle,
                        parts[1],
                        lecturer,
                        generateLightColor(),
                        convertDayToColumn(day),
                        startHour - 6,
                        endHour - startHour));
            } catch (Exception e) {
                System.out.println("Error parsing slot for: " + courseId);
            }
        }
    }

    private Div createEventCard(String title, String time, String subTitle, String color, int col, int startRow,
            int rowSpan) {
        Div card = new Div();
        card.getStyle()
                .set("background-color", color)
                .set("grid-column", String.valueOf(col))
                .set("grid-row", startRow + " / span " + rowSpan)
                .set("margin", "2px").set("padding", "8px").set("border-radius", "8px")
                .set("display", "flex").set("flex-direction", "column").set("gap", "2px")
                .set("border", "1px solid rgba(0,0,0,0.05)")
                .set("overflow", "hidden");

        Span t = new Span(title);
        t.getStyle().set("font-weight", "bold").set("font-size", "0.75rem");
        Span tm = new Span(time);
        tm.getStyle().set("font-size", "0.7rem");
        Span st = new Span(subTitle);
        st.getStyle().set("font-size", "0.65rem").set("color", "#4a5568");

        card.add(t, tm, st);
        return card;
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
        int red = 210 + random.nextInt(46);
        int green = 210 + random.nextInt(46);
        int blue = 210 + random.nextInt(46);
        return String.format("#%02x%02x%02x", red, green, blue);
    }
}