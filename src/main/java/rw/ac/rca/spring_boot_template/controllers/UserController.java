package rw.ac.rca.spring_boot_template.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rw.ac.rca.spring_boot_template.dtos.requests.*;
import rw.ac.rca.spring_boot_template.models.User;
import rw.ac.rca.spring_boot_template.services.UserService;

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
    public ResponseEntity getAllUsers() {
        List<User> users = userService.getAllUsers();
        return ResponseEntity.ok().body(new ApiResponse(
                true,
                "Successfully fetched the users",
                users
        ));
    }

    @GetMapping("/id/{userId}")
    public ResponseEntity getUserById(@PathVariable UUID userId) {
        User user = userService.getUserById(userId);
        return ResponseEntity.ok().body(new ApiResponse(
                true,
                "Successfully fetched the user",
                user
        ));
    }


    @GetMapping("/email/{email}")
    public ResponseEntity getUserByEmail(@PathVariable String email) {
        User user = userService.getUserByEmail(email);
        return ResponseEntity.ok().body(new ApiResponse(
                true,
                "Successfully fetched the user",
                user
        ));
    }

    @PostMapping("/create-invite-user")
    public ResponseEntity createUser(@RequestBody CreateUserDTO createUserDTO) {
        User user = userService.createUser(createUserDTO);
        return ResponseEntity.ok().body(new ApiResponse(
                true,
                "Successfully created the user",
                user
        ));
    }

    // inviting and creating the user
    @PostMapping("/create")
    public ResponseEntity inviteUser(@RequestBody InviteUserDTO inviteUserDTO) {
        User user = userService.inviteUser(inviteUserDTO);
        return ResponseEntity.ok().body(new ApiResponse(
                true,
                "Successfully invited the user",
                user
        ));
    }

    // code validation
    @PostMapping("/is-code-valid")
    public ResponseEntity validateUser(@RequestParam String email , @RequestParam String token) {
        boolean isValid = userService.isUserInvited(email, token);
        return ResponseEntity.ok().body(new ApiResponse(
                true,
                "Successfully validated the user",
                isValid
        ));
    }

    @PostMapping("/create-admin")
    public ResponseEntity createAdmin(@RequestBody CreateAdminDTO createAdminDTO) {
        User user = userService.createAdmin(createAdminDTO);
        return ResponseEntity.ok().body(
                new ApiResponse(
                        true,
                        "Successfully created the admin",
                        user
                )
        );
    }

    @PatchMapping("/update/{userId}")
    public ResponseEntity updateUser(@PathVariable UUID userId, @RequestBody UpdateUserDTO updateUserDTO) {
        User user = userService.updateUser(userId, updateUserDTO);
        return ResponseEntity.ok(new ApiResponse(
                true,
                "Successfully updated the user",
                user
        ));
    }

    @DeleteMapping("/delete/{userId}")
    public ResponseEntity deleteUser(@PathVariable UUID userId) {
        User user = userService.deleteUser(userId);
        return ResponseEntity.ok(new ApiResponse(
                true,
                "Successfully deleted the user",
                user
        ));
    }

}

