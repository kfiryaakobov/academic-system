package kfiry.academic_system.datamodels;
import java.time.DayOfWeek;
import java.util.Objects;

import org.springframework.data.mongodb.core.mapping.Document;

// Shows me one time slot on the system board
// Course X takes place on day Y between time A and time B
@Document(collection = "TimeSlot")
public class TimeSlot {
    private DayOfWeek day; // enum - {Sunday, Monday, Tuesday, Wednesday, Thursday, Friday, Saturday}
    private int startHour;
    private int endHour;

    public TimeSlot(DayOfWeek day, int startHour, int endHour) {
        this.day = day;
        this.startHour = startHour;
        this.endHour = endHour;
    }

    public DayOfWeek getDay() {
        return day;
    }

    public void setDay(DayOfWeek day) {
        this.day = day;
    }

    public int getStartHour() {
        return startHour;
    }

    public void setStartHour(int startHour) {
        this.startHour = startHour;
    }

    public int getEndHour() {
        return endHour;
    }

    public void setEndHour(int endHour) {
        this.endHour = endHour;
    }

    // check if 2 TimeSlot overlap in same time
    public boolean overlaps(TimeSlot other) {
        if (other.getDay() != day)
            return false;
        else {
            if (startHour < other.endHour && endHour > other.startHour)
                return true;
        }
        return false;
    }

    // check if hour is between startHour & endHour
    public boolean contains(int hour) {
        if (startHour <= hour && endHour >= hour) {
            return true;
        }
        return false;
    }

    // check if Object o and this TimeSlot is on the same time slot
    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || !(o instanceof TimeSlot)) // o.getClass() != TimeSlot.class
            return false;
        TimeSlot timeSlot = (TimeSlot) o;
        if (timeSlot.day == this.day && timeSlot.startHour == this.startHour && timeSlot.endHour == this.endHour)
            return true;
        else
            return false;
    }

    //return hashCode for this TimeSlot
    @Override
    public int hashCode() {
        int hashCode = Objects.hash(day, startHour, endHour);
        return hashCode;
    }

}

