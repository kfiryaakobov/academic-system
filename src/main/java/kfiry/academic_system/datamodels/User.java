package kfiry.academic_system.datamodels;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.mongodb.core.mapping.Document;

import org.springframework.data.annotation.Id;

@Document(collection = "Users")
public class User {

    @Id 
    private String email;
    private String username;
    private String password;
    private String phone;
    private int age;
    private ArrayList<String> courseIds = new ArrayList<>();

    public User(String username, String password,String email, String phone , int age, ArrayList<String> courseIds) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.phone = phone;
        this.age = age;
        this.courseIds = courseIds;
    }

    public User(String username, String password, ArrayList<String> courseIds) {
        this.username = username;
        this.password = password;
        this.courseIds = courseIds;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
    
    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public List<String> getCourseIds() {
        return courseIds;
    }

    public void setCourseIds(List<String> courseIds) {
        this.courseIds = new ArrayList<>(courseIds);
    }

    public void enrollCourse(String courseId) {
        if (!courseIds.contains(courseId)) {
            courseIds.add(courseId);
        }
    }

    public void removeCourse(String courseId) {
        courseIds.remove(courseId);
    }


    @Override
    public String toString() {
        return "User [username=" + username + ", password=" + password + "]";
    }

}
