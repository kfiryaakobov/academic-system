package kfiry.academic_system.ui;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.combobox.MultiSelectComboBox;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;

import kfiry.academic_system.datamodels.Admin;
import kfiry.academic_system.datamodels.Course;
import kfiry.academic_system.datamodels.Lecturer;
import kfiry.academic_system.datamodels.TimeSlot;
import kfiry.academic_system.datamodels.User;
import kfiry.academic_system.services.AdminService;
import kfiry.academic_system.services.CourseService;
import kfiry.academic_system.services.LecturerService;
import kfiry.academic_system.services.UserService;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Route(value = "/admin/add-data", layout = AppLayoutAdmin.class)
public class AdminAddDataView extends VerticalLayout implements BeforeEnterObserver {

    private UserService userService;
    private CourseService courseService;
    private LecturerService lecturerService;
    private AdminService adminService;

    public AdminAddDataView(UserService userService, CourseService courseService, LecturerService lecturerService,
            AdminService adminService) {
        this.userService = userService;
        this.courseService = courseService;
        this.lecturerService = lecturerService;
        this.adminService = adminService;

        // הגדרות עיצוב למסך כולו (כמו במסך בחירת הקורסים)
        getElement().setAttribute("dir", "rtl");
        setSizeFull();
        setPadding(true);
        getStyle().set("background-color", "#f8f9fa");
        getStyle().set("overflow-y", "auto"); // מאפשר גלילה למטה אם המסך קטן מדי
        getStyle().set("box-sizing", "border-box");

        H2 mainHeader = new H2("הוספת נתונים למערכת");
        mainHeader.getStyle()
                .set("margin", "0 0 20px 0")
                .set("color", "#1a2a3a");
        setAlignSelf(Alignment.CENTER, mainHeader);

        // לוח הכרטיסיות (הטפסים ישבו פה אחד ליד השני)
        HorizontalLayout boardLayout = new HorizontalLayout();
        boardLayout.setWidthFull();
        boardLayout.setSpacing(true);
        boardLayout.setPadding(false);
        // flex-wrap מאפשר לכרטיסים לרדת שורה אם המסך של המנהל צר מדי
        boardLayout.getStyle().set("flex-wrap", "wrap");

        // יצירת והוספת 4 הכרטיסים עם צבעים שונים
        boardLayout.add(
                createStudentCard("#4A90E2"), // כחול
                createLecturerCard("#50C878"), // ירוק
                createCourseCard("#FFB347"), // כתום
                createAdminCard("#9B59B6") // סגול
        );

        add(mainHeader, boardLayout);
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        Admin admin = (Admin) VaadinSession.getCurrent().getAttribute("admin");
        if (admin == null) {
            // זורק למסך הלוגין אם אין משתמש מחובר
            event.forwardTo(LoginLecturerView.class);
        }
    }

