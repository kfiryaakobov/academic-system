package kfiry.academic_system.ui;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
//import com.vaadin.flow.server.VaadinSession;

@Route(value = "", layout = AppLayoutWelcome.class)
public class WelcomeView extends VerticalLayout {

    public WelcomeView() {
        // הגדרות בסיסיות לעמוד
        setSpacing(false);
        setPadding(false);
        setSizeFull();
        getStyle().set("direction", "rtl");

        // 1. יצירת רקע מטושטש המבוסס על צבעי התמונה (כתום, קרם, חום)
        configureBackground();


        // 2. תוכן מרכזי
        VerticalLayout mainContent = createMainContent();

        // הוספה לעמוד
        add(mainContent);
        expand(mainContent);
    }

    private void configureBackground() {
        // יצירת גרדיאנט שמשלב את הצבעים מהתמונה (חום ספרייה, כתום דף, צהוב תאורה)
        getStyle().set("background", "radial-gradient(circle at top left, #f3d5b5, #e7bc91), " +
                "linear-gradient(135deg, #d4a373 0%, #a98467 100%)");
        getStyle().set("background-attachment", "fixed");

        // שכבת טשטוש מעל הרקע
        Div blurOverlay = new Div();
        blurOverlay.getStyle().set("position", "fixed");
        blurOverlay.getStyle().set("top", "0");
        blurOverlay.getStyle().set("left", "0");
        blurOverlay.getStyle().set("width", "100%");
        blurOverlay.getStyle().set("height", "100%");
        blurOverlay.getStyle().set("backdrop-filter", "blur(40px)");
        blurOverlay.getStyle().set("z-index", "-1");
        add(blurOverlay);
    }


    private VerticalLayout createMainContent() {
        VerticalLayout content = new VerticalLayout();
        content.setSizeFull();
        content.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        content.setAlignItems(FlexComponent.Alignment.CENTER);
        content.setSpacing(true);

        // כרטיס לבן חצי שקוף שמחזיק את התוכן (Glass Effect)
        VerticalLayout card = new VerticalLayout();
        card.setWidth("auto");
        card.setMaxWidth("800px");
        card.setPadding(true);
        card.setAlignItems(FlexComponent.Alignment.CENTER);
        card.getStyle().set("background", "rgba(255, 255, 255, 0.4)");
        card.getStyle().set("border-radius", "24px");
        card.getStyle().set("box-shadow", "0 8px 32px rgba(0, 0, 0, 0.1)");
        card.getStyle().set("border", "1px solid rgba(255, 255, 255, 0.5)");

        // התמונה החדשה שלך
        Image heroImage = new Image("images/informationPhoto.jpg", "מערכת שיבוץ");
        heroImage.setWidth("350px");
        heroImage.getStyle().set("border-radius", "15px");
        heroImage.getStyle().set("box-shadow", "0 4px 15px rgba(0,0,0,0.2)");

        // כותרת
        H1 title = new H1("מערכת לשיבוץ קורסים אקדמיים");
        title.getStyle().set("color", "#3e2723");
        title.getStyle().set("margin-top", "20px");
        title.getStyle().set("text-align", "center");

        // תיאור
        Paragraph description = new Paragraph(
                "פתרון חכם ומתקדם לניהול אילוצים אקדמיים, בניית מערכות שעות וייעול תהליכי הלמידה במוסדות חינוך.");
        description.getStyle().set("color", "#5d4037");
        description.getStyle().set("text-align", "center");
        description.getStyle().set("font-size", "1.1rem");

        // פוטר (קרדיט)
        Paragraph footer = new Paragraph(
                "מגיש: כפיר יוסף יעקובוב | שנת פרויקט: 2026 | מכללה: קריית נוער | תז: 216232470 ");
        footer.getStyle().set("color", "#8d6e63");
        footer.getStyle().set("font-size", "0.9rem");
        footer.getStyle().set("margin-top", "40px");

        // String countOnlines = (String) VaadinSession.getCurrent().getAttribute("counter");
        // Paragraph onlineViews = new Paragraph("Online Viewrs " + countOnlines);
        // onlineViews.getStyle().set("color", "#111010");
        // onlineViews.getStyle().set("font-size", "2rem");

        card.add(heroImage, title, description, footer);
        content.add(card);

        return content;
    }
}