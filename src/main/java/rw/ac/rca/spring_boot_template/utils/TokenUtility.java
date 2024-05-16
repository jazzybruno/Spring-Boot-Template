package rw.ac.rca.spring_boot_template.utils;

import java.util.UUID;

public class TokenUtility {

    public static String generateToken() {
        // Generate a unique token using UUID
        UUID uuid = UUID.randomUUID();
        return uuid.toString();
    }

    public static boolean isTokenValid(String token , long tokenExpirationTimeMillis) {
        try {
            UUID uuid = UUID.fromString(token);
            // Add logic to check if the token is still valid
            long currentTimeMillis = System.currentTimeMillis();
            return (currentTimeMillis - uuid.timestamp()) < tokenExpirationTimeMillis;
        } catch (IllegalArgumentException e) {
            // The token is not a valid UUID
            return false;
        }
    }
}
