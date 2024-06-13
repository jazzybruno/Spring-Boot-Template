package rw.ac.rca.spring_boot_template.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rw.ac.rca.spring_boot_template.dtos.requests.*;
import rw.ac.rca.spring_boot_template.models.Role;
import rw.ac.rca.spring_boot_template.models.User;
import rw.ac.rca.spring_boot_template.services.UserService;
import rw.ac.rca.spring_boot_template.utils.ApiResponse;

import java.util.List;
import java.util.UUID;

@RestController
@CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.PATCH, RequestMethod.OPTIONS})
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/all")
    public ResponseEntity<ApiResponse<List<User>>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        return ApiResponse.success(users).toResponseEntity();
    }

    @GetMapping("/id/{userId}")
    public ResponseEntity<ApiResponse<User>> getUserById(@PathVariable UUID userId) {
        User user = userService.getUserById(userId);
        return ApiResponse.success(user).toResponseEntity();
    }


    @GetMapping("/email/{email}")
    public ResponseEntity<ApiResponse<User>> getUserByEmail(@PathVariable String email) {
        User user = userService.getUserByEmail(email);
        return ApiResponse.success(user).toResponseEntity();
    }

    @PostMapping("/create-invite-user")
    public ResponseEntity<ApiResponse<User>> createUser(@RequestBody CreateUserDTO createUserDTO) {
        User user = userService.createUser(createUserDTO);
        return ApiResponse.success(user).toResponseEntity();
    }

    // inviting and creating the user
    @PostMapping("/create")
    public ResponseEntity<ApiResponse<User>> inviteUser(@RequestBody InviteUserDTO inviteUserDTO) {
        User user = userService.inviteUser(inviteUserDTO);
        return ApiResponse.success(user).toResponseEntity();
    }

    // code validation
    @PostMapping("/is-code-valid")
    public ResponseEntity<ApiResponse<Boolean>> validateUser(@RequestParam String email , @RequestParam String token) {
        boolean isValid = userService.isUserInvited(email, token);
        return ApiResponse.success(isValid).toResponseEntity();
    }

    @PostMapping("/create-admin")
    public ResponseEntity<ApiResponse<User>> createAdmin(@RequestBody CreateAdminDTO createAdminDTO) {
        User user = userService.createAdmin(createAdminDTO);
        return ApiResponse.success(user).toResponseEntity();
    }

    @PatchMapping("/update/{userId}")
    public ResponseEntity<ApiResponse<User>> updateUser(@PathVariable UUID userId, @RequestBody UpdateUserDTO updateUserDTO) {
        User user = userService.updateUser(userId, updateUserDTO);
        return ApiResponse.success(user).toResponseEntity();
    }

    @DeleteMapping("/delete/{userId}")
    public ResponseEntity<ApiResponse<User>> deleteUser(@PathVariable UUID userId) {
        User user = userService.deleteUser(userId);
        return ApiResponse.success(user).toResponseEntity();
    }

}

