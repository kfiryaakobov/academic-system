package kfiry.academic_system.ui;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;

//import kfiry.academic_system.datamodels.User;

@Route("/")
public class LoginView extends HorizontalLayout {

    private TextField username;
    private PasswordField password;

    public LoginView() {

        setSizeFull();
        setSpacing(false);
        setPadding(false);
        // =====================================================
        // LEFT SIDE (IMAGE - 70%)
        // =====================================================
        Div leftSide = new Div();
        leftSide.setWidth("70%");
        leftSide.setHeightFull();
        leftSide.getStyle()
                .set("background-image", "url('images/course-system.jpeg')")
                .set("background-size", "cover")
                .set("background-position", "center")
                .set("background-repeat", "no-repeat");

        // =====================================================
        // RIGHT SIDE (LOGIN - 30%)
        // =====================================================
        Div rightSide = new Div();
        rightSide.setWidth("30%");
        rightSide.setHeightFull();

        rightSide.getStyle()
                .set("display", "flex")
                .set("justify-content", "center")
                .set("align-items", "center")
                .set("background", "rgba(255, 255, 255, 0.08)")
                .set("backdrop-filter", "blur(25px)")
                .set("-webkit-backdrop-filter", "blur(25px)")
                .set("border-left", "1px solid rgba(255,255,255,0.2)");
        // =====================================================
        // LOGIN FORM
        // =====================================================
        VerticalLayout form = new VerticalLayout();
        form.setWidth("80%");
        form.setPadding(false);
        form.setSpacing(true);

        H1 title = new H1("Sign In");
        title.getStyle()
                .set("color", "black")
                .set("font-size", "42px");

        username = new TextField();
        username.setPlaceholder("Username");
        username.setWidthFull();
        styleField(username);

        password = new PasswordField();
        password.setPlaceholder("Password");
        password.setWidthFull();
        styleField(password);

        Button loginButton = new Button("Sign In");
        loginButton.setWidthFull();
        loginButton.addClickListener(clickEvent -> insertUserToDB());

        loginButton.getStyle()
                .set("height", "50px")
                .set("border-radius", "25px")
                .set("border", "none")
                .set("color", "white")
                .set("font-size", "18px")
                .set("font-weight", "bold")
                .set("background", "linear-gradient(90deg, #6a5af9, #8f6dff)");

        form.add(title, username, password, loginButton);

        rightSide.add(form);

        // =====================================================
        // ADD BOTH SIDES
        // =====================================================
        add(leftSide, rightSide);
    }

    private void insertUserToDB() {
        String un = username.getValue();
        String pw = password.getValue();
        // validation check
        // צריך לבדוק אם המתשמש קיים במערכת
        if (un == null || pw == null || un.length() < 6)
            return;
        try {
            // User user = new User(un, pw);
            VaadinSession.getCurrent().setAttribute("username", un);
            UI.getCurrent().navigate("/choose"); // מעבר לדף בחירת הקורסים

            // userService.insertUser(user);
            // Notification.show("User inserted Ok!", 3000, Position.MIDDLE);

        } catch (Exception exp) {
            exp.printStackTrace();
            // Notification.show("User NOT inserted!" + exp.getMessage(), 5000,
            // Position.MIDDLE);
        }
    }

    private void styleField(TextField field) {
        field.getStyle()
                .set("background", "rgba(255,255,255,0.2)")
                .set("border-radius", "20px")
                .set("backdrop-filter", "blur(10px)");
    }

    private void styleField(PasswordField field) {
        field.getStyle()
                .set("background", "rgba(255,255,255,0.2)")
                .set("border-radius", "20px")
                .set("backdrop-filter", "blur(10px)");
    }
}
