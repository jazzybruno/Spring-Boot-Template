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
import rw.ac.rca.spring_boot_template.exceptions.ResourceNotFoundException;
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

    @PostMapping (path = "/login")
    public ResponseEntity<ApiResponse<JWTAuthenticationResponse>> login(@Valid @RequestBody SignInDTO signInDTO) {
        JWTAuthenticationResponse jwtAuthenticationResponse = authenticationService.login(signInDTO);
        return ApiResponse.success(jwtAuthenticationResponse).toResponseEntity();
    }

    @PostMapping("/verify-account")
    public ResponseEntity<ApiResponse<Boolean>> verifyAccount(@RequestParam String email, @RequestParam String code) {
        boolean isVerified = authenticationService.verifyAccount(email , code);
        return ApiResponse.success(isVerified).toResponseEntity();
    }

    @PostMapping("/verify-reset-code")
    public ResponseEntity<ApiResponse<Boolean>> verifyResetCode(@RequestParam String email, @RequestParam String code) {
           boolean isVerified = authenticationService.verifyResetCode(email , code);
           return ApiResponse.success(isVerified).toResponseEntity();
    }

    @PostMapping("/resend-verification-code")
    public ResponseEntity<ApiResponse<User>> resendVerificationCode(@RequestParam String email) {
        User user = authenticationService.resendVerificationCode(email);
        return ApiResponse.success(user).toResponseEntity();
    }

    @PostMapping("/initiate-reset-password")
    public ResponseEntity<ApiResponse<User>> initiateResetPassword(@RequestParam String email) {
       User user = authenticationService.initiatePasswordReset(email);
       return ApiResponse.success(user).toResponseEntity();
    }

    @PostMapping("/reset-password")
    public ResponseEntity<ApiResponse<User>> resetPassword(@RequestBody ResetPasswordDTO resetPasswordDTO) {
        User user = authenticationService.resetPassword(resetPasswordDTO);
        return ApiResponse.success(user).toResponseEntity();
    }

    @GetMapping("/profile")
    public ResponseEntity<ApiResponse<ProfileResponseDTO>> getUserProfile() {
        ProfileResponseDTO profileResponseDTO = authenticationService.getUserProfile();
        return ApiResponse.success(profileResponseDTO).toResponseEntity();
    }

}
