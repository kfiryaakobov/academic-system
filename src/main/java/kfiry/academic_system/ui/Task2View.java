package kfiry.academic_system.ui;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H3;

import kfiry.academic_system.datamodels.User;
import kfiry.academic_system.services.UserService;

@Route("/Task2")
public class Task2View extends VerticalLayout{
    private UserService userService;
    private Grid<User> usersGrid;

    public Task2View(UserService userService){

        //add(new H3("-- User Grid --"));
        //usersGrid = new Grid<>(User.class);
        //usersGrid.setItems(userService.getAllUsersByOrder());
        //usersGrid.getStyle().setBorder("1px solid gray");
        //usersGrid.setColumns("username", "password", "email", "phone", "age");
        //usersGrid.addColumn(user -> String.join(", ", user.getCourseIds())).setHeader("Courses").setFlexGrow(3);
        //add(usersGrid);
    }
}