    // --- כרטיס 1: סטודנט ---
    private VerticalLayout createStudentCard(String borderColor) {
        VerticalLayout card = createBaseCard("הוספת סטודנט", borderColor);

        TextField username = new TextField("שם משתמש");
        PasswordField password = new PasswordField("סיסמה");
        TextField email = new TextField("אימייל");
        TextField phone = new TextField("טלפון");
        IntegerField age = new IntegerField("גיל");

        MultiSelectComboBox<Course> coursesPicker = new MultiSelectComboBox<>("שיוך קורסים");
        coursesPicker.setItems(courseService.getAllCourses());
        coursesPicker.setItemLabelGenerator(Course::getName);

        // הגדרה שכל השדות יתפסו את כל רוחב הכרטיס
        username.setWidthFull();
        password.setWidthFull();
        email.setWidthFull();
        phone.setWidthFull();
        age.setWidthFull();
        coursesPicker.setWidthFull();

        Button saveBtn = new Button("שמור סטודנט", e -> {
            if (username.isEmpty() || password.isEmpty() || email.isEmpty() || phone.isEmpty() || age.isEmpty()) {
                Notification.show("שגיאה: נא למלא את כל השדות!");
                return;
            }
            if (password.getValue().length() < 6) {
                Notification.show("שגיאה: הסיסמה חייבת להכיל לפחות 6 תווים!");
                return;
            }
            String emailValue = email.getValue();
            if (!emailValue.contains("@") || !emailValue.contains(".")) {
                Notification.show("שגיאה: כתובת האימייל אינה תקינה!");
                return;
            }
            if (age.getValue() <= 18) {
                Notification.show("שגיאה: הגיל חייב להיות מעל 18!");
                return;
            }

            boolean emailExists = false;
            for (User u : userService.getAllUsers()) {
                if (u.getEmail() != null && u.getEmail().equals(emailValue)) {
                    emailExists = true;
                    break;
                }
            }
            if (emailExists) {
                Notification.show("שגיאה: הכתובת תפוסה על ידי משתמש אחר!");
                return;
            }

            try {
                List<String> selectedCourseIds = new ArrayList<>();
                for (Course course : coursesPicker.getValue()) {
                    selectedCourseIds.add(course.getCourseID());
                }

                User user = new User(username.getValue(), password.getValue(), emailValue, phone.getValue(),
                        age.getValue(), new ArrayList<>(selectedCourseIds));
                userService.insertUser(user);
                Notification.show("הסטודנט נשמר בהצלחה!");

                username.clear();
                password.clear();
                email.clear();
                phone.clear();
                age.clear();
                coursesPicker.clear();
            } catch (Exception ex) {
                Notification.show("שגיאה: " + ex.getMessage());
            }
        });
        saveBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        saveBtn.setWidthFull();

        card.add(username, password, email, phone, age, coursesPicker, saveBtn);
        return card;
    }

    // --- כרטיס 2: מרצה ---
    private VerticalLayout createLecturerCard(String borderColor) {
        VerticalLayout card = createBaseCard("הוספת מרצה", borderColor);

        TextField name = new TextField("שם המרצה");
        TextField id = new TextField("תעודת זהות");
        PasswordField password = new PasswordField("סיסמה");

        name.setWidthFull();
        id.setWidthFull();
        password.setWidthFull();

        // --- חלק הוספת זמני הוראה (TimeSlots) ---
        Set<TimeSlot> selectedSlots = new HashSet<>(); // כאן נשמור את הזמנים שהמנהל הוסיף
        VerticalLayout slotsDisplay = new VerticalLayout(); // להצגת הזמנים שנבחרו
        slotsDisplay.setPadding(false);
        slotsDisplay.setSpacing(false);

        ComboBox<DayOfWeek> dayPicker = new ComboBox<>("יום");
        dayPicker.setItems(DayOfWeek.values());
        dayPicker.setItemLabelGenerator(
                day -> day.getDisplayName(java.time.format.TextStyle.FULL, new java.util.Locale("he")));

        IntegerField startHour = new IntegerField("שעת התחלה");
        IntegerField endHour = new IntegerField("שעת סיום");

        Button addSlotBtn = new Button("הוסף זמן הוראה", e -> {
            if (dayPicker.getValue() == null || startHour.getValue() == null || endHour.getValue() == null) {
                Notification.show("נא למלא יום ושעות!");
                return;
            }
            TimeSlot newSlot = new TimeSlot(dayPicker.getValue(), startHour.getValue(), endHour.getValue());
            selectedSlots.add(newSlot);

            // הצגת הזמן שהתווסף על המסך
            slotsDisplay.add(new Span(
                    "• " + dayPicker.getValue().name() + ": " + startHour.getValue() + "-" + endHour.getValue()));

            // ניקוי השדות להוספה הבאה
            dayPicker.clear();
            startHour.clear();
            endHour.clear();
        });
        addSlotBtn.setWidthFull();

        // --- כפתור שמירה סופי ---
        Button saveBtn = new Button("שמור מרצה", e -> {
            // 1. בדיקת שדות ריקים
            if (name.isEmpty() || id.isEmpty() || password.isEmpty()) {
                Notification.show("יש למלא את כל השדות!");
                return;
            }

            // 2. בדיקת ת"ז - חייבת להיות 9 תווים בדיוק
            String idValue = id.getValue();
            if (idValue.length() != 9) {
                Notification.show("שגיאה: תעודת זהות חייבת להכיל 9 תווים בדיוק!");
                return;
            }

            // 3. בדיקת אורך סיסמה - 6 ומעלה
            if (password.getValue().length() < 6) {
                Notification.show("שגיאה: הסיסמה חייבת להכיל לפחות 6 תווים!");
                return;
            }

            // 4. בדיקה אם תעודת הזהות כבר קיימת במערכת
            boolean idExists = false;
            for (Lecturer l : lecturerService.getAllLectuurer()) {
                if (l.getID() != null && l.getID().equals(idValue)) {
                    idExists = true;
                    break;
                }
            }

            if (idExists) {
                Notification.show("שגיאה: מרצה עם תעודת זהות זו כבר קיים במערכת!");
                return;
            }

            try {
                // יצירת המרצה עם ה-Set של הזמנים שנאספו
                Lecturer lecturer = new Lecturer(name.getValue(), idValue, password.getValue(), selectedSlots);

                lecturerService.insertLectuurer(lecturer); // קריאה לסרוויס לשמירה

                Notification.show("המרצה נשמר בהצלחה!");

                // ניקוי כל השדות והרשימות
                name.clear();
                id.clear();
                password.clear();
                selectedSlots.clear();
                slotsDisplay.removeAll();

            } catch (Exception ex) {
                Notification.show("שגיאה: " + ex.getMessage());
            }
        });

        saveBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        saveBtn.setWidthFull();

        // הוספת כל הרכיבים לכרטיס
        card.add(name, id, password, new H4("זמני הוראה"), dayPicker, startHour, endHour, addSlotBtn, slotsDisplay,
                saveBtn);
        return card;
    }

