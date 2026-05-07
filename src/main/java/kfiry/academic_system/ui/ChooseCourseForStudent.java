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
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import kfiry.academic_system.datamodels.Course;
import kfiry.academic_system.services.MongoService;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Route(value = "/choose", layout = AppLayoutStudent.class)
public class ChooseCourseForStudent extends VerticalLayout {

    private MongoService mongoService;
    private List<Course> allCourses;
    private VerticalLayout selectedCoursesList;
    private Span creditsLabel;
    private Map<Integer, CheckboxGroup<Course>> semesterGroups;
    private Button calculateBtn;

    public ChooseCourseForStudent(MongoService mongoService) {
        this.mongoService = mongoService;
        this.allCourses = mongoService.getAllCourses();
        
        // --- הגדרות קריטיות למניעת יציאה מהמסך ---
        getElement().setAttribute("dir", "rtl");
        setSizeFull(); // מגדיר גובה ורוחב 100%
        setPadding(true);
        setSpacing(false);
        getStyle().set("background-color", "#f8f9fa");
        getStyle().set("overflow", "hidden"); // חובה: מבטל גלילה של כל העמוד
        getStyle().set("box-sizing", "border-box");

        // 1. כותרת
        H3 mainHeader = new H3("בחירת קורסים");
        mainHeader.getStyle()
                .set("margin", "0 0 15px 0")
                .set("color", "#1a2a3a")
                .set("flex-shrink", "0"); // מבטיח שהכותרת לא תתכווץ
        setAlignSelf(Alignment.CENTER, mainHeader);

        // 2. לוח הסמסטרים (החלק העליון)
        HorizontalLayout boardLayout = new HorizontalLayout();
        boardLayout.setWidthFull();
        boardLayout.setSpacing(true);
        boardLayout.setPadding(false);
        boardLayout.getStyle().set("flex-shrink", "0"); // הסמסטרים יתפסו כמה מקום שהם צריכים

        Map<Integer, List<Course>> coursesBySemester = allCourses.stream()
                .collect(Collectors.groupingBy(Course::getDuration));

        semesterGroups = coursesBySemester.keySet().stream().collect(Collectors.toMap(
                s -> s,
                s -> createSemesterColumn(boardLayout, "סמסטר " + getSemesterName(s), coursesBySemester.get(s), s)
        ));

        // 3. תמונה תחתונה
        Image footerImage = new Image("images/chooseScreenPhoto.jpeg", "Academic Background");
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
        this.selectedCoursesList = new VerticalLayout();
        this.selectedCoursesList.setPadding(false);
        this.selectedCoursesList.setSpacing(false);
        // גלילה פנימית בתוך רשימת הקורסים אם היא ארוכה מדי
        this.selectedCoursesList.getStyle().set("overflow-y", "auto"); 

        this.creditsLabel = new Span("טרם נבחרו קורסים");
        this.creditsLabel.getStyle()
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

    private CheckboxGroup<Course> createSemesterColumn(HorizontalLayout parent, String title, List<Course> courses, int semesterIdx) {
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
        group.setItemLabelGenerator(Course::getName);
        group.addThemeVariants(CheckboxGroupVariant.LUMO_VERTICAL);
        group.addValueChangeListener(e -> refreshTotalSummary());

        column.add(colTitle, group);
        parent.add(column);
        parent.setFlexGrow(1, column);
        return group;
    }

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
        
        calculateBtn = new Button("סיים בחירה ועבור למסך הבית");
        calculateBtn.setEnabled(false);
        calculateBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_SUCCESS);
        calculateBtn.setWidthFull();
        calculateBtn.getStyle().set("flex-shrink", "0");
        calculateBtn.addClickListener(clickEvent -> moveToHome());

        summary.add(summaryTitle, selectedCoursesList, creditsLabel, calculateBtn);
        summary.setFlexGrow(1, selectedCoursesList); // הרשימה תתכווץ/תתרחב בתוך הטור
        return summary;
    }

    private void moveToHome() {
       UI.getCurrent().navigate("/home");
    }

    private void refreshTotalSummary() {
        selectedCoursesList.removeAll();
        Set<Course> allSelected = semesterGroups.values().stream()
                .flatMap(g -> g.getSelectedItems().stream())
                .collect(Collectors.toSet());

        calculateBtn.setEnabled(!allSelected.isEmpty());

        if (allSelected.isEmpty()) {
            creditsLabel.setText("טרם נבחרו קורסים");
        } else {
            allSelected.forEach(c -> {
                Span s = new Span("• " + c.getName());
                s.getStyle().set("font-size", "0.85em").set("display", "block");
                selectedCoursesList.add(s);
            });
            creditsLabel.setText("קורסים: " + allSelected.size());
        }
    }

    private String getSemesterName(int i) {
        return switch (i) {
            case 1 -> "א'";
            case 2 -> "ב'";
            default -> "קיץ";
        };
    }

    private String getSemesterColor(int i) {
        return switch (i) {
            case 1 -> "#4A90E2";
            case 2 -> "#50C878";
            default -> "#FFB347";
        };
    }
}