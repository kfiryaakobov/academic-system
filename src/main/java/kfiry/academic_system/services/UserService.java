package kfiry.academic_system.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import kfiry.academic_system.datamodels.Course;
import kfiry.academic_system.datamodels.User;
import kfiry.academic_system.repositories.CourseRepository;
import kfiry.academic_system.repositories.UserRepository;

@Service
public class UserService {
    public UserRepository userRepo;
    private CourseRepository courseRepo;

    /**
     * 
     * @param userRepo Dependency Injection הזרקת קוד ביוזר ריפוזיטורי
     */
    public UserService(UserRepository userRepo, CourseRepository courseRepo) {
        this.userRepo = userRepo;
        this.courseRepo = courseRepo;
    }

    public void insertUser(User user) throws Exception {
        if (userRepo.existsById(user.getUsername()))
            throw new Exception("User allredy exixt!");

        userRepo.insert(user);
    }

    // R (Read/Retrive)
    public ArrayList<User> getAllUsers() {
        return (ArrayList<User>) userRepo.findAll();
    }

    public List<Course> getStudentCourses(User user) {
        return courseRepo.findAllById(user.getCourseIds());
    }
}
