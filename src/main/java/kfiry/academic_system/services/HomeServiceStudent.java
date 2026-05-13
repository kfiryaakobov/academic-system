package kfiry.academic_system.services;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import kfiry.academic_system.datamodels.Course;
import kfiry.academic_system.repositories.CourseRepository;
import kfiry.academic_system.repositories.UserRepository;
import kfiry.academic_system.datamodels.User;

@Service
public class HomeServiceStudent {
    private CourseRepository courseRepo;
    private UserRepository userRepo;

    public HomeServiceStudent(CourseRepository courseRepo, UserRepository userRepo) {
        this.courseRepo = courseRepo;
        this.userRepo = userRepo;
    }

    public List<Course> getCoursesByStudent(String email) {
        Optional<User> optionalUser = userRepo.findByEmail(email);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            return courseRepo.findAllByCourseIDIn(user.getCourseIds());
        }
        return List.of();
    }

    public int calcDuration(List<Course> courses) {
        int totalDuration = 0;
        for (Course course : courses) {
            int duration = course.getDuration();
            totalDuration = totalDuration + duration;
        }

        return totalDuration;
    }
}
