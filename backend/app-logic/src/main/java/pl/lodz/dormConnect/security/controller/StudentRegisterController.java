package pl.lodz.dormConnect.security.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.lodz.commons.model.Student;
import pl.lodz.dormConnect.security.service.StudentRegisterService;

@RestController
@RequestMapping("/api/student")
@AllArgsConstructor
@Slf4j
public class StudentRegisterController {

    private final StudentRegisterService studentRegisterService;

    @PostMapping("/register")
    public ResponseEntity<Void> registerStudent(@Validated @RequestBody Student student) {
        studentRegisterService.registerStudent(student);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}