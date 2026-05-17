package kfiry.academic_system.services;

import java.util.ArrayList;
import java.util.List;

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
        List<Course> allCourses = courseRepo.findAll();
        List<Course> lecturerCourses = new ArrayList<>();

        for (Course course : allCourses) {
            if (course.getLecturer() != null) {
                boolean sameLecturer = course.getLecturer().getID().equals(lecturer.getID());
                if (sameLecturer) {
                    lecturerCourses.add(course);
                }
            }
        }
        return lecturerCourses;
    }

    public int calcDuration(List<Course> courses) {
        int totalDuration = 0;
        for (Course course : courses) {
            totalDuration += course.getDuration();
        }
        return totalDuration;
    }
}