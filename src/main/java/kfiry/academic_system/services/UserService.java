package kfiry.academic_system.services;

import org.springframework.stereotype.Service;

import kfiry.academic_system.datamodels.User;
import kfiry.academic_system.repositories.UserRepository;

@Service
public class UserService {
    public UserRepository userRepo;

    /**
     * 
     * @param userRepo Dependency Injection הזרקת קוד ביוזר ריפוזיטורי 
     */
    public UserService(UserRepository userRepo){
        this.userRepo = userRepo;
    }

    public void insertUser(User user) throws Exception
    {
        if(userRepo.existsById(user.getUsername()))
            throw new Exception("User allredy exixt!");
        
        userRepo.insert(user);
    }
}
