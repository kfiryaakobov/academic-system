package kfiry.academic_system.ui;

import java.util.List;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.Notification.Position;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;

import kfiry.academic_system.datamodels.Course;
import kfiry.academic_system.datamodels.Lecturer;
import kfiry.academic_system.datamodels.User;
import kfiry.academic_system.services.CoreService;
import kfiry.academic_system.services.CourseService;
import kfiry.academic_system.services.LecturerService;
import kfiry.academic_system.services.MongoService;
import kfiry.academic_system.services.UserService;

@Route("/mongo")
public class MongoView extends VerticalLayout {
    private UserService userService;
    private CoreService coreService;
    private CourseService courseService;
    private LecturerService lecturerService;
    private Button btnInsert;
    private TextField txfUn;
    private TextField txfPw;
    private Grid<User> usersGrid;
    private Grid<Course> coursesGrid;
    private Grid<Lecturer> lecturersGrid;
    private Grid<String> scheduleGrid;

    public MongoView(UserService userService, CoreService coreService, MongoService mongoService,
         CourseService courseService, LecturerService lecturerService) {
        this.userService = userService;
        this.coreService = coreService;
        this.courseService = courseService;
        this.lecturerService = lecturerService;

        add(new H1("MongoView"));
        Button btn = new Button("save Data to DB",e -> mongoService.firstSetUp());
        add(btn);

        Button btnPrivateSchedule = new Button("Schedule for Student");
        btnPrivateSchedule.addClickListener(clickEvent -> privateSchedule());
        add(btnPrivateSchedule);

        add(new H3("-- User Grid --"));
        HorizontalLayout layout = new HorizontalLayout(Alignment.BASELINE);
        layout.add(txfUn = new TextField("username"));
        layout.add(txfPw = new TextField("password"));
        layout.add(btnInsert = new Button("insert user to DB"));
        btnInsert.addClickListener(clickEvent -> insertUserToDB());
        add(layout);

        usersGrid = new Grid<>(User.class);
        usersGrid.setItems(userService.getAllUsers());
        usersGrid.getStyle().setBorder("1px solid gray");
        usersGrid.setColumns("username", "password","email", "phone","age", "semester");
        usersGrid.addColumn(user -> String.join(", ", user.getCourseIds())).setHeader("Courses").setFlexGrow(3);
        add(usersGrid);
        
        add(new H3("-- Course Grid --"));
        coursesGrid = new Grid<>(Course.class);
        coursesGrid.setItems(courseService.getAllCourses());
        coursesGrid.getStyle().setBorder("1px solid gray");
        coursesGrid.setColumns("name", "courseID","duration","lecturer","mandatory");
        add(coursesGrid);

        add(new H3("-- Lecturer Grid --"));
        lecturersGrid = new Grid<>(Lecturer.class);
        lecturersGrid.setItems(lecturerService.getAllLectuurer());
        lecturersGrid.getStyle().setBorder("1px solid gray");
        lecturersGrid.setColumns("name","ID","password","unavailableSlots");
        add(lecturersGrid);

        scheduleGrid = new Grid<>();
        scheduleGrid.getStyle().setBorder("1px solid gray");
        scheduleGrid.addColumn(s -> s).setHeader("Schedule");
        add(scheduleGrid);

    }

    private void privateSchedule() {
        User user = userService.getAllUsers().get(0);
        List<String> scheduleOutput = coreService.runCoreAndReturnStrings(user);
        scheduleGrid.setItems(scheduleOutput);
    }

    private void insertUserToDB() {
        String username = txfUn.getValue();
        String password = txfPw.getValue();
        // validation check
        if (username == null || password == null || username.length() < 6)
            return;
        try {
            User user = new User(username, password);
            userService.insertUser(user);
            Notification.show("User inserted Ok!", 3000, Position.MIDDLE);
        } catch (Exception exp) {
            exp.printStackTrace();
            Notification.show("User NOT inserted!" + exp.getMessage(), 5000, Position.MIDDLE);
        }
    }
}
