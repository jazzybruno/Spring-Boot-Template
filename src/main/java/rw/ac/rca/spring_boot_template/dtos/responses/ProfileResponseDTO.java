package rw.ac.rca.spring_boot_template.dtos.responses;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import rw.ac.rca.spring_boot_template.models.Role;
import rw.ac.rca.spring_boot_template.models.User;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
public class ProfileResponseDTO {
    private User user;
    private Set<Role> roles;

    public ProfileResponseDTO(User user, Set<Role> roles) {
        this.user = user;
        this.roles = roles;
    }

}

