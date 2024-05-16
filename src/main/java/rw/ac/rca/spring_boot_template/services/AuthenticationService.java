package rw.ac.rca.spring_boot_template.services;


import rw.ac.rca.spring_boot_template.dtos.requests.SignInDTO;
import rw.ac.rca.spring_boot_template.dtos.responses.ProfileResponseDTO;
import rw.ac.rca.spring_boot_template.exceptions.ResourceNotFoundException;
import rw.ac.rca.spring_boot_template.models.User;
import rw.ac.rca.spring_boot_template.dtos.requests.ResetPasswordDTO;
import rw.ac.rca.spring_boot_template.dtos.requests.LoginResponse;

public interface AuthenticationService {
    public LoginResponse login (SignInDTO dto);
    public boolean verifyAccount(String email, String code) throws ResourceNotFoundException;
    public boolean verifyResetCode(String email, String code) throws ResourceNotFoundException;
    public User resendVerificationCode(String email) throws ResourceNotFoundException;
    public User resetPassword(ResetPasswordDTO dto) throws ResourceNotFoundException;
    public User initiatePasswordReset(String email) throws ResourceNotFoundException;
    // other methods
    public ProfileResponseDTO getUserProfile() throws ResourceNotFoundException;

}
