package rw.ac.rca.spring_boot_template.services;


import rw.ac.rca.spring_boot_template.dtos.requests.SignInDTO;
import rw.ac.rca.spring_boot_template.dtos.responses.ProfileResponseDTO;
import rw.ac.rca.spring_boot_template.exceptions.ResourceNotFoundException;
import rw.ac.rca.spring_boot_template.models.User;
import rw.ac.rca.spring_boot_template.dtos.requests.ResetPasswordDTO;
import rw.ac.rca.spring_boot_template.dtos.requests.LoginResponse;
import rw.ac.rca.spring_boot_template.utils.JWTAuthenticationResponse;

public interface AuthenticationService {
    public JWTAuthenticationResponse login (SignInDTO dto);
    public boolean verifyAccount(String email, String code) ;
    public boolean verifyResetCode(String email, String code) ;
    public User resendVerificationCode(String email);
    public User resetPassword(ResetPasswordDTO dto) ;
    public User initiatePasswordReset(String email) ;
    // other methods
    public ProfileResponseDTO getUserProfile() ;

}
