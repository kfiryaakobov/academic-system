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
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.server.VaadinSession;

import kfiry.academic_system.datamodels.Lecturer;

public class AppLayoutLecturer extends AppLayout{
    private AppLayoutLecturer() {
        HorizontalLayout topNavbar = new HorizontalLayout();
        topNavbar.setWidthFull();
        topNavbar.setAlignItems(FlexComponent.Alignment.CENTER);
        topNavbar.setJustifyContentMode(FlexComponent.JustifyContentMode.BETWEEN); // מפזר לימין אמצע ושמאל
        topNavbar.getStyle()
                .set("padding", "10px 20px")
                .set("border-bottom", "1px solid #eaeaea")
                .set("background", "white");

        // 1. ימין: לוגו
        HorizontalLayout logo = new HorizontalLayout();
        Icon cap = VaadinIcon.ACADEMY_CAP.create();
        cap.setColor("#1a56db");
        H2 title = new H2("איזור אישי סגל");
        title.getStyle().set("margin", "0").set("font-size", "var(--lumo-font-size-l)");
        logo.add(cap, title);
        logo.setAlignItems(FlexComponent.Alignment.CENTER);

        // 2. אמצע: תפריט ניווט
        HorizontalLayout menu = new HorizontalLayout();

        // יצירת כפתור התנתקות
        Button logoutButton = new Button("התנתקות");
        logoutButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY); // העיצוב הזה מעלים את הרקע והמסגרת של הכפתור
        logoutButton.getStyle().set("font-size", "var(--lumo-font-size-m)");

        logoutButton.addClickListener(e -> {
            // ניקוי מלא של התיק (הסשן) כולל כל המשתנים השמורים
            VaadinSession.getCurrent().getSession().invalidate();
            VaadinSession.getCurrent().close();

            // ניווט חזרה למסך הלוגין
            UI.getCurrent().navigate(LoginView.class);
        });

        menu.add(logoutButton);

        // 3. שמאל: פרטי המרצה
        Lecturer lecturer = (Lecturer) VaadinSession.getCurrent().getAttribute("lecturer");
        String nameLecturer = " ";

        if (lecturer != null) {
            nameLecturer = lecturer.getName();
        }

        Avatar userAvatar = new Avatar(nameLecturer);
        Span userInfo = new Span("שלום, " + nameLecturer);
        userInfo.getStyle().set("font-weight", "600");

        HorizontalLayout userSection = new HorizontalLayout(userInfo, userAvatar);
        userSection.setAlignItems(FlexComponent.Alignment.CENTER);

        topNavbar.add(logo, menu, userSection);
        addToNavbar(topNavbar);
    }
}
