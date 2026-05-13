package kfiry.academic_system.ui;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.CheckboxGroup;
import com.vaadin.flow.component.checkbox.CheckboxGroupVariant;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;

import kfiry.academic_system.datamodels.Course;
import kfiry.academic_system.datamodels.User;
import kfiry.academic_system.services.MongoService;
import kfiry.academic_system.services.UserService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Route("/choose")
public class ChooseCourseForStudentView extends VerticalLayout {

    private MongoService mongoService;
    private UserService userService;
    private List<Course> allCourses;
    private VerticalLayout selectedCoursesList;
    private Span creditsLabel;
    private Map<Integer, CheckboxGroup<Course>> semesterGroups;// לכל סמסטר קיימת קבוצת צ'ק בוקסים משלו
    private Button finishChooseBtn;
    private Set<Course> selectedCourses;

    public ChooseCourseForStudentView(MongoService mongoService, UserService userService) {
        this.mongoService = mongoService;
        this.userService = userService;
        allCourses = mongoService.getAllCourses();

        // ---הגדרות קריטיות למניעת יציאה מהמסך ועיצוב המסך---
        getElement().setAttribute("dir", "rtl");
        setSizeFull(); // מגדיר גובה ורוחב 100%
        setPadding(true);
        setSpacing(false);
        getStyle().set("background-color", "#f8f9fa");
        getStyle().set("overflow", "hidden"); // מבטל גלילה של כל העמוד
        getStyle().set("box-sizing", "border-box");// עוזר לשמור שכל הרכיבים יישארו בתוך הגבולות שלהם בצורה מסודרת

        // 1. כותרת
        H3 mainHeader = new H3("בחירת קורסים");
        mainHeader.getStyle()
                .set("margin", "0 0 15px 0")// מגדיר רווחים חיצוניים של הכותרת. זה מתחיל מאפס למעלה, אפס ימין, 15 למטה,
                                            // ואפס משמאל
                .set("color", "#1a2a3a")
                .set("flex-shrink", "0"); // מבטיח שהכותרת לא תתכווץ
        setAlignSelf(Alignment.CENTER, mainHeader);// הכותרת תהיה באמצע אופקית

        // 2. לוח הסמסטרים (החלק העליון)
        HorizontalLayout boardLayout = new HorizontalLayout();
        boardLayout.setWidthFull();
        boardLayout.setSpacing(true);
        boardLayout.setPadding(false);
        boardLayout.getStyle().set("flex-shrink", "0"); // הסמסטרים יתפסו כמה מקום שהם צריכים

        // מחלק קורסים עפ"י סמסטרים
        Map<Integer, List<Course>> coursesBySemester = new HashMap<>();// על כל מספר סמסטר נשמור רשימת קורסים
        for (Course course : allCourses) {
            int semester = course.getDuration();
            if (!coursesBySemester.containsKey(semester)) {// האם קיימת רשימת קורסים לסמסטר הזה?
                coursesBySemester.put(semester, new ArrayList<>());// אם לא, יוצר רשימה חדשה ריקה
            }
            coursesBySemester.get(semester).add(course);// מוסיף את הקורס לרשימת אותו סמסטר
        }

        // יוצר UI בנפרד לכל סמסטר
        semesterGroups = new HashMap<>();
        for (Integer semester : coursesBySemester.keySet()) {// עובר על כל הסמסטרים
            CheckboxGroup<Course> group = createSemesterColumn(boardLayout, // יוצר את העמודה בשביל אותו סמסטר
                    "סמסטר " + getSemesterName(semester),
                    coursesBySemester.get(semester), semester);// מוסיף לעמודה את הקורס
            semesterGroups.put(semester, group);// שורה את הצ'ק בוקס בתוך המאפ
        }

        // 3. תמונה תחתונה
        Image footerImage = new Image("images/ChoosePhoto.png", "Academic Background");
        footerImage.setWidthFull();
        footerImage.setHeightFull(); // נותן לה למלא את מה שנשאר
        footerImage.getStyle()
                .set("object-fit", "cover")
                .set("border-radius", "15px")
                .set("margin-top", "15px")
                .set("box-shadow", "0 4px 10px rgba(0,0,0,0.1)")
                .set("min-height", "0"); // מאפשר לה להתכווץ בתוך ה-Flex בלי לדחוף החוצה

        // חיבור הסמסטרים והתמונה
        VerticalLayout leftContent = new VerticalLayout(boardLayout, footerImage);
        leftContent.setSizeFull();
        leftContent.setPadding(false);
        leftContent.setSpacing(false);
        leftContent.setFlexGrow(0, boardLayout);
        leftContent.setFlexGrow(1, footerImage); // התמונה היא זו שמתאימה את עצמה לגובה שנשאר

        // 4. טור סיכום
        selectedCoursesList = new VerticalLayout();
        selectedCoursesList.setPadding(false);
        selectedCoursesList.setSpacing(false);
        // גלילה פנימית בתוך רשימת הקורסים אם היא ארוכה מדי
        selectedCoursesList.getStyle().set("overflow-y", "auto");

        creditsLabel = new Span("טרם נבחרו קורסים");
        creditsLabel.getStyle()
                .set("font-weight", "600")
                .set("margin", "10px 0")
                .set("flex-shrink", "0");

        VerticalLayout summaryColumn = createSummaryColumn();

        // 5. המבנה המרכזי
        HorizontalLayout mainContent = new HorizontalLayout(leftContent, summaryColumn);
        mainContent.setSizeFull();
        mainContent.getStyle().set("overflow", "hidden");
        mainContent.setFlexGrow(3, leftContent);
        mainContent.setFlexGrow(1, summaryColumn);

        add(mainHeader, mainContent);
        expand(mainContent); // גורם ל-mainContent למלא את כל הגובה הנותר ב-UI
    }

