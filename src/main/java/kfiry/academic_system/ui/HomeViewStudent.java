package kfiry.academic_system.ui;

import com.vaadin.flow.component.avatar.Avatar;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility;

@Route(value = "/home", layout = AppLayoutStudent.class)
public class HomeViewStudent extends VerticalLayout {

    public HomeViewStudent() {}
    //     setSizeFull();
    //     setPadding(false);
    //     setSpacing(false);
    //     getStyle().set("background-color", "#f8f9fa");// צבע רקע אפרפר בהיר
    //     getStyle().set("direction", "rtl"); // הגדרת כיווניות לימין-לשמאל
    //     getStyle().set("font-family", "Rubik, sans-serif"); // מומלץ להוסיף פונט עברי מתאים כמו רוביק

    //     // יצירת חלקי המסך
    //     //add(createHeader());
        
    //     HorizontalLayout mainContent = new HorizontalLayout();
    //     mainContent.setSizeFull();
    //     mainContent.setPadding(true);
    //     mainContent.setSpacing(true);
        
    //     // סדר הוספת הרכיבים הוא מימין לשמאל בגלל ה-RTL
    //     mainContent.add(createRightPanel(), createCenterSchedulePanel(), createLeftPanel());
        
    //     // חלוקת הרוחב בין העמודות
    //     mainContent.setFlexGrow(1.5, mainContent.getComponentAt(0)); // פאנל ימני
    //     mainContent.setFlexGrow(4, mainContent.getComponentAt(1));   // מערכת השעות (הכי רחב)
    //     mainContent.setFlexGrow(1.5, mainContent.getComponentAt(2)); // פאנל שמאלי

    //     add(mainContent);
    // }

    // // --- פאנל ימני (הקורסים שלי + סיכום) ---
    // private VerticalLayout createRightPanel() {
    //     VerticalLayout panel = new VerticalLayout();
    //     panel.setPadding(false);
    //     panel.setSpacing(true);

    //     // כרטיס הקורסים שלי
    //     Div myCoursesCard = createCard();
    //     H3 coursesTitle = new H3("הקורסים שלי");
    //     coursesTitle.addClassNames(LumoUtility.FontSize.MEDIUM, LumoUtility.Margin.Top.NONE);
        
    //     VerticalLayout coursesList = new VerticalLayout();
    //     coursesList.setPadding(false);
    //     coursesList.add(createCourseListItem("מבוא לכלכלה", "#a8e6cf"));
    //     coursesList.add(createCourseListItem("סטטיסטיקה", "#ffd3b6"));
    //     coursesList.add(createCourseListItem("חשבונאות פיננסית", "#dcbadd"));
    //     coursesList.add(createCourseListItem("שיווק", "#ffb6b9"));
        
    //     Button seeMoreBtn = new Button("צפה בפרטים");
    //     seeMoreBtn.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        
    //     myCoursesCard.add(coursesTitle, coursesList, seeMoreBtn);

    //     // כרטיס סיכום
    //     Div summaryCard = createCard();
    //     H3 summaryTitle = new H3("סיכום");
    //     summaryTitle.addClassNames(LumoUtility.FontSize.MEDIUM, LumoUtility.Margin.Top.NONE);
        
    //     HorizontalLayout statsLayout = new HorizontalLayout();
    //     statsLayout.setWidthFull();
    //     statsLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.BETWEEN);
    //     statsLayout.add(createStatBox("18", "נקודות זכות", "#f3e8ff"));
    //     statsLayout.add(createStatBox("24", "שעות שבועיות", "#e0f2fe"));
    //     statsLayout.add(createStatBox("8", "קורסים משובצים", "#dcfce7"));
        
    //     summaryCard.add(summaryTitle, statsLayout);

    //     panel.add(myCoursesCard, summaryCard);
    //     return panel;
    // }

    // private HorizontalLayout createCourseListItem(String name, String color) {
    //     HorizontalLayout layout = new HorizontalLayout();
    //     layout.setWidthFull();
    //     layout.setJustifyContentMode(FlexComponent.JustifyContentMode.BETWEEN);
    //     layout.setAlignItems(FlexComponent.Alignment.CENTER);
        
    //     Div dot = new Div();
    //     dot.setWidth("10px");
    //     dot.setHeight("10px");
    //     dot.getStyle().set("border-radius", "50%");
    //     dot.getStyle().set("background-color", color);
        
    //     Span title = new Span(name);
    //     title.getStyle().set("flex-grow", "1");
    //     title.getStyle().set("margin-right", "10px");
        
    //     Icon deleteIcon = VaadinIcon.CLOSE_SMALL.create();
    //     deleteIcon.setColor("gray");
    //     deleteIcon.getStyle().set("cursor", "pointer");
        
    //     layout.add(deleteIcon, title, dot); // RTL order
    //     return layout;
    // }

    // private Div createStatBox(String number, String label, String bgColor) {
    //     Div box = new Div();
    //     box.getStyle().set("background-color", bgColor);
    //     box.getStyle().set("padding", "10px");
    //     box.getStyle().set("border-radius", "8px");
    //     box.getStyle().set("text-align", "center");
    //     box.getStyle().set("flex", "1");
    //     box.getStyle().set("margin", "0 5px");

    //     Span numSpan = new Span(number);
    //     numSpan.addClassNames(LumoUtility.FontSize.XLARGE, LumoUtility.FontWeight.BOLD);
    //     Span labelSpan = new Span(label);
    //     labelSpan.addClassNames(LumoUtility.FontSize.XXSMALL);
    //     labelSpan.getStyle().set("display", "block");

