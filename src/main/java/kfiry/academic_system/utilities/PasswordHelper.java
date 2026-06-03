package kfiry.academic_system.utilities;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

public class PasswordHelper {

    public static String encode(String pw){
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodedPw = passwordEncoder.encode(pw);
        return encodedPw;
    }

    public static boolean match(String pwLogin, String pwDB){
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        return passwordEncoder.matches(pwLogin, pwDB);
    }
}
