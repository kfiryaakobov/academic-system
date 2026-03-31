package kfiry.academic_system.utilities;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kfiry.academic_system.datamodels.Course;
import kfiry.academic_system.datamodels.Lecturer;
import kfiry.academic_system.datamodels.TimeSlot;

public class SchedulerHelper {
    private Map<Course, TimeSlot> assignments = new HashMap<>();
    private List<Course> topologicCourses; // קורסים ממוינים טופולוגית

    public void setTopologicCourses(List<Course> topologicCourses) {
        this.topologicCourses = topologicCourses;
    }

    // check if course can be assigned to this timeSlot
    public boolean canAssign(Course course, TimeSlot timeSlot) {
        Lecturer lecturer = course.getLecturer();

        for (TimeSlot unavailable : lecturer.getUnavailableSlots()) {
            if (unavailable.overlaps(timeSlot)) {
                return false;
            }
        }

        for (TimeSlot assigned : assignments.values()) {
            if (assigned.overlaps(timeSlot)) {
                return false;
            }
        }

        if (assignments.containsKey(course)) {
            return false;
        }
        return true;
    }

    public void assign(Course course, TimeSlot timeSlot) {
        assignments.put(course, timeSlot);
    }

    public void unassign(Course course) {
        assignments.remove(course);
    }

    public boolean isAssigned(Course spesipicalCourse) {
        for (Course course : assignments.keySet()) {
            if (course.equals(spesipicalCourse))
                return true;
        }
        return false;
    }

    public Course getNextCourse(int index) {
        return topologicCourses.get(index);
    }

    public List<TimeSlot> getPossibleTimeSlots(Course course) {
        List<TimeSlot> possible = new ArrayList<>();
        int courseLength = course.getDuration();
        for (DayOfWeek day : DayOfWeek.values()) {
            if (day == DayOfWeek.FRIDAY || day == DayOfWeek.SATURDAY)
                continue;

            for (int startHour = 8; startHour <= 18 - courseLength; startHour++) { // כלומר אתה מתחיל בשעה 8 את הלימודים
                                                                                   // ויכול לסיים על 18
                TimeSlot slot = new TimeSlot(day, startHour, startHour + courseLength);
                possible.add(slot);
            }
        }
        return possible;
    }

    public boolean isComplete() {
        if (assignments.size() == topologicCourses.size()) {
            return true;
        }
        return false;
    }

    public TimeSlot getAssignment(Course course) {
        return assignments.get(course);
    }


    public void printAssignments() {
        System.out.println("--- FINAL SCHEDULE ---");
        for (Map.Entry<Course, TimeSlot> entry : assignments.entrySet()) {
            Course c = entry.getKey();
            TimeSlot t = entry.getValue();

            System.out.println(
                    c.getName() + " | " +
                            t.getDay() + " " +
                            t.getStartHour() + ":00-" +
                            t.getEndHour() + ":00");
        }
    }

}