    // --- כרטיס 3: קורס ---
    private VerticalLayout createCourseCard(String borderColor) {
        VerticalLayout card = createBaseCard("הוספת קורס", borderColor);

        TextField name = new TextField("שם הקורס");
        TextField courseID = new TextField("מזהה (ID)");
        IntegerField duration = new IntegerField("משך הקורס (סמסטר פתיחה)");
        Checkbox mandatory = new Checkbox("קורס חובה?");

        ComboBox<Lecturer> lecturerCombo = new ComboBox<>("מרצה");
        lecturerCombo.setItems(lecturerService.getAllLectuurer());
        lecturerCombo.setItemLabelGenerator(Lecturer::getName);

        // 2. הוספת אפשרות לבחירת קורסי קדימות
        MultiSelectComboBox<Course> prerequisitesPicker = new MultiSelectComboBox<>("קורסי קדימות");
        prerequisitesPicker.setItems(courseService.getAllCourses());
        prerequisitesPicker.setItemLabelGenerator(Course::getName);

        name.setWidthFull();
        courseID.setWidthFull();
        duration.setWidthFull();
        lecturerCombo.setWidthFull();
        prerequisitesPicker.setWidthFull();

        Button saveBtn = new Button("שמור קורס", e -> {
            // 1. בדיקה ששום נתון חובה לא ריק (שם, מזהה, משך, ומרצה)
            if (name.isEmpty() || courseID.isEmpty() || duration.getValue() == null
                    || lecturerCombo.getValue() == null) {
                Notification.show("שגיאה: יש למלא את כל שדות החובה!");
                return;
            }

            // 4. משך הקורס הוא מספר - IntegerField כבר מבטיח זאת, אך נוודא שהוא חיובי
            if (duration.getValue() <= 0) {
                Notification.show("שגיאה: משך הקורס חייב להיות מספר חיובי!");
                return;
            }

            String newID = courseID.getValue();

            // 3. בדיקה שמזהה הקורס (Course ID) לא קיים כבר אצל קורס אחר
            boolean idExists = false;
            for (Course c : courseService.getAllCourses()) {
                if (c.getCourseID() != null && c.getCourseID().equals(newID)) {
                    idExists = true;
                    break;
                }
            }

            if (idExists) {
                Notification.show("שגיאה: מזהה קורס זה כבר קיים במערכת!");
                return;
            }

            try {
                // שליפת רשימת ה-IDs של קורסי הקדימות שנבחרו בלולאה
                ArrayList<String> prereqIds = new ArrayList<>();
                for (Course selected : prerequisitesPicker.getValue()) {
                    prereqIds.add(selected.getCourseID());
                }

                // יצירת הקורס החדש ושמירתו
                Course course = new Course(
                        name.getValue(),
                        newID,
                        duration.getValue(),
                        lecturerCombo.getValue(),
                        mandatory.getValue(),
                        prereqIds);

                courseService.insertCourse(course); // וודא שקיימת מתודה כזו ב-Service שלך
                Notification.show("הקורס נשמר בהצלחה במאגר!");

                // ניקוי שדות הטופס לאחר הצלחה
                name.clear();
                courseID.clear();
                duration.clear();
                lecturerCombo.clear();
                mandatory.clear();
                prerequisitesPicker.clear();

            } catch (Exception ex) {
                Notification.show("שגיאה בשמירה: " + ex.getMessage());
            }
        });

        saveBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        saveBtn.setWidthFull();

        card.add(name, courseID, duration, mandatory, lecturerCombo, prerequisitesPicker, saveBtn);
        return card;
    }

