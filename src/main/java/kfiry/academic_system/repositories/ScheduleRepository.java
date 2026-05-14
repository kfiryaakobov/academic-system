package kfiry.academic_system.repositories;

import kfiry.academic_system.datamodels.ScheduleDocument;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ScheduleRepository extends MongoRepository<ScheduleDocument, String> {

    Optional<ScheduleDocument> findTopByOrderByIdDesc();
}