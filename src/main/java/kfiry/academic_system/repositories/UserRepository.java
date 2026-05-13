package kfiry.academic_system.repositories;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import kfiry.academic_system.datamodels.User;

@Repository
public interface UserRepository extends MongoRepository<User,String>
{
    User findByUsername(String username);

    Optional<User> findByEmail(String mail);
}
