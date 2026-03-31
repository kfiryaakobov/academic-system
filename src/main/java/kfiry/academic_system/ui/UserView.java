package kfiry.academic_system.ui;

import java.util.List;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.Notification.Position;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;

import kfiry.academic_system.datamodels.User;
import kfiry.academic_system.services.CoreService;
import kfiry.academic_system.services.UserService;

@Route("/")
public class UserView extends VerticalLayout {
    private UserService userService;
    private CoreService coreService;
    private Button btnInsert;
    private TextField txfUn;
    private TextField txfPw;
    private Grid<User> usersGrid;
    private Grid<String> scheduleGrid;

    public UserView(UserService userService, CoreService coreService) {
        this.userService = userService;
        this.coreService = coreService;
        add(new H1("UserView"));
        HorizontalLayout layout = new HorizontalLayout(Alignment.BASELINE);
        layout.add(txfUn = new TextField("username"));
        layout.add(txfPw = new TextField("password"));
        layout.add(btnInsert = new Button("insert user to DB"));
        btnInsert.addClickListener(clickEvent -> insertUserToDB());
        add(layout);

        usersGrid = new Grid<>(User.class);
        usersGrid.setItems(userService.getAllUsers());
        usersGrid.getStyle().setBorder("1px solid gray");
        usersGrid.setColumns("username", "password");
        add(usersGrid);

        Button btnPrivateSchedule = new Button("Schedule for Student");
        btnPrivateSchedule.addClickListener(clickEvent -> privateSchedule());
        add(btnPrivateSchedule);

        scheduleGrid = new Grid<>();
        scheduleGrid.getStyle().setBorder("1px solid gray");
        add(scheduleGrid);

    }

    private void privateSchedule() {
        List<String> scheduleOutput = coreService.runCoreAndReturnStrings();
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
