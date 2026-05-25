package kfiry.academic_system.ui;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;

public class AppLayoutWelcome extends AppLayout{
    public AppLayoutWelcome(){
        HorizontalLayout navBar = new HorizontalLayout();
        navBar.setWidthFull();
        navBar.setPadding(true);
        navBar.setJustifyContentMode(FlexComponent.JustifyContentMode.BETWEEN);
        navBar.setAlignItems(FlexComponent.Alignment.CENTER);
        navBar.getStyle().set("background", "#e1ba95");
        navBar.getStyle().set("backdrop-filter", "blur(10px)");
        navBar.getStyle().set("border-bottom", "1px solid rgba(255, 255, 255, 0.3)");
        navBar.setHeight("70px");
        navBar.getElement().setAttribute("dir", "rtl");

        // יצירת האייקון של כובע הסטודנט
        Icon capIcon = VaadinIcon.ACADEMY_CAP.create();
        capIcon.getStyle().set("color", "#3e2723");
        capIcon.getStyle().set("margin-left", "10px"); // רווח קטן בין האייקון לטקסט

        // טקסט הלוגו
        Span logoText = new Span("Academic System");
        logoText.getStyle().set("font-weight", "bold");
        logoText.getStyle().set("font-size", "1.2rem");
        logoText.getStyle().set("color", "#3e2723");

        // איחוד האייקון והטקסט ביחד
        HorizontalLayout logoLayout = new HorizontalLayout(capIcon, logoText);
        logoLayout.setAlignItems(FlexComponent.Alignment.CENTER);
        logoLayout.setSpacing(false); // ביטלנו את הרווח הדיפולטיבי כי השתמשנו ב-margin

        // כפתורי ניווט
        Button loginBtn = new Button("התחברות", e -> UI.getCurrent().navigate("login"));
        loginBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        loginBtn.getStyle().set("background-color", "#6d4c41");

        Button registerBtn = new Button("הרשמה", e -> UI.getCurrent().navigate("register"));
        registerBtn.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        registerBtn.getStyle().set("color", "#3e2723");

        HorizontalLayout buttons = new HorizontalLayout(registerBtn, loginBtn);

        // שינוי כאן: מוסיפים את logoLayout במקום ה-Span הבודד
        navBar.add(logoLayout, buttons);
        addToNavbar(navBar);
    }
}
