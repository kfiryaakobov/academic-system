package kfiry.academic_system.services;

import kfiry.academic_system.datamodels.Course;
import kfiry.academic_system.repositories.CourseRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HomeAdminService {
    private final CourseRepository courseRepo;

    public HomeAdminService(CourseRepository courseRepo) {
        this.courseRepo = courseRepo;
    }

    public List<Course> getAllCourses() {
        return courseRepo.findAll();
    }

    public Course getCourseById(String id) {
        // שימוש ב-findById המובנה ופתיחת ה-Optional
        return courseRepo.findById(id).orElse(null);
    }

    public int getTotalCourseCount() {
        // פונקציה יעילה של Spring Data שמחזירה רק את הכמות בלי לשלוף את כל האובייקטים
        return (int) courseRepo.count();
    }
}