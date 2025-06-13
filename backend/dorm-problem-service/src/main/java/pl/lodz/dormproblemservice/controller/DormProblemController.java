package pl.lodz.dormproblemservice.controller;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.lodz.dormproblemservice.ProblemStatus;
import pl.lodz.dormproblemservice.dto.CreateDormProblemDTO;
import pl.lodz.dormproblemservice.dto.GetDormProblemDTO;
import pl.lodz.dormproblemservice.dto.UpdateDormProblemDTO;
import pl.lodz.dormproblemservice.service.DormProblemService;

import java.util.List;

@RestController
@RequestMapping("/api/dorm-problem")
public class DormProblemController {

    private final DormProblemService dormProblemService;

    @Autowired
    public DormProblemController(DormProblemService dormProblemService) {
        this.dormProblemService = dormProblemService;
    }

    @PostMapping("/create")
    public ResponseEntity<GetDormProblemDTO> createDormProblem(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody CreateDormProblemDTO createDormProblemDTO) {

        String jwt = extractToken(authHeader);
        GetDormProblemDTO created = dormProblemService.createDormProblem(createDormProblemDTO, jwt);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping("/get")
    public ResponseEntity<List<GetDormProblemDTO>> getDormProblems(
            @RequestHeader("Authorization") String authHeader) {

        String jwt = extractToken(authHeader);
        List<GetDormProblemDTO> dormProblems = dormProblemService.getAllDormProblems(jwt);
        return ResponseEntity.ok(dormProblems);
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<GetDormProblemDTO> getDormProblem(@PathVariable Long id) {
        GetDormProblemDTO getDormProblemDTO = dormProblemService.getDormProblemById(id);
        return ResponseEntity.ok(getDormProblemDTO);
    }

    @PostMapping("/update/{id}")
    public ResponseEntity<GetDormProblemDTO> updateDormProblem(
            @RequestBody UpdateDormProblemDTO updateDormProblemDTO) {

        GetDormProblemDTO dormProblem = dormProblemService.updateDormProblem(updateDormProblemDTO);
        return ResponseEntity.ok(dormProblem);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<GetDormProblemDTO> deleteDormProblemById(@PathVariable Long id) {
        GetDormProblemDTO deleted = dormProblemService.getDormProblemById(id);
        dormProblemService.deleteDormProblem(id);
        return ResponseEntity.ok(deleted);
    }

    @GetMapping("/problem-statuses")
    public ResponseEntity<List<ProblemStatus>> getProblemStatuses() {
        List<ProblemStatus> dormProblems = dormProblemService.getAllProblemStatuses();
        return ResponseEntity.status(HttpStatus.OK).body(dormProblems);
    }

    private String extractToken(String header) {
        if (header != null && header.startsWith("Bearer ")) {
            return header.substring(7);
        }
        throw new IllegalArgumentException("Missing or invalid Authorization header");
    }
}
