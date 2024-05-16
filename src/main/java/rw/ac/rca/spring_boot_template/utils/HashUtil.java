package rw.ac.rca.spring_boot_template.utils;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class HashUtil {
    private static BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

    public static String hashPassword(String password){
        return bCryptPasswordEncoder.encode(password);
    }
    public static boolean isTheSame(String rawPassword , String savedPassword){
        return bCryptPasswordEncoder.matches(rawPassword , savedPassword);
    }
}
