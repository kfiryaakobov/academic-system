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

import kfiry.academic_system.datamodels.Admin;

public class AppLayoutAdmin extends AppLayout {

    private VerticalLayout topNavbarPanel;
    private HorizontalLayout topNavbar;
    private Avatar userAvatar;
    private Span userInfo;

    public AppLayoutAdmin() {
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

        // 1. ימין: לוגו וכותרת (צבע אדום למנהל)
        HorizontalLayout logo = new HorizontalLayout();
        Icon cap = VaadinIcon.ACADEMY_CAP.create();
        cap.setColor("#e11d48");
        H2 title = new H2("איזור ניהול מערכת");
        title.getStyle().set("margin", "0").set("font-size", "var(--lumo-font-size-l)");
        logo.add(cap, title);
        logo.setAlignItems(Alignment.CENTER);

        // 2. אמצע: תפריט ניווט וכפתור יציאה
        HorizontalLayout menu = new HorizontalLayout();
        menu.setSpacing(true);
        menu.setAlignItems(Alignment.CENTER);

        Button logoutButton = new Button("התנתקות");
        logoutButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        logoutButton.getStyle().set("font-size", "var(--lumo-font-size-m)");
        logoutButton.addClickListener(e -> {
            VaadinSession.getCurrent().getSession().invalidate();
            VaadinSession.getCurrent().close();
            UI.getCurrent().navigate(LoginLecturerView.class);
        });

        Button homeButton = new Button("מסך הבית");
        homeButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        homeButton.getStyle().set("font-size", "var(--lumo-font-size-m)");
        homeButton.addClickListener(e -> {
            UI.getCurrent().navigate(HomeAdminView.class);
        });

        Button addDataButton = new Button("מסך הוספת הנתונים");
        addDataButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        addDataButton.getStyle().set("font-size", "var(--lumo-font-size-m)");
        addDataButton.addClickListener(e -> {
            UI.getCurrent().navigate(AdminAddDataView.class);
        });

        Button viewDataButton = new Button("מסך הצגת הנתונים");
        viewDataButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        viewDataButton.getStyle().set("font-size", "var(--lumo-font-size-m)");
        viewDataButton.addClickListener(e -> {
            UI.getCurrent().navigate(AdminDataView.class);
        });

        menu.add(logoutButton, homeButton, addDataButton, viewDataButton);

        // 3. שמאל: פרטי מנהל ואוואטר
        Admin admin = (Admin) VaadinSession.getCurrent().getAttribute("admin");
        String adminName = admin != null ? admin.getName() : "מנהל";

        userAvatar = new Avatar(adminName);
        userInfo = new Span("שלום, " + adminName);
        userInfo.getStyle().set("font-weight", "600");

        HorizontalLayout userSection = new HorizontalLayout(userInfo, userAvatar);
        userSection.setAlignItems(Alignment.CENTER);

        // איחוד לשורה אחת
        topNavbar = new HorizontalLayout();
        topNavbar.setWidthFull();
        topNavbar.setAlignItems(Alignment.CENTER);
        topNavbar.setJustifyContentMode(JustifyContentMode.BETWEEN);
        topNavbar.add(logo, menu, userSection);

        topNavbarPanel.add(topNavbar);
    }
}