    // --- כרטיס 4: מנהל ---
    private VerticalLayout createAdminCard(String borderColor) {
        VerticalLayout card = createBaseCard("הוספת מנהל", borderColor);

        TextField name = new TextField("שם המנהל");
        TextField id = new TextField("תעודת זהות / משתמש");
        PasswordField password = new PasswordField("סיסמה");

        name.setWidthFull();
        id.setWidthFull();
        password.setWidthFull();

        Button saveBtn = new Button("שמור מנהל", e -> {
            // 1. בדיקת שדות ריקים - סוגרים את הבלוק מיד אחרי ה-return!
            if (name.isEmpty() || id.isEmpty() || password.isEmpty()) {
                Notification.show("יש למלא את כל השדות!");
                return;
            } // <--- הסוגר הזה היה חסר והופיע בסוף בטעות

            // 2. בדיקת ת"ז - חייבת להיות 9 תווים בדיוק
            String idValueAdmin = id.getValue();
            if (idValueAdmin.length() != 9) {
                Notification.show("שגיאה: תעודת זהות חייבת להכיל 9 תווים בדיוק!");
                return;
            }

            // 3. בדיקת אורך סיסמה - 6 ומעלה
            if (password.getValue().length() < 6) {
                Notification.show("שגיאה: הסיסמה חייבת להכיל לפחות 6 תווים!");
                return;
            }

            // 4. בדיקה אם תעודת הזהות כבר קיימת במערכת
            boolean idExists = false;
            for (Admin a : adminService.getAllAdmins()) {
                if (a.getId() != null && a.getId().equals(idValueAdmin)) {
                    idExists = true;
                    break;
                }
            }

            // הוספתי את התנאי שעוצר את הפעולה אם המנהל כבר קיים
            if (idExists) {
                Notification.show("שגיאה: מנהל עם תעודת זהות זו כבר קיים במערכת!");
                return;
            }

            try {
                // שיניתי כאן שישתמש ב-idValueAdmin שכבר שלפנו
                Admin admin = new Admin(idValueAdmin, password.getValue(), name.getValue());
                adminService.insertAdmin(admin);
                Notification.show("המנהל נשמר בהצלחה!");
                name.clear();
                id.clear();
                password.clear();
            } catch (Exception ex) {
                Notification.show("שגיאה: " + ex.getMessage());
            }
        });

        saveBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        saveBtn.setWidthFull();

        card.add(name, id, password, saveBtn);
        return card;
    }

    // --- פונקציית עזר ליצירת הבסיס העיצובי של הכרטיס ---
    private VerticalLayout createBaseCard(String title, String borderColor) {
        VerticalLayout card = new VerticalLayout();
        // הגדרה שכל כרטיס יתפוס מינימום 300 פיקסלים ויתרחב בצורה שווה
        card.setMinWidth("300px");
        card.getStyle().set("flex", "1 1 0");

        card.getStyle()
                .set("background-color", "white")
                .set("border-radius", "12px")
                .set("box-shadow", "0 4px 8px rgba(0,0,0,0.05)")
                .set("border-top", "6px solid " + borderColor)
                .set("padding", "20px");

        H4 cardTitle = new H4(title);
        cardTitle.getStyle().set("margin", "0 0 15px 0");

        card.add(cardTitle);
        return card;
    }
}