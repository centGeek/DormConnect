package pl.lodz.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import pl.lodz.UserAlreadyExistsException;
import pl.lodz.model.Manager;
import pl.lodz.model.Student;
import pl.lodz.service.ManagerRegisterService;
import pl.lodz.service.StudentRegisterService;

import java.util.Map;

@RestController
@RequestMapping("/register")
@AllArgsConstructor
@Slf4j
public class RegisterController {

    private final ManagerRegisterService managerRegisterService;
    private final StudentRegisterService studentRegisterService;


    @PostMapping("/manager")
    public ResponseEntity<Void> registerManager(@Validated @RequestBody Manager manager) {
        managerRegisterService.registerManager(manager);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/student")
    public ResponseEntity<?> registerStudent(@Validated @RequestBody Student student) {
        try {
            studentRegisterService.registerStudent(student);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (UserAlreadyExistsException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("message", "Użytkownik o podanym mailu już istnieje"));
        }
    }
}
