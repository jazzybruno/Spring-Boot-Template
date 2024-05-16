package rw.ac.rca.spring_boot_template.utils;

import java.time.Duration;
import java.time.Instant;
import java.util.UUID;

public class ExpirationTokenUtils {
    public static String generateToken() {
        // Generate a UUID
        UUID uuid = UUID.randomUUID();

        // Get the current timestamp
        Instant creationTime = Instant.now();

        // Combine the UUID and timestamp
        return generateToken(uuid, creationTime);
    }

    public static String generateToken(UUID uuid, Instant creationTime) {
        // Convert the UUID to a string
        String uuidString = uuid.toString();

        // Convert the timestamp to a string
        String timestampString = creationTime.toString();

        // Combine the UUID and timestamp (or use any format you prefer)
        return uuidString + "_" + timestampString;
    }

    public static boolean isTokenValid(String token, int daysValidityThreshold) {
        // Extract UUID and timestamp from the token
        String[] parts = token.split("_");

        if (parts.length != 2) {
            // Invalid token format
            return false;
        }

        try {
            // Parse UUID and timestamp
            UUID uuid = UUID.fromString(parts[0]);
            Instant creationTime = Instant.parse(parts[1]);

            // Check if the difference between creation time and current time
            // is less than or equal to the specified number of days
            Instant currentTime = Instant.now();
            Duration difference = Duration.between(creationTime, currentTime);

            return difference.toDays() <= daysValidityThreshold;
        } catch (Exception e) {
            // Error parsing UUID or timestamp
            return false;
        }
    }

}

