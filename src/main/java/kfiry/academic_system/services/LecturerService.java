package kfiry.academic_system.services;

import java.util.ArrayList;

import org.springframework.stereotype.Service;

import kfiry.academic_system.datamodels.Lecturer;
import kfiry.academic_system.repositories.LecturerRepository;

@Service
public class LecturerService {
    public LecturerRepository lecturerRepo;

    public LecturerService(LecturerRepository lecturerRepo){
        this.lecturerRepo = lecturerRepo;
    }

    // R (Read/Retrive)
   public ArrayList<Lecturer> getAllLectuurer()
   {
      return (ArrayList<Lecturer>)lecturerRepo.findAll();
   }

   public Lecturer authenticateLecturer(String ID, String password) throws Exception {
        Lecturer lecturer = lecturerRepo.findByID(ID);
        if (lecturer == null || !lecturer.getPassword().equals(password)) {
            throw new Exception("שם משתמש או סיסמה שגויים");
        }
        return lecturer;
    } 
}
