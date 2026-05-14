package kfiry.academic_system.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import kfiry.academic_system.datamodels.Course;
import kfiry.academic_system.datamodels.Lecturer;
import kfiry.academic_system.repositories.CourseRepository;

@Service
public class HomeLecturerService {
    private CourseRepository courseRepo;

    public HomeLecturerService(CourseRepository courseRepo) {
        this.courseRepo = courseRepo;
    }

    public List<Course> getCoursesByLecturer(Lecturer lecturer) {
        // שולף את כל הקורסים במערכת ומסנן רק את אלו שמשויכים למרצה המחובר
        List<Course> allCourses = courseRepo.findAll();
        
        return allCourses.stream()
                .filter(course -> course.getLecturer() != null && 
                                  course.getLecturer().getID().equals(lecturer.getID()))
                .collect(Collectors.toList());
    }

    public int calcDuration(List<Course> courses) {
        int totalDuration = 0;
        for (Course course : courses) {
            totalDuration += course.getDuration();
        }
        return totalDuration;
    }
}