package kfiry.academic_system.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

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
        // בודקים לפי המייל (Id)
        if (userRepo.existsById(user.getEmail()))
            throw new Exception("User already exists!");

        userRepo.save(user); // save בטוח יותר מ-insert
    }

    // R (Read/Retrive)
    public ArrayList<User> getAllUsers() {
        return (ArrayList<User>) userRepo.findAll();
    }

    public List<Course> getStudentCourses(User user) {
        return courseRepo.findAllById(user.getCourseIds());
    }

    // הרשמה - הכנסת נתונים בסיסים
    public boolean registerNewUser(User user) {
        if (userRepo.findByEmail(user.getEmail()).isPresent()) {
            return false;
        }
        userRepo.save(user);
        return true;
    }

    // המשך הרשמה - הכנסת קורסים אל תוך המשתמש
    public User addCoursesForUser(String email, Set<Course> courses) {
        Optional<User> userOpt = userRepo.findById(email);
        if (userOpt.isPresent()) {
            User user = userOpt.get();

            // הפיכת הקורסים לרשימת ID
            List<String> ids = new ArrayList<>();
            for (Course course : courses) {
                ids.add(course.getCourseID());
            }
            user.setCourseIds(new ArrayList<>(ids));
            return userRepo.save(user);
        }
        return null;
    }

    // התחברות
    public User authenticate(String username, String password) throws Exception {
        User user = userRepo.findByUsername(username);
        if (user == null || !user.getPassword().equals(password)) {
            throw new Exception("שם משתמש או סיסמה שגויים");
        }
        return user;
    }

}
