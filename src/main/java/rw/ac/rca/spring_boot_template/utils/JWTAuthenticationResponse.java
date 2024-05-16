package rw.ac.rca.spring_boot_template.utils;

import lombok.Data;
import rw.ac.rca.spring_boot_template.models.User;

@Data
public class JWTAuthenticationResponse {
    private String accessToken;
    private String tokenType = "Bearer";
    private User user;

    public JWTAuthenticationResponse(String accessToken , User user) {
        this.accessToken = accessToken;
        this.user = user;
    }
}
