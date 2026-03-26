package kfiry.academic_system.datamodels;

import java.util.Set;

public class Lecturer {
    private String name;
    private String ID;
    private Set<TimeSlot> unavailableSlots;// = new HashSet<>(); -----> not null

    public Lecturer(String name, String ID, Set<TimeSlot> unavailableSlots) {
        this.name = name;
        this.ID = ID;
        this.unavailableSlots = unavailableSlots;// הבעיה נפתרת אם תסתכל למעלה - במידה ומכניסים לערך זה נאל , התוכנית
                                                 // תקרוס. אפשר להכניס תנאי אם אבל לברר עם אילן לפני
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

    public void setID(String iD) {
        ID = iD;
    }

    public Set<TimeSlot> getUnavailableSlots() {
        return unavailableSlots;
    }

    public void setUnavailableSlots(Set<TimeSlot> unavailableSlots) {
        this.unavailableSlots = unavailableSlots;
    }

    // add a timeSlot for a Set(unavailableSlots)
    public void addUnavailableSlot(TimeSlot timeSlot) {
        unavailableSlots.add(timeSlot);
    }

    // return if lecturer can teach at the given time
    public boolean isAvailable(TimeSlot time) {
        for (TimeSlot timeSlot : unavailableSlots) {
            if (timeSlot.overlaps(time)) {
                return false;
            }
        }
        return true;
    }

}