    //     box.add(numSpan, labelSpan);
    //     return box;
    // }

    // // --- מערכת שעות מרכזית ---
    // private VerticalLayout createCenterSchedulePanel() {
    //     VerticalLayout panel = new VerticalLayout();
    //     panel.getStyle().set("background-color", "white");
    //     panel.getStyle().set("border-radius", "12px");
    //     panel.getStyle().set("box-shadow", "0 4px 6px rgba(0,0,0,0.05)");
    //     panel.setPadding(true);

    //     // הדר מערכת שעות (כפתור הוספה, תאריכים)
    //     HorizontalLayout tools = new HorizontalLayout();
    //     tools.setWidthFull();
    //     tools.setJustifyContentMode(FlexComponent.JustifyContentMode.BETWEEN);
    //     tools.setAlignItems(FlexComponent.Alignment.CENTER);

    //     Button addCourseBtn = new Button("שיבוץ קורסים", VaadinIcon.PLUS.create());
    //     addCourseBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        
    //     Span dateRange = new Span("<  9-15 ביוני 2024 (שבוע 11)  >");
    //     dateRange.addClassNames(LumoUtility.FontWeight.BOLD);
        
    //     Button todayBtn = new Button("היום");

    //     tools.add(todayBtn, dateRange, addCourseBtn); // RTL order

    //     // הגריד של המערכת (Mockup בסיסי באמצעות CSS Grid)
    //     Div scheduleGrid = new Div();
    //     scheduleGrid.getStyle().set("display", "grid");
    //     scheduleGrid.getStyle().set("grid-template-columns", "60px repeat(6, 1fr)");
    //     scheduleGrid.getStyle().set("grid-gap", "1px");
    //     scheduleGrid.getStyle().set("background-color", "#e5e7eb"); // צבע הקווים
    //     scheduleGrid.getStyle().set("margin-top", "20px");
    //     scheduleGrid.setWidthFull();
    //     scheduleGrid.setHeight("600px");

    //     // כאן ניתן להוסיף לולאות שיוצרות את תאי הזמן ואת בלוקי הקורסים לפי הנתונים של הסטודנט
    //     // למטרת ההדגמה אשים טקסט זמני כפלייסולדר:
    //     Div placeholder = new Div(new Span("כאן ייכנס הרכיב שמצייר את הגריד ובלוקי הקורסים (דורש לוגיקת מיקומים מתקדמת)"));
    //     placeholder.getStyle().set("background", "white");
    //     placeholder.getStyle().set("grid-column", "1 / -1");
    //     placeholder.getStyle().set("padding", "20px");
    //     placeholder.getStyle().set("text-align", "center");
    //     scheduleGrid.add(placeholder);

    //     panel.add(tools, scheduleGrid);
    //     return panel;
    // }

    // // --- פאנל שמאלי (סינונים ולוח שנה קטן) ---
    // private VerticalLayout createLeftPanel() {
    //     VerticalLayout panel = new VerticalLayout();
    //     panel.setPadding(false);
    //     panel.setSpacing(true);

    //     // לוח שנה קטן (Mockup)
    //     Div miniCalendarCard = createCard();
    //     H3 monthTitle = new H3("יוני 2024");
    //     monthTitle.addClassNames(LumoUtility.FontSize.MEDIUM, LumoUtility.Margin.Top.NONE, LumoUtility.TextAlignment.CENTER);
    //     // *אפשר להשתמש פה בקומפוננטת DatePicker פתוחה או לבנות גריד קטן*
    //     miniCalendarCard.add(monthTitle, new Span("[לוח שנה קטן]"));

    //     // סינונים
    //     Div filtersCard = createCard();
    //     H3 filtersTitle = new H3("סינונים");
    //     filtersTitle.addClassNames(LumoUtility.FontSize.MEDIUM, LumoUtility.Margin.Top.NONE);

    //     ComboBox<String> semesterCb = new ComboBox<>("סמסטר");
    //     semesterCb.setPlaceholder("קיץ תשפ\"ד");
    //     semesterCb.setWidthFull();

    //     ComboBox<String> facultyCb = new ComboBox<>("פקולטה");
    //     facultyCb.setPlaceholder("כל הפקולטות");
    //     facultyCb.setWidthFull();

    //     ComboBox<String> hoursCb = new ComboBox<>("שעות");
    //     hoursCb.setPlaceholder("כל השעות");
    //     hoursCb.setWidthFull();

    //     Button resetFiltersBtn = new Button("איפוס סינונים");
    //     resetFiltersBtn.setWidthFull();
    //     resetFiltersBtn.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

    //     filtersCard.add(filtersTitle, semesterCb, facultyCb, hoursCb, resetFiltersBtn);

    //     panel.add(miniCalendarCard, filtersCard);
    //     return panel;
    // }

    // // --- מתודת עזר ליצירת "כרטיסייה" ---
    // private Div createCard() {
    //     Div card = new Div();
    //     card.getStyle().set("background-color", "white");
    //     card.getStyle().set("border-radius", "12px");
    //     card.getStyle().set("padding", "20px");
    //     card.getStyle().set("box-shadow", "0 4px 6px rgba(0,0,0,0.05)");
    //     card.setWidthFull();
    //     return card;
    // }
}