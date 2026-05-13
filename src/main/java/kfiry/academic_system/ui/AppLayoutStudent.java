package kfiry.academic_system.ui;

import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.avatar.Avatar;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.orderedlayout.FlexComponent.JustifyContentMode;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.server.VaadinSession;

public class AppLayoutStudent extends AppLayout {

    private VerticalLayout topNavbarPanel;
    private HorizontalLayout topNavbar;
    private Avatar userAvatar;
    private Span userInfo;

    public AppLayoutStudent() {
        getElement().setAttribute("dir", "rtl");
        buildNavbar();
        addToNavbar(topNavbarPanel);
    }

    private void buildNavbar() {
        topNavbarPanel = new VerticalLayout();
        topNavbarPanel.setSpacing(false);
        topNavbarPanel.setPadding(false);
        topNavbarPanel.getStyle()
                .set("padding", "10px 20px")
                .set("border-bottom", "1px solid #eaeaea")
                .set("background", "white");

        // 1. ימין: לוגו וכותרת
        HorizontalLayout logo = new HorizontalLayout();
        Icon cap = VaadinIcon.ACADEMY_CAP.create();
        cap.setColor("#1a56db");
        H2 title = new H2("איזור אישי סטודנטים");
        title.getStyle().set("margin", "0").set("font-size", "var(--lumo-font-size-l)");
        logo.add(cap, title);
        logo.setAlignItems(Alignment.CENTER);

        // 2. אמצע: תפריט ניווט
        HorizontalLayout menu = new HorizontalLayout();
        menu.setSpacing(true);
        menu.add(
                new RouterLink("דף הבית", HomeStudentView.class),
                new RouterLink("מסך התחברות", LoginView.class));

        // 3. שמאל: פרטי משתמש ואוואטר
        String username = (String) VaadinSession.getCurrent().getAttribute("username");

        userAvatar = new Avatar(username);
        userInfo = new Span("שלום, " + username);
        userInfo.getStyle().set("font-weight", "600");

        HorizontalLayout userSection = new HorizontalLayout(userInfo, userAvatar);
        userSection.setAlignItems(Alignment.CENTER);

        // =========================
        // איחוד הכל לשורה אחת
        // =========================
        topNavbar = new HorizontalLayout();
        topNavbar.setWidthFull();
        topNavbar.setAlignItems(Alignment.CENTER);

        // זה מה שדוחף את החלקים לצדדים (ימין, אמצע, שמאל)
        topNavbar.setJustifyContentMode(JustifyContentMode.BETWEEN);

        // הוספה לפי הסדר (מימין לשמאל בגלל ה-RTL)
        topNavbar.add(logo, menu, userSection);

        // מוסיפים רק את ה-topNavbar לפאנל הראשי (בלי ה-userInfo הנפרד ובלי ה-Hr)
        topNavbarPanel.add(topNavbar);
    }
}