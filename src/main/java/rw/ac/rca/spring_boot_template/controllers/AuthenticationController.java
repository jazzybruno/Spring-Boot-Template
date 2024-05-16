package rw.ac.rca.spring_boot_template.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import rw.ac.rca.spring_boot_template.dtos.requests.ResetPasswordDTO;
import rw.ac.rca.spring_boot_template.dtos.requests.SignInDTO;
import rw.ac.rca.spring_boot_template.dtos.responses.ProfileResponseDTO;
import rw.ac.rca.spring_boot_template.models.User;
import rw.ac.rca.spring_boot_template.security.JwtTokenProvider;
import rw.ac.rca.spring_boot_template.security.UserPrincipal;
import rw.ac.rca.spring_boot_template.services.AuthenticationService;
import rw.ac.rca.spring_boot_template.services.serviceImpl.EmailService;
import rw.ac.rca.spring_boot_template.services.serviceImpl.UserServiceImpl;
import rw.ac.rca.spring_boot_template.utils.*;

@RestController
@CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.PATCH, RequestMethod.OPTIONS})
@RequestMapping (path = "/api/auth")
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationService authenticationService;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserServiceImpl userService;
    private final EmailService emailService;
//    @Value("${token.expirationTimeMillis}")
    private final long tokenExpirationTimeMillis = 3600000;
    private final AuthenticationProvider authenticationProvider;
    private static final String BASE_URL = "https://example.com/reset-password";
    @Autowired
    public AuthenticationController(AuthenticationService authenticationService, JwtTokenProvider jwtTokenProvider, AuthenticationManager authenticationManager , UserServiceImpl userService , EmailService emailService , AuthenticationProvider authenticationProvider) {
        this.authenticationService = authenticationService;
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
        this.userService = userService;
        this.emailService = emailService;
        this.authenticationProvider = authenticationProvider;
    }

    @PostMapping (path = "/login")
    public ResponseEntity<ApiResponse> login(@Valid @RequestBody SignInDTO signInDTO) {
        String jwt = null;
        UserPrincipal userPrincipal = null;
        User user = null;
        // Create a UsernamePasswordAuthenticationToken
        UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(signInDTO.getEmail(), signInDTO.getPassword());

// Set the authentication in the SecurityContext
        Authentication authentication = authenticationProvider.authenticate(authRequest);
        try {
            SecurityContextHolder.getContext().setAuthentication(authentication);
            jwt = jwtTokenProvider.generateToken(authentication);
            userPrincipal = UserUtils.getLoggedInUser();
            user = userService.getUserById(userPrincipal.getId());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.ok().body(new ApiResponse(
                true,
                "Successfully Logged in",
                new JWTAuthenticationResponse(jwt , user )
        ));
    }

    @PostMapping("/verify-account")
    public ResponseEntity<ApiResponse> verifyAccount(@RequestParam String email, @RequestParam String code) {
        try {
            // Call the verify account service method
            boolean verificationStatus = authenticationService.verifyAccount(email, code);

            // Return a response based on the verification status
            if (verificationStatus) {
                return new ResponseEntity<>(new ApiResponse(
                        true,
                        "Account verification successful",
                        null
                ), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(new ApiResponse(
                        false,
                        "Account verification failed",
                        null
                ), HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            // Handle exceptions and return an appropriate response
            return ExceptionUtils.handleControllerExceptions(e);
        }
    }

    @PostMapping("/verify-reset-code")
    public ResponseEntity<ApiResponse> verifyResetCode(@RequestParam String email, @RequestParam String code) {
        try {
            // Call the verify reset code service method
            boolean verificationStatus = authenticationService.verifyResetCode(email, code);

            // Return a response based on the verification status
            if (verificationStatus) {
                return new ResponseEntity<>(new ApiResponse(
                        true,
                        "Reset code verification successful",
                        null
                ), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(new ApiResponse(
                        false,
                        "Reset code verification failed",
                        null
                ), HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            // Handle exceptions and return an appropriate response
            return ExceptionUtils.handleControllerExceptions(e);
        }
    }

    // Add other methods for reset password, resend verification code, reset password, etc.
    // Example:
    @PostMapping("/resend-verification-code")
    public ResponseEntity<ApiResponse> resendVerificationCode(@RequestParam String email) {
        try {
            // Call the resend verification code service method
            User user = authenticationService.resendVerificationCode(email);

            // Return a response based on the user
            if (user != null) {
                return new ResponseEntity<>(new ApiResponse(
                        true,
                        "Verification code resent successfully",
                        null
                ), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(new ApiResponse(
                        false,
                        "Resend verification code failed",
                        null
                ), HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            // Handle exceptions and return an appropriate response
            return ExceptionUtils.handleControllerExceptions(e);
        }
    }

    @PostMapping("/initiate-reset-password")
    public ResponseEntity<ApiResponse> initiateResetPassword(@RequestParam String email) {
        try {
            // Call the initiate reset password service method
            User user = authenticationService.initiatePasswordReset(email);

            // Return a response based on the user
            if (user != null) {
                return new ResponseEntity<>(new ApiResponse(
                        true,
                        "Password reset initiated successfully",
                        null
                ), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(new ApiResponse(
                        false,
                        "Password reset initiation failed",
                        null
                ), HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            // Handle exceptions and return an appropriate response
            return ExceptionUtils.handleControllerExceptions(e);
        }
    }

    @PostMapping("/reset-password")
    public ResponseEntity<ApiResponse> resetPassword(@RequestBody ResetPasswordDTO resetPasswordDTO) {
        try {
            // Call the reset password service method
            User user = authenticationService.resetPassword(resetPasswordDTO);

            // Return a response based on the user
            if (user != null) {
                return new ResponseEntity<>(new ApiResponse(
                        true,
                        "Password reset successfully",
                        null
                ), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(new ApiResponse(
                        false,
                        "Password reset failed",
                        null
                ), HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            // Handle exceptions and return an appropriate response
            return ExceptionUtils.handleControllerExceptions(e);
        }
    }

    // Add other methods for reset password, resend verification code, reset password, etc.
    // Example:
    @GetMapping("/profile")
    public ResponseEntity<ApiResponse> getUserProfile() {
        try {
            // Call the get user profile service method
            ProfileResponseDTO profileResponseDTO = authenticationService.getUserProfile();

            // Return a response based on the user
            return new ResponseEntity<>(new ApiResponse(
                    true,
                    "User profile retrieved successfully",
                    profileResponseDTO
            ), HttpStatus.OK);
        } catch (Exception e) {
            // Handle exceptions and return an appropriate response
            return ExceptionUtils.handleControllerExceptions(e);
        }
    }

    public static String generatePasswordResetLink(String email, String token) {
        // Create the password reset link by appending the email and token to the base URL
        return BASE_URL + "?email=" + email + "&token=" + token;
    }
}
