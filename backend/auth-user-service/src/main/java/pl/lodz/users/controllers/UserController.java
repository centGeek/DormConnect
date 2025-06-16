package pl.lodz.users.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import pl.lodz.users.dto.GetUserDTO;
import pl.lodz.users.dto.UpdateUserDTO;
import pl.lodz.users.exceptions.UserException;
import pl.lodz.users.services.UserService;
import pl.lodz.config.JwtService;

import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;
    private final JwtService jwtService;

    public UserController(UserService userService, JwtService jwtService) {
        this.userService = userService;
        this.jwtService = jwtService;
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<GetUserDTO>> getAllUsers() {
        try {
            return ResponseEntity.ok(userService.getAllUsers());
        } catch (Exception e) {
            throw new UserException("Error fetching users: " + e.getMessage());
        }
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<GetUserDTO> getUserById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(userService.getUserById(id));
        } catch (Exception e) {
            throw new UserException("Error fetching user by ID: " + e.getMessage(), e);
        }
    }


    @GetMapping("/get/fullname/{id}")
    public ResponseEntity<String> getFullNameUserById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(userService.getFullNameUserById(id));
        } catch (Exception e) {
            throw new UserException("Error fetching user by ID: " + e.getMessage(), e);
        }
    }

    @PutMapping("/update")
    @Transactional
    public ResponseEntity<GetUserDTO> updateUser(@RequestBody UpdateUserDTO entity) {
        try {
            if (entity == null) {
                throw new UserException("Invalid input: id or entity cannot be null");
            }

            GetUserDTO updatedUser = userService.updateUser(entity);

            return ResponseEntity.ok(updatedUser);
        } catch (Exception e) {
            throw new UserException("Error updating user: " + e.getMessage(), e);
        }
    }

    @GetMapping("get/username/{id}")
    public ResponseEntity<String> getUserByUsername(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(userService.getUsernameById(id));
        } catch (Exception e) {
            throw new UserException("Error fetching user by username: " + e.getMessage(), e);
        }
    }


    @PostMapping("/update-uuid")
    @Transactional
    public ResponseEntity<GetUserDTO> updateUserUuid(@RequestBody UpdateUserDTO entity) {
        try {
            if (entity == null || entity.uuid() == null) {
                throw new UserException("Invalid input: entity or uuid cannot be null");
            }

            GetUserDTO updatedUser = userService.updateUserUuid(entity);
            return ResponseEntity.ok(updatedUser);
        } catch (Exception e) {
            throw new UserException("Error updating user UUID: " + e.getMessage(), e);
        }
    }


    @DeleteMapping("/delete/{id}")
    @Transactional
    public ResponseEntity<String> deleteUser(@PathVariable Long id) {
        try {
            userService.deleteUser(id);
            return ResponseEntity.ok().body("User deleted successfully");
        } catch (Exception e) {
            throw new UserException("Error deleting user: " + e.getMessage(), e);
        }
    }

    @PutMapping("/update/password")
    public ResponseEntity<String> updatePassword(@RequestHeader("Authorization") String token_bearer,
                                                 @RequestBody Map<String, String> body){
        String newPassword = body.get("newPassword");
        String oldPassword = body.get("oldPassword");
        String token = token_bearer.replace("Bearer ", "");
        Long organizerId = jwtService.getIdFromToken(token);
        try{
            userService.changeUserPassword(organizerId, newPassword, oldPassword);
            return ResponseEntity.ok("Password updated successfully");
        } catch (Exception e) {
            throw new UserException("Error updating password: " + e.getMessage(), e);
        }
    }

    @GetMapping("/get/name-surname/{id}")
    public ResponseEntity<Map<String, String>> getUserNameAndSurnameById(@PathVariable Long id) {
        try {
            Map<String, String> result = userService.getUserNameAndSurnameById(id);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            throw new UserException("Error fetching name and surname: " + e.getMessage(), e);
        }
    }

    @PutMapping("/update/username")
    public ResponseEntity<String> updateUsername(@RequestHeader("Authorization") String token_bearer,
                                                 @RequestBody Map<String, String> body) {
        String newUsername = body.get("newUsername");
        String token = token_bearer.replace("Bearer ", "");
        Long userId = jwtService.getIdFromToken(token);
        try {
            userService.updateUsername(userId, newUsername);
            return ResponseEntity.ok("Username updated successfully");
        } catch (Exception e) {
            throw new UserException("Error updating username: " + e.getMessage(), e);
        }
    }
}
