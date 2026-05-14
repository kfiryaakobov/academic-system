package kfiry.academic_system.services;

import org.springframework.stereotype.Service;
import kfiry.academic_system.datamodels.Admin;
import kfiry.academic_system.repositories.AdminRepository;

@Service
public class AdminService {
    private AdminRepository adminRepo;

    public AdminService(AdminRepository adminRepo) {
        this.adminRepo = adminRepo;
    }

    public Admin authenticateAdmin(String id, String password) throws Exception {
        // שימוש בפונקציה המובנית של Spring ופתיחת ה-Optional
        Admin admin = adminRepo.findById(id).orElse(null);
        
        if (admin == null || !admin.getPassword().equals(password)) {
            throw new Exception("פרטי מנהל שגויים");
        }
        return admin;
    }
}