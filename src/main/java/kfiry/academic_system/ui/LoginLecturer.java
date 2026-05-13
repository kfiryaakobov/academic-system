package kfiry.academic_system.ui;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;

@Route("/loginLecturer")
public class LoginLecturer extends HorizontalLayout {

    private TextField username;
    private PasswordField password;

    public LoginLecturer() {

        setSizeFull();
        setSpacing(false);
        setPadding(false);

        // =====================================================
        // LEFT SIDE (IMAGE - 70%)
        // =====================================================
        // יצירת דיב עבור הצד השמאלי. בצד זה תופיע התמונה
        Div rightSide = new Div();
        rightSide.setWidth("70%");
        rightSide.setHeightFull();
        rightSide.getStyle()
                .set("background-image", "url('images/LoginLecturer.png')")// השם של התמונה
                .set("background-size", "cover")// שלא ישארו שטחים ריקים על המסך גם אם זה אומר לחתוך אותם
                .set("background-position", "center")// ממקם את התמונה במרכז של הדיב
                .set("background-repeat", "no-repeat");// מונע מהתמונה להשתכפל - יכול להיות שהתמונה קטנה מידיי ואז היא
                                                       // תוצג פעמיים

        // =====================================================
        // RIGHT SIDE (LOGIN - 30%)
        // =====================================================
        // יצירת דיב עבור הצד הימני. בצד זה יופיע מסך ההתחברות וההרשמה
        Div leftSide = new Div();
        leftSide.setWidth("30%");
        leftSide.setHeightFull();

        leftSide.getStyle()
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

        H1 title = new H1("Login for lecturers/admin");
        title.getStyle()
                .set("color", "black")
                .set("font-size", "42px");

        username = new TextField();
        username.setPlaceholder("Username");
        username.setWidthFull();
        username.getStyle()
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
        loginButton.addClickListener(clickEvent -> moveToHomePage());

        loginButton.getStyle()
                .set("height", "50px")
                .set("border-radius", "25px")// מעגל את הפינות של הכפתור
                .set("border", "none")// מוריד את המסגרת של הכפתור מסביב
                .set("color", "black")// מגדיר את צבע הטקסט כשחור
                .set("font-size", "18px")// גודל הטקסט
                .set("font-weight", "bold")// עובי הטקסט
                .set("background", "linear-gradient(90deg, #f3d61b, #a7ef2c)");// נותן רקע עם מעבר צבעים. מתחיל צהוב חזק
                                                                               // והולך ומתחזק

        form.add(title, username, password, loginButton);

        leftSide.add(form);

        // =====================================================
        // ADD BOTH SIDES
        // =====================================================
        add(leftSide,rightSide);
    }

    private void moveToHomePage() {
        //בדיקה אם קיים ואבטחה - הצפנה
        //לעבור לעמוד הבית
    }
}
