package pl.lodz.dormConnect.dormProblem.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.lodz.dormConnect.dormProblem.dto.CreateDormProblemDTO;
import pl.lodz.dormConnect.dormProblem.dto.GetDormProblemDTO;
import pl.lodz.dormConnect.dormProblem.dto.UpdateDormProblemDTO;
import pl.lodz.dormConnect.dormProblem.model.ProblemStatus;
import pl.lodz.dormConnect.dormProblem.service.DormProblemService;

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
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<GetDormProblemDTO> createDormProblem(@RequestBody CreateDormProblemDTO createDormProblemDTO) {
        GetDormProblemDTO created = dormProblemService.createDormProblem(createDormProblemDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping("/get")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<GetDormProblemDTO>> getDormProblems() {
        List<GetDormProblemDTO> dormProblems = dormProblemService.getAllDormProblems();
        return ResponseEntity.status(HttpStatus.OK).body(dormProblems);

    }

    @GetMapping("/get/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<GetDormProblemDTO> getDormProblem(@PathVariable Long id) {
        GetDormProblemDTO getDormProblemDTO = dormProblemService.getDormProblemById(id);
        return ResponseEntity.status(HttpStatus.OK).body(getDormProblemDTO);
    }

    @PostMapping("/update/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<GetDormProblemDTO> updateDormProblem(@RequestBody UpdateDormProblemDTO updateDormProblemDTO) {
        GetDormProblemDTO dormProblem = dormProblemService.updateDormProblem(updateDormProblemDTO);
        return ResponseEntity.status(HttpStatus.OK).body(dormProblem);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<GetDormProblemDTO> deleteDormProblemById(@PathVariable Long id) {
        GetDormProblemDTO deleted = dormProblemService.getDormProblemById(id);
        dormProblemService.deleteDormProblem(id);
        return ResponseEntity.status(HttpStatus.OK).body(deleted);
    }

    @GetMapping("/problem-statuses")
    public ResponseEntity<List<ProblemStatus>> getProblemStatuses() {
        List<ProblemStatus> dormProblems = dormProblemService.getAllProblemStatuses();
        return ResponseEntity.status(HttpStatus.OK).body(dormProblems);
    }


}