    // הפעולה יוצרת עבור סמסטר עמודה מתאימה
    private CheckboxGroup<Course> createSemesterColumn(HorizontalLayout parent, String title, List<Course> courses,
            int semesterIdx) {
        VerticalLayout column = new VerticalLayout();
        column.setWidthFull();
        column.getStyle()
                .set("background-color", "white")
                .set("border-radius", "12px")
                .set("box-shadow", "0 4px 8px rgba(0,0,0,0.05)")
                .set("border-top", "6px solid " + getSemesterColor(semesterIdx))
                .set("padding", "15px");

        H4 colTitle = new H4(title);
        colTitle.getStyle().set("margin", "0 0 10px 0");

        CheckboxGroup<Course> group = new CheckboxGroup<>();
        group.setItems(courses);
        group.setItemLabelGenerator(course -> course.getName());
        group.addThemeVariants(CheckboxGroupVariant.LUMO_VERTICAL);
        group.addValueChangeListener(e -> refreshTotalSummary());

        column.add(colTitle, group);
        parent.add(column);
        parent.setFlexGrow(1, column);
        return group;
    }

    //יוצר את עמודת הסיכום בצד המסך
    private VerticalLayout createSummaryColumn() {
        VerticalLayout summary = new VerticalLayout();
        summary.setWidth("320px");
        summary.setHeightFull(); // חשוב: הטור יגיע בדיוק עד סוף ה-mainContent
        summary.getStyle()
                .set("background-color", "#ffffff")
                .set("border-radius", "15px")
                .set("box-shadow", "0 8px 20px rgba(0,0,0,0.1)")
                .set("padding", "20px")
                .set("box-sizing", "border-box");

        H3 summaryTitle = new H3("הסיכום שלי");
        summaryTitle.getStyle().set("margin-top", "0").set("font-size", "1.2em").set("flex-shrink", "0");

        finishChooseBtn = new Button("סיים בחירה ועבור למסך הבית");
        finishChooseBtn.setEnabled(false);// כל עוד הוא לא בחר שום קורס אז אי אפשר לעבור מסך
        finishChooseBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_SUCCESS);
        finishChooseBtn.setWidthFull();
        finishChooseBtn.getStyle().set("flex-shrink", "0");
        finishChooseBtn.addClickListener(clickEvent -> moveToHome());

        summary.add(summaryTitle, selectedCoursesList, creditsLabel, finishChooseBtn);
        summary.setFlexGrow(1, selectedCoursesList); // הרשימה תתכווץ/תתרחב בתוך הטור
        return summary;
    }

    // מסיים את בחירת הקורסים של המשתמש + מעבירה אותו למסך הבית
    private void moveToHome() {
        // שליפת המייל שנשמר הרגע בזמן ההרשמה
        String email = (String) VaadinSession.getCurrent().getAttribute("email");

        if (email == null || email.isEmpty()) {
            Notification.show("שגיאה: פרטי משתמש לא נמצאו. נא להירשם מחדש.");
            UI.getCurrent().navigate("/register");
            return;
        }

        // קריאה ל-Service עם המייל (שהוא ה-@Id שלך)
        User updatedUser = userService.addCoursesForUser(email, selectedCourses);

        if (updatedUser != null) {
            Notification.show("הרישום לקורסים הושלם!");
            VaadinSession.getCurrent().setAttribute("user", updatedUser);
            UI.getCurrent().navigate("/home");
        } else {
            Notification.show("שגיאה בשמירת הקורסים בבסיס הנתונים");
        }
    }

    // הפעולה מעדכנת את רשימת הקורסים שנבחרו ואת סיכום הבחירה של המשתמש
    private void refreshTotalSummary() {
        selectedCoursesList.removeAll();
        selectedCourses = new HashSet<>();

        for (CheckboxGroup<Course> group : semesterGroups.values()) {
            selectedCourses.addAll(group.getSelectedItems());
        }

        finishChooseBtn.setEnabled(!selectedCourses.isEmpty());
        if (selectedCourses.isEmpty()) {
            creditsLabel.setText("טרם נבחרו קורסים");
        } else {
            selectedCourses.forEach(c -> {
                Span s = new Span("• " + c.getName());
                s.getStyle()
                        .set("font-size", "0.85em")
                        .set("display", "block");

                selectedCoursesList.add(s);
            });
            creditsLabel.setText("קורסים: " + selectedCourses.size());
        }
    }

    //הפונק' מקבלת מספר סמסטר ומחזירה שם בעברית. נגיד סמסטר 1 הופך לסמסטר א
    private String getSemesterName(int i) {
        return switch (i) {
            case 1 -> "א'";
            case 2 -> "ב'";
            default -> "קיץ";
        };
    }

    //פונק' מקבלת מספר סמסטר ומחזירה את הצבע עבורו. נגיד סמסטר 1 הופך לצבע כחול 
    private String getSemesterColor(int i) {
        return switch (i) {
            case 1 -> "#4A90E2";
            case 2 -> "#50C878";
            default -> "#FFB347";
        };
    }
}