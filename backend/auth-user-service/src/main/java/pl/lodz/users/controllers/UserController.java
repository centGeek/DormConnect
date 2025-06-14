package pl.lodz.users.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import pl.lodz.users.dto.GetUserDTO;
import pl.lodz.users.dto.UpdateUserDTO;
import pl.lodz.users.exceptions.UserException;
import pl.lodz.users.services.UserService;

import java.util.List;


@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<GetUserDTO>> getAllUsers() {
        try {
            return ResponseEntity.ok(userService.getAllUsers());
        } catch (Exception e) {
            throw new UserException( "Error fetching users: " + e.getMessage());
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
}
