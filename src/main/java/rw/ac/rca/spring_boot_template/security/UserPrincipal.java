package rw.ac.rca.spring_boot_template.security;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import rw.ac.rca.spring_boot_template.enumerations.EGender;
import rw.ac.rca.spring_boot_template.models.User;

import java.util.Collection;
import java.util.UUID;
import java.util.stream.Collectors;

@Getter
@Setter
@AllArgsConstructor
public class UserPrincipal implements UserDetails {
    private UUID id;
    private String email;
    private String phoneNumber;
    @JsonIgnore
    private String password;
    private String username;
    private EGender gender;
    private Collection<? extends GrantedAuthority> authorities;
    public static UserPrincipal create(User user) {
        Collection<SimpleGrantedAuthority> authorities = user.getRoles()
                .stream()
                .map(role -> {
                    return new SimpleGrantedAuthority(role.getRoleName().toString());
                })
                .collect(Collectors.toList());

        return new UserPrincipal(
                user.getId(),
                user.getEmail(),
                user.getPhoneNumber(),
                user.getPassword(),
                user.getUsername(),
                user.getGender(),
                authorities);
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}

