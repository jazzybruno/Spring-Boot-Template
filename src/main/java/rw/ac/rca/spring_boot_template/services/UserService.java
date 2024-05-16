package rw.ac.rca.spring_boot_template.services;

import rw.ac.rca.spring_boot_template.dtos.requests.*;
import rw.ac.rca.spring_boot_template.models.User;

import java.util.List;
import java.util.UUID;

public interface UserService {
    public List<User> getAllUsers();
    public User getUserById(UUID uuid);
    public User getUserByEmail(String email);
    public User createUser(CreateUserDTO createUserDTO);
    public User createAdmin(CreateAdminDTO createAdminDTO);
    public User updateUser(UUID userId , UpdateUserDTO updateUserDTO);
    public User deleteUser(UUID userId);

    // inviting the user
    public User inviteUser(InviteUserDTO inviteUserDTO);
    public boolean isUserInvited(String email , String token);
    public User getLoggedInUser();
}
