package rw.ac.rca.spring_boot_template.security;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import rw.ac.rca.spring_boot_template.exceptions.BadRequestAlertException;
import rw.ac.rca.spring_boot_template.models.User;
import rw.ac.rca.spring_boot_template.repositories.IUserRepository;

import java.util.UUID;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    private final IUserRepository userRepository;

    @Autowired
    public CustomUserDetailsService(IUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    public UserDetails loadByUserId(UUID id) {
        User user = this.userRepository.findById(id).orElseThrow(() -> new UsernameNotFoundException("User not found with id: " + id));
        return UserPrincipal.create(user);
    }

    @Transactional
    public UserDetails loadUserByUsername(String s) throws BadRequestAlertException {
        User user = userRepository.findUserByEmailOrPhoneNumber(s, s).orElseThrow(() -> new UsernameNotFoundException("user not found with email or mobile of " + s));
//        if (!user.isVerified()) {
//            throw new BadRequestException("User is not verified");
//        }
//        //if status is not active
//        if (!user.getStatus().equals(Status.ACTIVE)) {
//            throw new BadRequestException("User is not active");
//        }
        return UserPrincipal.create(user);
    }
}