package kfiry.academic_system.repositories;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import kfiry.academic_system.datamodels.Course;

@Repository
public interface CourseRepository extends MongoRepository<Course,String>{
    List<Course> findByCourseIDIn(List<String> ids);

    List<Course> findAllByCourseIDIn(List<String> ids);
}
