package kfiry.academic_system.services;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import kfiry.academic_system.repositories.AdminRepository;
import org.springframework.stereotype.Service;

import kfiry.academic_system.datamodels.Admin;
import kfiry.academic_system.datamodels.Course;
import kfiry.academic_system.datamodels.Lecturer;
import kfiry.academic_system.datamodels.TimeSlot;
import kfiry.academic_system.datamodels.User;
import kfiry.academic_system.repositories.CourseRepository;
import kfiry.academic_system.repositories.LecturerRepository;
import kfiry.academic_system.repositories.UserRepository;

@Service
public class MongoService {
    private final AdminRepository adminRepository;
    private CourseRepository courseRepository;
    private LecturerRepository lecturerRepository;
    private UserRepository userRepository;

    public MongoService(CourseRepository courseRepository, LecturerRepository lecturerRepository,
            UserRepository userRepository, AdminRepository adminRepository) {
        this.courseRepository = courseRepository;
        this.lecturerRepository = lecturerRepository;
        this.userRepository = userRepository;
        this.adminRepository = adminRepository;
    }

    public void firstSetUp() {

        // ========== TimeSlot ==========
        TimeSlot time1 = new TimeSlot(DayOfWeek.SUNDAY, 8, 18);
        TimeSlot time2 = new TimeSlot(DayOfWeek.MONDAY, 8, 18);
        TimeSlot time3 = new TimeSlot(DayOfWeek.TUESDAY, 8, 18);
        TimeSlot time4 = new TimeSlot(DayOfWeek.WEDNESDAY, 11, 18);
        TimeSlot time5 = new TimeSlot(DayOfWeek.THURSDAY, 8, 18);

        Set<TimeSlot> timesDavid = new HashSet<>();
        timesDavid.add(time1);
        timesDavid.add(time2);
        timesDavid.add(time5);

        Set<TimeSlot> timesYotam = new HashSet<>();
        timesYotam.add(time3);
        timesYotam.add(time4);

        // ========== Lecturer ==========
        Lecturer lecturer1 = new Lecturer("David Gerber", "232784029","123456", timesDavid);
        Lecturer lecturer2 = new Lecturer("Yotam Eli", "462829473","yotam222", timesYotam);

        lecturerRepository.insert(lecturer1);
        lecturerRepository.insert(lecturer2);

        // ========== Course & CourseGraph ==========

        Course c1 = new Course("Intro Computer Science", "C1CS1", 1, lecturer1, true);
        Course c2 = new Course("C++ Programming", "C1C++2", 1, lecturer2, true, new ArrayList<>(List.of("C1CS1")));
        Course c3 = new Course("Mathematics I", "C1MATH1", 1, lecturer1, true);
        Course c4 = new Course("Algorithms", "C1ALG4", 1, lecturer2, true, new ArrayList<>(List.of("C1C++2", "C1MATH1")));
        Course c5 = new Course("Data Structures", "C1DS5", 1, lecturer1, true, new ArrayList<>(List.of("C1C++2", "C1MATH1")));
        Course c6 = new Course("Databases", "C1DB6", 2, lecturer1, true, new ArrayList<>(List.of("C1C++2")));
        Course c7 = new Course("Software Engineering", "C1SE7", 3, lecturer2, true, new ArrayList<>(List.of("C1ALG4", "C1DS5")));
        Course c8 = new Course("Operating Systems", "C1OS8", 2, lecturer1, true, new ArrayList<>(List.of("C1DS5")));
        Course c9 = new Course("Computer Networks", "C1CN9", 2, lecturer1, true, new ArrayList<>(List.of("C1DS5", "C1DB6")));
        Course c10 = new Course("Preparation for the first semester", "C1LL10", 1, lecturer2, false);
        Course c11 = new Course("AI Basics", "C1AI11", 3, lecturer1, true, new ArrayList<>(List.of("C1C++2")));
        Course c12 = new Course("Machine Learning", "C1ML12", 2, lecturer2, true, new ArrayList<>(List.of("C1C++2")));
        Course c13 = new Course("Web Dev", "C1WEB13", 2, lecturer1, true, new ArrayList<>(List.of("C1MATH1")));
        Course c14 = new Course("Mobile Apps", "C1MOB14", 2, lecturer2, true, new ArrayList<>(List.of("C1MATH1")));
        Course c15 = new Course("Cybersecurity", "C1SEC15", 2, lecturer1, true, new ArrayList<>(List.of("C1DS5")));
        Course c16 = new Course("Cloud Computing", "C1CLOUD16", 3, lecturer2, true, new ArrayList<>(List.of("C1DS5")));
        Course c17 = new Course("Datamining", "C1DM17", 2, lecturer1, true, new ArrayList<>(List.of("C1DB6")));
        Course c18 = new Course("Networks II", "C1NET18", 3, lecturer2, true, new ArrayList<>(List.of("C1CN9")));
        Course c19 = new Course("Advanced DB", "C1ADB19", 3, lecturer1, true, new ArrayList<>(List.of("C1DB6")));
        Course c20 = new Course("Capstone Mini", "C1CAP20", 2, lecturer2, true, new ArrayList<>(List.of("C1SE7")));

        List<Course> coursesList = List.of(c1, c2, c3, c4, c5, c6, c7, c8, c9, c10, c11, c12, c13, c14, c15, c16, c17,
                c18, c19, c20);

        courseRepository.insert(coursesList);

        // ========== User ==========
        ArrayList<String> lst1 = new ArrayList<>(
                List.of("C1CS1", "C1C++2", "C1MATH1", "C1ALG4", "C1DS5", "C1LL10"));
        User user1 = new User("kfiry", "123445678", lst1);
        userRepository.insert(user1);

        ArrayList<String> lst2 = new ArrayList<>(
                List.of("C1ML12", "C1WEB13", "C1MOB14", "C1SEC15", "C1DM17", "C1CAP20"));
        User user2 = new User("omriy", "1234", lst2);
        userRepository.insert(user2);

        Admin admin = new Admin("216232470", "123", "kfiry");
        adminRepository.insert(admin);
    }

    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }

    public User getUser(String username) {
        return userRepository.findByUsername(username);
    }

    public List<Course> getCoursesByIds(List<String> ids) {
        return courseRepository.findByCourseIDIn(ids);
    }
}
