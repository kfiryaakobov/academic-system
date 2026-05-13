package kfiry.academic_system.ui;

import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

@Route("/homeLecturer")
public class HomeLecturerView extends VerticalLayout{
    
    public HomeLecturerView(){
        add(new H3("home"));
    }
}
