package kfiry.academic_system.ui;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.avatar.Avatar;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.orderedlayout.FlexComponent.JustifyContentMode;
import com.vaadin.flow.server.VaadinSession;

import kfiry.academic_system.datamodels.User;
import kfiry.academic_system.services.WeatherService; // ייבוא של הסרוויס 

public class AppLayoutStudent extends AppLayout {

    private VerticalLayout topNavbarPanel;
    private HorizontalLayout topNavbar;
    private Avatar userAvatar;
    private Span userInfo;
    private WeatherService weatherService;

    public AppLayoutStudent(WeatherService weatherService) {
        this.weatherService = weatherService;
        getElement().setAttribute("dir", "rtl");
        buildNavbar(weatherService); // 2. העברנו אותו לפונקציית הבנייה
        addToNavbar(topNavbarPanel);
    }

    private void buildNavbar(WeatherService weatherService) {
        topNavbarPanel = new VerticalLayout();
        topNavbarPanel.setSpacing(false);
        topNavbarPanel.setPadding(false);
        topNavbarPanel.getStyle()
                .set("padding", "10px 20px")
                .set("border-bottom", "1px solid #eaeaea")
                .set("background", "white");

        // --- 1. יצירת תגית מזג האוויר ---
        String weatherText = weatherService.getUserWeatherRecommendation();
        Span weatherBadge = new Span(weatherText);
        weatherBadge.getStyle()
            .set("background-color", "#e0f7fa")
            .set("color", "#006064")
            .set("padding", "4px 12px")
            .set("border-radius", "20px")
            .set("font-size", "14px")
            .set("font-weight", "600")
            // מוסיף רווח מימין כדי שלא יידבק לטקסט של הכותרת
            .set("margin-right", "15px"); 

        // --- 2. ימין: לוגו, כותרת, ומזג האוויר ---
        HorizontalLayout logo = new HorizontalLayout();
        Icon cap = VaadinIcon.ACADEMY_CAP.create();
        cap.setColor("#1a56db");
        H2 title = new H2("איזור אישי סטודנטים");
        title.getStyle().set("margin", "0").set("font-size", "var(--lumo-font-size-l)");
        
        // כאן הוספנו את ה-weatherBadge יחד עם הכובע והכותרת!
        logo.add(cap, title, weatherBadge);
        logo.setAlignItems(Alignment.CENTER);

        // --- 3. אמצע: תפריט ניווט (רק התנתקות עכשיו) ---
        HorizontalLayout menu = new HorizontalLayout();
        menu.setSpacing(true);
        menu.setAlignItems(Alignment.CENTER);

        Button logoutButton = new Button("התנתקות");
        logoutButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        logoutButton.getStyle().set("font-size", "var(--lumo-font-size-m)");

        logoutButton.addClickListener(e -> {

            // String countOnlines = (String) VaadinSession.getCurrent().getAttribute("counter");
            // int number = Integer.parseInt(countOnlines);
            // System.out.println("num" + number);
            // number--;
            // String num = number + " ";
            // System.out.println("newCount" + num);
            // VaadinSession.getCurrent().getSession().setAttribute("counter",num);

            VaadinSession.getCurrent().getSession().invalidate();
            VaadinSession.getCurrent().close();
            UI.getCurrent().navigate(LoginView.class);
        });

        // מזג האוויר הוסר מפה, נשאר רק כפתור ההתנתקות
        menu.add(logoutButton);

        // --- 4. שמאל: פרטי משתמש ואוואטר ---
        User user = (User) VaadinSession.getCurrent().getAttribute("user");
        String displayName = "אורח";

        if (user != null) {
            displayName = user.getUsername();
        }

        userAvatar = new Avatar(displayName);
        userInfo = new Span("שלום, " + displayName);
        userInfo.getStyle().set("font-weight", "600");

        HorizontalLayout userSection = new HorizontalLayout(userInfo, userAvatar);
        userSection.setAlignItems(Alignment.CENTER);

        // =========================
        // איחוד הכל לשורה אחת
        // =========================
        topNavbar = new HorizontalLayout();
        topNavbar.setWidthFull();
        topNavbar.setAlignItems(Alignment.CENTER);
        topNavbar.setJustifyContentMode(JustifyContentMode.BETWEEN);

        topNavbar.add(logo, menu, userSection);

        topNavbarPanel.add(topNavbar);
    }
}