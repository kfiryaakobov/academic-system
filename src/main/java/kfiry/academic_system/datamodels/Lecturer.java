package kfiry.academic_system.datamodels;

import java.util.Set;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "Lecturers")
public class Lecturer {

    private String name;
    private String ID;
    private String password;
    private Set<TimeSlot> availableSlots;

    public Lecturer(String name, String ID, String password, Set<TimeSlot> availableSlots) {
        this.name = name;
        this.ID = ID;
        this.password = password;
        this.availableSlots = availableSlots;
    }

    public Lecturer() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<TimeSlot> getAvailableSlots() {
        return availableSlots;
    }

    public void setAvailableSlots(Set<TimeSlot> availableSlots) {
        this.availableSlots = availableSlots;
    }

    // הוספת זמן פנוי
    public void addAvailableSlot(TimeSlot timeSlot) {
        availableSlots.add(timeSlot);
    }

    // בדיקה אם המרצה יכול ללמד בזמן הזה
    public boolean isAvailable(TimeSlot time) {
        for (TimeSlot slot : availableSlots) {
            if (slot.overlaps(time)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String toString() {
        return name;
    }
}