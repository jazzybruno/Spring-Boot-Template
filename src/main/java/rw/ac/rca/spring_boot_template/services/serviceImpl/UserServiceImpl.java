package rw.ac.rca.spring_boot_template.services.serviceImpl;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rw.ac.rca.spring_boot_template.dtos.requests.*;
import rw.ac.rca.spring_boot_template.enumerations.EStatus;
import rw.ac.rca.spring_boot_template.enumerations.EUserRole;
import rw.ac.rca.spring_boot_template.exceptions.*;
import rw.ac.rca.spring_boot_template.models.Role;
import rw.ac.rca.spring_boot_template.models.User;
import rw.ac.rca.spring_boot_template.repositories.IUserRepository;
import rw.ac.rca.spring_boot_template.security.UserPrincipal;
import rw.ac.rca.spring_boot_template.services.MailService;
import rw.ac.rca.spring_boot_template.services.UserService;
import rw.ac.rca.spring_boot_template.utils.ApiResponse;
import rw.ac.rca.spring_boot_template.utils.ExpirationTokenUtils;
import rw.ac.rca.spring_boot_template.utils.HashUtil;
import rw.ac.rca.spring_boot_template.utils.Utility;

import java.util.*;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private IUserRepository userRepository;
    private RoleServiceImpl roleService;
    @Value("${adminKey}")
    private String adminKey;

    @Value("${invitation.valid.days}")
    private int validityDays;

    private final MailService mailService;

    @Override
    public List<User> getAllUsers() {
        try {
            return userRepository.findAll();
        }catch (Exception e){
            throw new CustomException(e);
        }
    }

    @Override
    public User getUserById(UUID uuid) {
       try {
           return userRepository.findById(uuid).orElseThrow(() -> {throw new NotFoundException("The Resource was not found");
           });
       }catch (Exception e){
           throw new CustomException(e);
       }
    }

    @Override
    public User getUserByEmail(String email) {
       try {
           return userRepository.findUserByEmail(email).orElseThrow(() -> {throw new NotFoundException("The Resource was not found");
           });
       }catch (Exception e){
           throw new CustomException(e);
       }
    }

    @Override
    @Transactional
    public User createUser(CreateUserDTO createUserDTO) {
        try {
        User user = userRepository.findUserByEmail(createUserDTO.getEmail()).orElseThrow(()-> new NotFoundException("The user with the given email was not found"));
        if(user.getStatus().equals(EStatus.NO_PROFILE)){
            user.setFirstName(createUserDTO.getFirstName());
            user.setLastName(createUserDTO.getLastName());
            user.setNationalId(createUserDTO.getNationalId());
            user.setDateOfBirth(createUserDTO.getDateOfBirth());
            user.setGender(createUserDTO.getGender());
            user.setPhoneNumber(createUserDTO.getPhoneNumber());
            user.setStatus(EStatus.ACTIVE);
            user.setEmail(createUserDTO.getEmail());
//            user.setUsername(createUserDTO.getUsername());
            user.setPassword(HashUtil.hashPassword(createUserDTO.getPassword()));
            return user;
        }else{
            throw new BadRequestAlertException("Invalid invitation please first validated the invitation");
        }
        }catch (Exception e){
            throw new CustomException(e);
        }
    }

    @Override
    public User createAdmin(CreateAdminDTO createAdminDTO) {
       try {
           Optional<User> optionalUser = userRepository.findUserByEmailOrPhoneNumber(createAdminDTO.getEmail() , createAdminDTO.getPhoneNumber());
           if(optionalUser.isEmpty()){
               if(createAdminDTO.getRegistrationCode().equals(adminKey)){
                   try {
                       String activationCode = Utility.randomUUID(6 , 0 , 'N');
                       EStatus status =  EStatus.WAIT_EMAIL_VERIFICATION;
                       Role role = roleService.getRoleByName(EUserRole.ADMIN);
                       createAdminDTO.setPassword(HashUtil.hashPassword(createAdminDTO.getPassword()));
                       User user = new User(
                               createAdminDTO.getUsername(),
                               createAdminDTO.getPhoneNumber(),
                               createAdminDTO.getEmail(),
                               createAdminDTO.getGender(),
                               createAdminDTO.getPassword(),
                               status,
                               false,
                               activationCode
                       );
                       user.setLastName(createAdminDTO.getLastName());
                       user.setFirstName(createAdminDTO.getFirstName());
                       user.setNationalId(createAdminDTO.getNationalId());
                       user.setDateOfBirth(createAdminDTO.getDateOfBirth());
                       Set<Role> roles = new HashSet<Role>();
                       roles.add(role);
                       user.setRoles(roles);
                       System.out.println("The codes have reached here");
                       mailService.sendAccountVerificationEmail(user);
                       System.out.println("The codes have reached here after sending mail");
                       userRepository.save(user);
                       System.out.println("The codes that save the user ");
                       return user;
                   }catch (Exception e){
                       e.printStackTrace();
                       throw new  InternalServerErrorException(e.getMessage());
                   }
               }else{
                   throw new BadRequestAlertException("Unauthorized to perform this action");
               }
           }else{
               throw new BadRequestAlertException("The User with the provided email or phone Already Exists");
           }
       }catch (Exception e){
           throw new CustomException(e);
       }
    }

    @Override
    @Transactional
    public User updateUser(UUID userId, UpdateUserDTO updateUserDTO) {
       try {
           Optional<User> optionalUser = userRepository.findUserByEmailOrPhoneNumber(updateUserDTO.getEmail() , updateUserDTO.getPhoneNumber());
           if(optionalUser.isEmpty()){
               User user =  userRepository.findById(userId).orElseThrow(() -> {throw new NotFoundException("The Resource was not found");
               });
               try {
                   user.setEmail(updateUserDTO.getEmail());
                   user.setPhoneNumber(updateUserDTO.getPhoneNumber());
                   user.setUsername(updateUserDTO.getUsername());
                   return user;
               }catch (Exception e){
                   throw new InternalServerErrorException(e.getMessage());
               }
           }else{
               throw new BadRequestAlertException("The email or password is already taken");
           }
       }catch (Exception e){
           throw new CustomException(e);
       }
    }

    @Override
    public User deleteUser(UUID userId) {
       try {
           User user = userRepository.findById(userId).orElseThrow(() -> {throw new NotFoundException("The Resource was not found");
           });
            userRepository.deleteById(userId);
            return user;
       }catch (Exception e){
           throw new CustomException(e);
       }
    }




    @Override
    public User inviteUser(InviteUserDTO inviteUserDTO) {
        try {
            User userByEmail = userRepository.findUserByEmail(inviteUserDTO.getEmail()).orElse(null);
            if(userByEmail != null){
                throw new BadRequestAlertException("The user with the given email already exists");
            }
            User userEntity = new User();
            userEntity.setEmail(inviteUserDTO.getEmail());
            userEntity.setUsername(inviteUserDTO.getUsername());
            Role role = roleService.getRoleByName(EUserRole.USER);
            HashSet<Role> roles = new HashSet<>();
            roles.add(role);
            userEntity.setRoles(roles);
            userEntity.setStatus(EStatus.DISABLED);
            mailService.sendInvitationEmail(userEntity);
            userRepository.save(userEntity);
            return userEntity;
        }catch (BadRequestAlertException e){
            throw new BadRequestAlertException(e.getMessage());
        }catch (NotFoundException e){
            throw new NotFoundException(e.getMessage());
        }
        catch (Exception e){
            throw new CustomException(e);
        }
    }

    @Override
    @Transactional
    public boolean isUserInvited(String email, String token) {
        try {
            User user = userRepository.findUserByEmail(email).orElseThrow(()-> new NotFoundException("The user with the given email was not found"));
            String expirationToken = user.getExpirationToken();
            if(expirationToken == null){
                throw new BadRequestAlertException("Invalid invitation please first validated the invitation");
            }
            if(!token.equals(expirationToken)) {
                throw new BadRequestAlertException("Invalid invitation please first validated the invitation");
            }
            boolean isValid = ExpirationTokenUtils.isTokenValid(expirationToken, validityDays);
            if(isValid){
                user.setStatus(EStatus.NO_PROFILE);
            }
            return isValid;
        }catch (Exception e){
            throw new CustomException(e);
        }
    }

    @Override
    public User getLoggedInUser() {
         try {
             UserPrincipal userSecurityDetails;
             // Retrieve the currently authenticated user from the SecurityContextHolder
             Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

             if (authentication != null) {
                 userSecurityDetails = (UserPrincipal) authentication.getPrincipal();
                 return this.userRepository.findUserByEmail(userSecurityDetails.getUsername()).orElseThrow(() -> new UnAuthorizedException("You are not authorized! please login"));
             } else {
                 throw new UnAuthorizedException("You are not authorized! please login");
             }
         }catch (Exception e){
             throw new CustomException(e);
         }
    }


}
