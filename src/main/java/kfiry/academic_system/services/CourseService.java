package kfiry.academic_system.services;

import java.util.ArrayList;

import org.springframework.stereotype.Service;

import kfiry.academic_system.datamodels.Course;
import kfiry.academic_system.repositories.CourseRepository;

@Service
public class CourseService {
    public CourseRepository courseRepo;

    public CourseService(CourseRepository courseRepo){
        this.courseRepo = courseRepo;
    }

    // R (Read/Retrive)
   public ArrayList<Course> getAllCourses()
   {
      return (ArrayList<Course>)courseRepo.findAll();
   }
}
