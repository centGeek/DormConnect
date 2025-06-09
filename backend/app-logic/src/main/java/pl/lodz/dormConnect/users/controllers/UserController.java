package pl.lodz.dormConnect.users.controllers;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import pl.lodz.dormConnect.users.dto.GetUserDTO;
import pl.lodz.dormConnect.users.exceptions.UserException;
import pl.lodz.dormConnect.users.services.UserService;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/getAll")
    public List<GetUserDTO> getAllUsers() {
        try {
            return userService.getAllUsers();
        } catch (Exception e) {
            throw new UserException( "Error fetching users: " + e.getMessage());
        }
    }
    
}
