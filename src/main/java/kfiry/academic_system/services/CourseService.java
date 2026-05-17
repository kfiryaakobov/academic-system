package kfiry.academic_system.services;

import java.util.ArrayList;

import org.springframework.stereotype.Service;

import kfiry.academic_system.datamodels.Course;
import kfiry.academic_system.repositories.CourseRepository;

@Service
public class CourseService {
    public CourseRepository courseRepo;

    public CourseService(CourseRepository courseRepo) {
        this.courseRepo = courseRepo;
    }

    public void insertCourse(Course course) throws Exception {
        // בודקים לפי (Id)
        if (courseRepo.existsById(course.getCourseID()))
            throw new Exception("course already exists!");

        courseRepo.save(course);
    }

    // R (Read/Retrive)
    public ArrayList<Course> getAllCourses() {
        return (ArrayList<Course>) courseRepo.findAll();
    }
}
