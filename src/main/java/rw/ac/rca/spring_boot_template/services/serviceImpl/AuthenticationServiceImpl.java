package rw.ac.rca.spring_boot_template.services.serviceImpl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rw.ac.rca.spring_boot_template.dtos.requests.LoginResponse;
import rw.ac.rca.spring_boot_template.dtos.requests.ResetPasswordDTO;
import rw.ac.rca.spring_boot_template.dtos.requests.SignInDTO;
import rw.ac.rca.spring_boot_template.dtos.responses.ProfileResponseDTO;
import rw.ac.rca.spring_boot_template.enumerations.EStatus;
import rw.ac.rca.spring_boot_template.exceptions.BadRequestAlertException;
import rw.ac.rca.spring_boot_template.exceptions.CustomException;
import rw.ac.rca.spring_boot_template.exceptions.ResourceNotFoundException;
import rw.ac.rca.spring_boot_template.models.User;
import rw.ac.rca.spring_boot_template.repositories.IUserRepository;
import rw.ac.rca.spring_boot_template.security.JwtTokenProvider;
import rw.ac.rca.spring_boot_template.security.UserPrincipal;
import rw.ac.rca.spring_boot_template.services.AuthenticationService;
import rw.ac.rca.spring_boot_template.services.MailService;
import rw.ac.rca.spring_boot_template.services.UserService;
import rw.ac.rca.spring_boot_template.utils.*;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {
    private final UserService userService;
    private final IUserRepository userRepository;
    private final MailService mailService;
    private final AuthenticationProvider authenticationProvider;
    private final JwtTokenProvider jwtTokenProvider;


    @Override
    public JWTAuthenticationResponse login(SignInDTO signInDTO) {
        try {
        String jwt = null;
        UserPrincipal userPrincipal = null;
        User user = null;
        // Create a UsernamePasswordAuthenticationToken
        UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(signInDTO.getEmail(), signInDTO.getPassword());

       // Set the authentication in the SecurityContext
            Authentication authentication = authenticationProvider.authenticate(authRequest);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            jwt = jwtTokenProvider.generateToken(authentication);
            userPrincipal = UserUtils.getLoggedInUser();
            user = userService.getUserById(userPrincipal.getId());
            return  new JWTAuthenticationResponse(jwt , user );
        } catch (Exception e) {
            throw new CustomException(e);
        }
    }

    @Override
    @Transactional
    public boolean verifyAccount(String email, String code) {
        try {
            User user = userRepository.findUserByEmail(email).orElseThrow(() -> new BadRequestAlertException("User with provided email not found"));
            if(user.getActivationCode().equals(code)){
                user.setStatus(EStatus.ACTIVE);
                userRepository.save(user);
                mailService.sendWelcomeEmail(user);
                return true;
            }else{
                throw new BadRequestAlertException("Incorrect verification code");
            }
        }catch (Exception e){
            throw new CustomException(e);
        }
    }

    @Override
    public boolean verifyResetCode(String email, String code) {
        try {
            User user = userRepository.findUserByEmail(email).orElseThrow(() -> new BadRequestAlertException("User with provided email not found"));
            if(user.getActivationCode().equals(code)){
                user.setStatus(EStatus.ACTIVE);
                userRepository.save(user);
                return true;
            }else{
                throw new BadRequestAlertException("Incorrect verification code");
            }
        }catch (Exception e){
            throw new CustomException(e);
        }
    }

    @Override
    public User resendVerificationCode(String email) {
        try {
            User user = userRepository.findUserByEmail(email).orElseThrow(() -> new BadRequestAlertException("User with provided email not found"));
            mailService.sendAccountVerificationEmail(user);
            return user;
        }catch (Exception e){
            throw new CustomException(e);
        }
    }

    @Override
    @Transactional
    public User resetPassword(ResetPasswordDTO dto) {
        try {
            User user = userRepository.findUserByEmail(dto.getEmail()).orElseThrow(() -> new BadRequestAlertException("User with provided email not found"));
            user.setPassword(HashUtil.hashPassword(dto.getNewPassword()));
            return user;
        }catch (Exception e){
            throw new CustomException(e);
        }
    }

    @Override
    @Transactional
    public User initiatePasswordReset(String email)  {
        try {
            User user = userRepository.findUserByEmail(email).orElseThrow(() -> new BadRequestAlertException("User with provided email not found"));
            user.setActivationCode(Utility.randomUUID(6, 0, 'N'));
            mailService.sendResetPasswordMail(user);
            return user;
        }catch (Exception e){
            throw new CustomException(e);
        }
    }

    @Override
    public ProfileResponseDTO getUserProfile()  {
        try {
            User user = this.userService.getLoggedInUser();
                return new ProfileResponseDTO(user , user.getRoles());
        }catch (Exception e){
            throw new CustomException(e);
        }
    }
}
