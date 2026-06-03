package kfiry.academic_system.ui;

import com.vaadin.flow.component.HasSize;
import com.vaadin.flow.component.HasStyle;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.Notification.Position;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;

import kfiry.academic_system.datamodels.User;
import kfiry.academic_system.services.UserService;
import kfiry.academic_system.utilities.PasswordHelper;

@Route("/register")
public class RegisterStudentView extends VerticalLayout {

    private TextField username;
    private PasswordField password;
    private EmailField email;
    private TextField phone;
    private IntegerField age;
    private RadioButtonGroup<String> gender;
    private UserService userService;

    public RegisterStudentView(UserService userService) {
        // =====================================================
        // MAIN LAYOUT (FULL SCREEN BACKGROUND)
        // =====================================================
        this.userService = userService;
        setSizeFull();
        setJustifyContentMode(JustifyContentMode.CENTER); // ממרכז את הבועה אנכית
        setAlignItems(Alignment.CENTER); // ממרכז את הבועה אופקית

        getStyle()
                .set("background-image", "url('images/RegisterPhoto.png')")
                .set("background-size", "cover")
                .set("background-position", "center")
                .set("background-repeat", "no-repeat");

        // =====================================================
        // THE "BUBBLE" (GLASSMORPHISM CONTAINER)
        // =====================================================
        // שינוי ל-VerticalLayout כדי שהטופס והכפתור יסתדרו מלמעלה למטה בצורה נוחה
        VerticalLayout bubble = new VerticalLayout();
        bubble.setWidth("800px");
        bubble.setHeight("auto");
        bubble.setPadding(true);
        bubble.setSpacing(true);

        bubble.getStyle()
                .set("background", "rgba(255, 255, 255, 0.15)") // רקע שקוף למחצה
                .set("backdrop-filter", "blur(25px)") // אפקט הטשטוש (Glassmorphism)
                .set("border-radius", "30px") // פינות עגולות לבועה
                .set("border", "1px solid rgba(255,255,255,0.3)")
                .set("box-shadow", "0 8px 32px 0 rgba(0, 0, 0, 0.37)"); // צל עדין

        // =====================================================
        // REGISTRATION FORM
        // =====================================================
        H1 title = new H1("Sign Up");
        title.getStyle().set("color", "black").set("font-size", "36px").set("margin-top", "0");

        // אתחול השדות
        username = new TextField();
        username.setPlaceholder("username");
        applyFieldStyle(username);

        password = new PasswordField();
        password.setPlaceholder("password");
        applyFieldStyle(password);

        email = new EmailField();
        email.setPlaceholder("email");
        applyFieldStyle(email);

        phone = new TextField();
        phone.setPlaceholder("phone");
        applyFieldStyle(phone);

        age = new IntegerField();
        age.setPlaceholder("age");
        applyFieldStyle(age);

        // =====================================================
        // GENDER SELECTION
        // =====================================================
        HorizontalLayout genderLayout = new HorizontalLayout();
        genderLayout.setAlignItems(Alignment.CENTER);

        Span genderLabel = new Span("gender:");
        genderLabel.getStyle()
                .set("color", "white")
                .set("font-weight", "bold")
                .set("font-size", "18px")
                .set("margin-right", "10px");

        gender = new RadioButtonGroup<>();
        gender.setItems("Male", "Female");
        gender.getStyle().set("color", "white").set("font-weight", "bold");

        genderLayout.add(genderLabel, gender);

        // הגדרת הודעות שגיאה לכל שדה במידה והוא ריק
        String errorMsg = "חובה למלא שדה זה";
        username.setErrorMessage(errorMsg);
        password.setErrorMessage(errorMsg);
        email.setErrorMessage(errorMsg);
        phone.setErrorMessage(errorMsg);
        age.setErrorMessage(errorMsg);
        gender.setErrorMessage(errorMsg);

        // =====================================================
        // BOTTOM LEFT (GREEN BUTTON)
        // =====================================================
        Button nextButton = new Button("Next");
        nextButton.setWidth("140px");
        nextButton.setHeight("45px");
        nextButton.addClickListener(e -> validateAndSubmit());

        nextButton.getStyle()
                .set("background", "linear-gradient(90deg, #28a745, #218838)") // צבע ירוק
                .set("color", "white")
                .set("border-radius", "50px") // מעוגל לחלוטין
                .set("font-size", "18px")
                .set("font-weight", "bold")
                .set("border", "none")
                .set("cursor", "pointer") // כאשר העכבר עובר מעל הרכיב הסמן של העכבר יהפוך ליד
                .set("margin-top", "15px")
                .set("margin-right", "auto"); // עוזר למשוך את הכפתור לשמאל באופן מוחלט

        // =====================================================
        // ADD COMPONENTS TO BUBBLE AND TO MAIN VIEW
        // =====================================================
        bubble.add(title, username, password, email, phone, age, genderLayout, nextButton);

        // יישור ספציפי של הכפתור לצד שמאל של הבועה
        bubble.setHorizontalComponentAlignment(Alignment.START, nextButton);
        add(bubble);
    }

