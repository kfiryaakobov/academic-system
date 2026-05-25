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
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;
import com.vaadin.flow.theme.lumo.LumoUtility;

import kfiry.academic_system.datamodels.Course;
import kfiry.academic_system.datamodels.Lecturer;
import kfiry.academic_system.datamodels.ScheduleDocument;
import kfiry.academic_system.services.CoreService;
import kfiry.academic_system.services.HomeLecturerService;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

@Route(value = "/homeLecturer",  layout = AppLayoutLecturer.class)
public class HomeLecturerView extends VerticalLayout implements BeforeEnterObserver { // שינוי ל-Vertical והוספת הגנה

    private HomeLecturerService homeServiceLecturer;
    private List<Course> lecturerCourses;
    private CoreService coreService;
    private Div calendarGrid;

    public HomeLecturerView(HomeLecturerService homeServiceLecturer, CoreService coreService) {
        this.homeServiceLecturer = homeServiceLecturer;
        this.coreService = coreService;

        setSizeFull();
        setHeight("100vh"); // מונע ירידה בגלגלת
        getStyle().set("overflow", "hidden"); // חיתוך גלילה ברמת המסך הראשי
        setPadding(false); // הורדנו ריווח כדי שהתפריט העליון ייצמד לקצוות
        setSpacing(false);
        getStyle().set("background-color", "#f8fafd");


        // 1. יצירת תוכן המסך (3 העמודות)
        HorizontalLayout mainContent = new HorizontalLayout();
        mainContent.setSizeFull();
        mainContent.setPadding(true); // החזרנו ריווח פנימי לתוכן
        mainContent.setSpacing(true);

        VerticalLayout leftSidebar = createLeftSidebar();
        VerticalLayout centerArea = createCenterArea();
        VerticalLayout rightSidebar = createRightSidebar();

        mainContent.add(leftSidebar, centerArea, rightSidebar);
        mainContent.setFlexGrow(0.25, leftSidebar);
        mainContent.setFlexGrow(0.5, centerArea);
        mainContent.setFlexGrow(0.25, rightSidebar);

        add(mainContent);
    }

    // ==========================================
    // חסימת כניסה למרצה שלא מחובר
    // ==========================================
    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        Lecturer lecturer = (Lecturer) VaadinSession.getCurrent().getAttribute("lecturer");
        if (lecturer == null) {
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
        calendar.addClassNames(LumoUtility.Margin.Top.MEDIUM);

        Button runAlgButton = new Button("הצג מערכת שעות");
        runAlgButton.setWidthFull();
        runAlgButton.addClickListener(clickEvent -> privateSchedule());

        runAlgButton.getStyle()
                .set("border-radius", "25px")
                .set("border", "none")
                .set("color", "black")
                .set("font-size", "16px")
                .set("font-weight", "bold")
                .set("background", "linear-gradient(90deg, #0fcebb, #11a1c6)");

        layout.add(calendar, runAlgButton);
        return layout;
    }

    private VerticalLayout createCenterArea() {
        VerticalLayout layout = new VerticalLayout();
        layout.setPadding(false);

        LocalDate today = LocalDate.now();
        LocalDate startOfWeek = today.with(TemporalAdjusters.previousOrSame(DayOfWeek.SUNDAY));
        LocalDate endOfWeek = startOfWeek.plusDays(5);

        DateTimeFormatter monthYearFormatter = DateTimeFormatter.ofPattern("MMMM yyyy", new Locale("he", "IL"));

        // תיקון סדר התצוגה בעברית על ידי חלוקה ל-Spans
        H3 title = new H3();
        title.getStyle().set("display", "flex").set("gap", "6px").set("margin", "0");
        title.add(
                new Span("(השבוע הנוכחי)"),
                new Span(endOfWeek.format(monthYearFormatter)),
                new Span(startOfWeek.getDayOfMonth() + "-" + endOfWeek.getDayOfMonth()));

        HorizontalLayout header = new HorizontalLayout();
        header.setWidthFull();
        header.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        header.setAlignItems(FlexComponent.Alignment.CENTER);
        header.getStyle().set("margin-bottom", "10px");

        HorizontalLayout dateNav = new HorizontalLayout(title);
        dateNav.setAlignItems(FlexComponent.Alignment.CENTER);
        header.add(dateNav);

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
            dayHeader.getStyle().set("grid-column", String.valueOf(i + 1))
                    .set("grid-row", "1")
                    .set("display", "flex").set("align-items", "center").set("justify-content", "center")
                    .set("font-weight", "bold").set("border-bottom", "1px solid #edf2f7");
            calendarGrid.add(dayHeader);
        }

