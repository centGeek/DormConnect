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
import pl.lodz.commons.model.Manager;
import pl.lodz.dormConnect.security.service.ManagerRegisterService;

@RestController
@RequestMapping("/api/manager")
@AllArgsConstructor
@Slf4j
public class ManagerRegisterController {

    private final ManagerRegisterService managerRegisterService;

    @PostMapping("/register")
    public ResponseEntity<Void> registerStudent(@Validated @RequestBody Manager manager) {
        managerRegisterService.registerStudent(manager);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