    /**
     * פונקציית עזר לעיצוב השדות כדי לחסוך שכפול קוד
     * 
     */
    private void applyFieldStyle(HasStyle field) {
        ((HasSize) field).setWidthFull();
        field.getStyle()
                .set("background", "rgba(255,255,255,0.7)")
                .set("border-radius", "15px")
                .set("box-shadow", "0 2px 5px rgba(0,0,0,0.1)");
    }

    /**
     * פונקציה שמופעלת בלחיצה על הכפתור הירוק ובודקת האם כל השדות מולאו
     */
    private void validateAndSubmit() {
        boolean isValid = true;

        // בדיקת שם משתמש
        if (username.isEmpty()) {
            username.setInvalid(true);
            isValid = false;
        }

        // בדיקת סיסמה
        if (password.isEmpty()) {
            password.setInvalid(true);
            isValid = false;
        }

        // --- בדיקת מייל משופרת ---
        if (email.isEmpty()) {
            email.setErrorMessage("חובה למלא שדה זה");
            email.setInvalid(true);
            isValid = false;
            // בודק אם יש @ וגם נקודה (פורמט מינימלי למייל)
        } else if (!email.getValue().contains("@") || !email.getValue().contains(".")) {
            email.setErrorMessage("כתובת המייל אינה חוקית");
            email.setInvalid(true);
            isValid = false;
        } else {
            email.setInvalid(false);
        }

        // בדיקת טלפון: חייב להכיל רק מספרים ועד 10 ספרות
        String phoneValue = phone.getValue();
        if (phone.isEmpty() || !phoneValue.matches("[0-9]{1,10}")) {
            phone.setInvalid(true);
            isValid = false;
        } else {
            phone.setInvalid(false);
        }

        // בדיקת גיל
        Integer ageValue = age.getValue();
        if (ageValue == null || ageValue < 18) {
            age.setErrorMessage("חובה למלא גיל תקין (18 ומעלה)");
            age.setInvalid(true);
            isValid = false;
        } else {
            age.setInvalid(false);
        }

        // בדיקת מגדר
        if (gender.isEmpty()) {
            gender.setInvalid(true);
            isValid = false;
        }

        if (isValid) {
            // הכל תקין - אפשר לעבור מסך
            User user = new User();
            user.setUsername(username.getValue());
            user.setPassword(PasswordHelper.encode(password.getValue()));//PasswordHelper.encode(password.getValue())
            user.setEmail(email.getValue());
            user.setPhone(phone.getValue());
            user.setAge(age.getValue());

            boolean registerAns = userService.registerNewUser(user);
            if (registerAns == true) {
                VaadinSession.getCurrent().setAttribute("user", user);
                // העברת השם משתמש והמייל בשביל המשך הרישום
                VaadinSession.getCurrent().setAttribute("email", user.getEmail());
                VaadinSession.getCurrent().setAttribute("username", user.getUsername());
                UI.getCurrent().navigate("/choose");
            } else {
                Notification.show("כתובת המייל תפוסה, נסה כתובת אחרת", 5000, Position.MIDDLE);
            }
        }
    }
}