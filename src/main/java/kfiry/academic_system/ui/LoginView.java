package kfiry.academic_system.ui;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.Notification.Position;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;

import kfiry.academic_system.datamodels.User;
import kfiry.academic_system.services.UserService;

//import kfiry.academic_system.datamodels.User;

@Route("/")
public class LoginView extends HorizontalLayout {

    private TextField email;
    private PasswordField password;
    private UserService userService;

    public LoginView(UserService userService) {

        this.userService = userService;
        setSizeFull();
        setSpacing(false);
        setPadding(false);

        // =====================================================
        // LEFT SIDE (IMAGE - 70%)
        // =====================================================
        // יצירת דיב עבור הצד השמאלי. בצד זה תופיע התמונה
        Div leftSide = new Div();
        leftSide.setWidth("70%");
        leftSide.setHeightFull();
        leftSide.getStyle()
                .set("background-image", "url('images/LoginPhoto.png')")// השם של התמונה
                .set("background-size", "cover")// שלא ישארו שטחים ריקים על המסך גם אם זה אומר לחתוך אותם
                .set("background-position", "center")// ממקם את התמונה במרכז של הדיב
                .set("background-repeat", "no-repeat");// מונע מהתמונה להשתכפל - יכול להיות שהתמונה קטנה מידיי ואז היא
                                                       // תוצג פעמיים

        // =====================================================
        // RIGHT SIDE (LOGIN - 30%)
        // =====================================================
        // יצירת דיב עבור הצד הימני. בצד זה יופיע מסך ההתחברות וההרשמה
        Div rightSide = new Div();
        rightSide.setWidth("30%");
        rightSide.setHeightFull();

        rightSide.getStyle()
                .set("display", "flex")// מאפשר למקם רכיבים בצורה נוחה
                .set("justify-content", "center")// שהתוכן יהיה באמצע באמצע לרוחב ולא לאורך
                .set("align-items", "center")// ממרכז את התוכן בידיוק למרכז העמוד באנכיות
                .set("background", "rgba(255, 255, 255, 0.08)")// נותן רקע לבן ושקוף. הפרמטר הרביעי הוא שקיפות
                .set("backdrop-filter", "blur(25px)")// יוצר טשטוש עדין
                .set("border-left", "1px solid rgba(255,255,255,0.2)");// יוצר קו גבול בין התמונה למסך הקלדת נתונים
        // =====================================================
        // LOGIN FORM
        // =====================================================
        VerticalLayout form = new VerticalLayout();
        form.setWidth("80%");
        form.setPadding(false);
        form.setSpacing(true);

        H1 title = new H1("Login");
        title.getStyle()
                .set("color", "black")
                .set("font-size", "42px");

        email = new TextField();
        email.setPlaceholder("Email");
        email.setWidthFull();
        email.getStyle()
                .set("background", "rgba(255,255,255,0.2)")
                .set("border-radius", "20px")
                .set("backdrop-filter", "blur(10px)");

        password = new PasswordField();
        password.setPlaceholder("Password");
        password.setWidthFull();
        password.getStyle()
                .set("background", "rgba(255,255,255,0.2)")
                .set("border-radius", "20px")
                .set("backdrop-filter", "blur(10px)");

        Button loginButton = new Button("Sign In");
        loginButton.setWidthFull();
        loginButton.addClickListener(clickEvent -> loginStudent());

        loginButton.getStyle()
                .set("height", "50px")
                .set("border-radius", "25px")// מעגל את הפינות של הכפתור
                .set("border", "none")// מוריד את המסגרת של הכפתור מסביב
                .set("color", "white")// מגדיר את צבע הטקסט כלבן
                .set("font-size", "18px")// גודל הטקסט
                .set("font-weight", "bold")// עובי הטקסט
                .set("background", "linear-gradient(90deg, #6a5af9, #8f6dff)");// נותן רקע עם מעבר צבעים. מתחיל סגול חזק
                                                                               // והולך ומתבהר

        Button registerButton = new Button("Don't have an account? Sign up");
        registerButton.setWidthFull();
        registerButton.addClickListener(clickEvent -> createNewStudent());

        registerButton.getStyle()
                .set("height", "50px")
                .set("border-radius", "25px")// מעגל את הפינות של הכפתור
                .set("border", "none")// מוריד את המסגרת של הכפתור מסביב
                .set("color", "linear-gradient(90deg, #6a5af9, #8f6dff)")// נותן טקסט עם מעבר צבעים. מתחיל סגול חזק
                                                                         // והולך ומתבהר
                .set("font-size", "18px")// גודל הטקסט
                .set("font-weight", "bold")/// עובי הטקסט
                .set("background", "white");// רקע הכפתור - לבן

        Button loginLecturerButton = new Button("Login Lecturer/Admin");
        loginLecturerButton.setWidthFull();
        loginLecturerButton.addClickListener(clickEvent -> loginLecturer());

        loginLecturerButton.getStyle()
                .set("height", "50px")
                .set("border-radius", "25px")// מעגל את הפינות של הכפתור
                .set("border", "none")// מוריד את המסגרת של הכפתור מסביב
                .set("color", "black")// צבע הטקסט - שחור
                .set("font-size", "18px")// גודל הטקסט
                .set("font-weight", "bold")/// עובי הטקסט
                .set("background", "linear-gradient(90deg, #f3d61b, #a7ef2c)");// רקט הכפתור - נותן רקע עם מעבר צבעים.
                                                                               // מתחיל צהוב חזק והולך ומתחזק

        form.add(title, email, password, loginButton, registerButton, loginLecturerButton);

        rightSide.add(form);

        // =====================================================
        // ADD BOTH SIDES
        // =====================================================
        add(leftSide, rightSide);
    }

    private void loginLecturer() {
        UI.getCurrent().navigate("/loginLecturer");
    }

    private void createNewStudent() {
        UI.getCurrent().navigate("/register");
    }

    private void loginStudent() {
        String emailValue = email.getValue();
        String pw = password.getValue();

        if (emailValue == null || pw == null || !emailValue.contains("@")) {
            Notification.show("חובה למלא אימייל תקין וסיסמה", 7000, Position.MIDDLE);
            return;
        }

        try {
            User user = userService.authenticate(emailValue, pw);
            // שמירת האימייל בסשן
            VaadinSession.getCurrent().setAttribute("email", user.getEmail());
            VaadinSession.getCurrent().setAttribute("user", user);
            UI.getCurrent().navigate("/home");
        } catch (Exception exp) {
            Notification.show(exp.getMessage(), 5000, Position.MIDDLE);
        }
    }
}
