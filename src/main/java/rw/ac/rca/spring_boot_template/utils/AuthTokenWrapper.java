package rw.ac.rca.spring_boot_template.utils;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Component
public class AuthTokenWrapper {
    public String getAuthToken() {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (requestAttributes == null) {
            throw new RuntimeException("No current request found.");
        }

        HttpServletRequest request = requestAttributes.getRequest();

        String bearerToken = request.getHeader("Authorization");

        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            System.out.println(bearerToken
            );
            return bearerToken.substring(7); // 7 is the length of "Bearer "
        }

        throw new RuntimeException("Bearer token not found in the request header.");
        }

}
