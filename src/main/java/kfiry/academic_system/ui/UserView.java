package kfiry.academic_system.ui;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.Notification.Position;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;

import kfiry.academic_system.datamodels.User;
import kfiry.academic_system.services.UserService;

@Route("/")
public class UserView extends VerticalLayout {
    private UserService userService;
    private Button btnInsert;
    private TextField txfUn;
    private TextField txfPw;

    public UserView(UserService userService){
        this.userService = userService;
        add(new H1("UserView"));
        HorizontalLayout layout = new HorizontalLayout(Alignment.BASELINE);
        layout.add(txfUn = new TextField("username"));
        layout.add(txfPw = new TextField("password"));
        layout.add(btnInsert = new Button("insert user to DB"));
        btnInsert.addClickListener(clickEvent -> insertUserToDB());
        add(layout);

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
