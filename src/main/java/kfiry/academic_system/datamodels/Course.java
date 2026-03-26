package kfiry.academic_system.datamodels;

import java.util.ArrayList;

public class Course {
    private String name;
    private String courseID;
    private int duration;
    private Lecturer lecturer;
    private boolean mandatory;
    private ArrayList<Course> prerequisites = new ArrayList<>();

    public Course(String name, String courseID, int duration, Lecturer lecturer, boolean mandatory) {
        this.name = name;
        this.courseID = courseID;
        this.duration = duration;
        this.lecturer = lecturer;
        this.mandatory = mandatory;
    }

    public Course(String name, String courseID, int duration, Lecturer lecturer, boolean mandatory, ArrayList<Course> prerequisites) {
        this.name = name;
        this.courseID = courseID;
        this.duration = duration;
        this.lecturer = lecturer;
        this.mandatory = mandatory;
        this.prerequisites = prerequisites;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCourseID() {
        return courseID;
    }

    public void setCourseID(String courseID) {
        this.courseID = courseID;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public Lecturer getLecturer() {
        return lecturer;
    }

    public void setLecturer(Lecturer lecturer) {
        this.lecturer = lecturer;
    }

    public boolean isMandatory() {
        return mandatory;
    }

    public void setMandatory(boolean mandatory) {
        this.mandatory = mandatory;
    }

    public ArrayList<Course> getPrerequisites() {
        return prerequisites;
    }

    public void setPrerequisites(ArrayList<Course> prerequisites) {
        this.prerequisites = prerequisites;
    }

    // add a course for a arrayList(prerequisites)
    public void addPrerequisite(Course course) {
        if (!prerequisites.contains(course))
            prerequisites.add(course);
    }

}
