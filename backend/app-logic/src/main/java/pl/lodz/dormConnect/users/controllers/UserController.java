package pl.lodz.dormConnect.users.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import pl.lodz.dormConnect.users.dto.GetUserDTO;
import pl.lodz.dormConnect.users.dto.UpdateUserDTO;
import pl.lodz.dormConnect.users.exceptions.UserException;
import pl.lodz.dormConnect.users.services.UserService;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;


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
    
}