        for (int hour = 8; hour <= 18; hour++) {
            Div timeLabel = new Div(new Span(String.format("%02d:00", hour)));
            timeLabel.getStyle().set("grid-column", "1")
                    .set("grid-row", String.valueOf(hour - 6))
                    .set("padding-right", "10px").set("font-size", "0.8rem").set("color", "#718096");
            calendarGrid.add(timeLabel);
        }

        layout.add(header, calendarGrid);
        layout.setFlexGrow(1, calendarGrid);
        return layout;
    }

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

        VerticalLayout myCourses = new VerticalLayout();
        myCourses.addClassNames(
                LumoUtility.Background.BASE,
                LumoUtility.BorderRadius.LARGE,
                LumoUtility.Padding.MEDIUM,
                LumoUtility.BoxShadow.SMALL);

        Lecturer lecturer = (Lecturer) VaadinSession.getCurrent().getAttribute("lecturer");
        lecturerCourses = List.of();

        if (lecturer != null) {
            H3 coursesTitle = new H3("הקורסים שאני מלמד");
            coursesTitle.addClassName(LumoUtility.FontSize.MEDIUM);
            myCourses.add(coursesTitle);

            VaadinSession.getCurrent().setAttribute("lecturer_id", lecturer.getID());
            VaadinSession.getCurrent().setAttribute("lecturer_name", lecturer.getName());

            lecturerCourses = homeServiceLecturer.getCoursesByLecturer(lecturer);

            if (lecturerCourses != null && !lecturerCourses.isEmpty()) {
                Random random = new Random();
                for (Course course : lecturerCourses) {
                    String randomColor = String.format("#%06x", random.nextInt(0xffffff + 1));
                    myCourses.add(createCourseRow(course.getName(), randomColor));
                }
            } else {
                myCourses.add(new Span("אין קורסים רשומים"));
            }
        } else {
            myCourses.add(new Span("שגיאה: מרצה לא מחובר"));
        }

        VerticalLayout summary = new VerticalLayout();
        summary.addClassNames(
                LumoUtility.Background.BASE,
                LumoUtility.BorderRadius.LARGE,
                LumoUtility.Padding.MEDIUM,
                LumoUtility.BoxShadow.SMALL,
                LumoUtility.Margin.Top.MEDIUM);

        H3 summaryTitle = new H3("סיכום שעות הוראה");
        summaryTitle.addClassName(LumoUtility.FontSize.MEDIUM);

        int courseCount = (lecturerCourses != null) ? lecturerCourses.size() : 0;
        int totalDuration = (lecturerCourses != null) ? homeServiceLecturer.calcDuration(lecturerCourses) : 0;

        HorizontalLayout statsContainer = new HorizontalLayout();
        statsContainer.setWidthFull();
        statsContainer.setSpacing(true);
        statsContainer.add(
                createStatCard(String.valueOf(courseCount), "קורסים", "#ecfdf5"),
                createStatCard(String.valueOf(totalDuration), "שעות", "#f5f3ff"));

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
        Lecturer lecturer = (Lecturer) VaadinSession.getCurrent().getAttribute("lecturer");
        if (lecturer == null || lecturerCourses == null)
            return;

        ScheduleDocument globalSchedule = coreService.getGlobalSchedule();

        if (globalSchedule == null || globalSchedule.getCourseToSlot() == null) {
            System.out.println("No global schedule found in DB!");
            return;
        }

        Map<String, String> globalAssignments = globalSchedule.getCourseToSlot();

        for (Course course : lecturerCourses) {
            String courseId = course.getCourseID();

            if (globalAssignments.containsKey(courseId)) {
                String timeString = globalAssignments.get(courseId);

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
                                    timeString.split(" ")[1],
                                    "חדר הרצאות",
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
}