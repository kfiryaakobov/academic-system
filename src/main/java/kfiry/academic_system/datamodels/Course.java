package kfiry.academic_system.datamodels;

import java.util.ArrayList;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "Courses")
public class Course {
    private String name;
    private String courseID;
    private int duration;
    private Lecturer lecturer;
    private boolean mandatory;
    private ArrayList<String> prerequisites = new ArrayList<>();

    public Course(String name, String courseID, int duration, Lecturer lecturer, boolean mandatory) {
        this.name = name;
        this.courseID = courseID;
        this.duration = duration;
        this.lecturer = lecturer;
        this.mandatory = mandatory;
    }

    public Course(String name, String courseID, int duration, Lecturer lecturer, boolean mandatory,
            ArrayList<String> prerequisites) {
        this.name = name;
        this.courseID = courseID;
        this.duration = duration;
        this.lecturer = lecturer;
        this.mandatory = mandatory;
        this.prerequisites = prerequisites;
    }

    public Course() {
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

    public ArrayList<String> getPrerequisites() {
        return prerequisites;
    }

    public void setPrerequisites(ArrayList<String> prerequisites) {
        this.prerequisites = prerequisites;
    }

    // add a course for a arrayList(prerequisites)
    public void addPrerequisite(String courseId) {
        if (!prerequisites.contains(courseId)) {
            prerequisites.add(courseId);
        }
    }

}
