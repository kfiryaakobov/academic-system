package kfiry.academic_system.datamodels;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "Users")
public class User {
    private String username;
    private String password;
    private ArrayList<String> courseIds = new ArrayList<>();
    private int semester;

    public User(String username, String password, ArrayList<String> courseIds, int semester) {
        this.username = username;
        this.password = password;
        this.courseIds = courseIds;
        this.semester = semester;
    }
    

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }


    public User() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<String> getCourseIds() {
        return courseIds;
    }

    public void setCourseIds(ArrayList<String> courseIds) {
        this.courseIds = courseIds;
    }

    public void enrollCourse(String courseId) {
        if (!courseIds.contains(courseId)) {
            courseIds.add(courseId);
        }
    }

    public void removeCourse(String courseId) {
        courseIds.remove(courseId);
    }

    public int getSemester() {
        return semester;
    }

    public void setSemester(int semester) {
        this.semester = semester;
    }

    @Override
    public String toString() {
        return "User [username=" + username + ", password=" + password + "]";
    }

}
