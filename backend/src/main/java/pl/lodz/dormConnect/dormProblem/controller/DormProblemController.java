package pl.lodz.dormConnect.dormProblem.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.lodz.dormConnect.dormProblem.dto.DormProblemDTO;
import pl.lodz.dormConnect.dormProblem.mapper.DormProblemMapper;
import pl.lodz.dormConnect.dormProblem.model.DormProblem;
import pl.lodz.dormConnect.dormProblem.repository.DormProblemRepository;
import pl.lodz.dormConnect.dormProblem.service.DormProblemService;

import java.util.List;
import java.util.stream.Collectors;

import static pl.lodz.dormConnect.dormProblem.mapper.DormProblemMapper.mapToDTO;

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
    public ResponseEntity<DormProblemDTO> createDormProblem(@RequestBody DormProblemDTO dormProblemDTO) {
        DormProblemDTO created = dormProblemService.createDormProblem(dormProblemDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping("/get")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<DormProblemDTO>> getDormProblems() {
        List<DormProblemDTO> dormProblems = dormProblemService.getAllDormProblems();
        return ResponseEntity.status(HttpStatus.OK).body(dormProblems);

    }

    @GetMapping("/get/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<DormProblemDTO> getDormProblem(@PathVariable Long id) {
        DormProblemDTO dormProblemDTO = dormProblemService.getDormProblemById(id);
        return ResponseEntity.status(HttpStatus.OK).body(dormProblemDTO);
    }

    @PostMapping("/update/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<DormProblemDTO> updateDormProblem(@RequestBody DormProblemDTO dormProblemDTO) {
        DormProblemDTO dormProblem = dormProblemService.updateDormProblem(dormProblemDTO);
        return ResponseEntity.status(HttpStatus.OK).body(dormProblem);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<DormProblemDTO> deleteDormProblemById(@PathVariable Long id) {
        DormProblemDTO deleted = dormProblemService.getDormProblemById(id);
        dormProblemService.deleteDormProblem(id);
        return ResponseEntity.status(HttpStatus.OK).body(deleted);
    }


}
