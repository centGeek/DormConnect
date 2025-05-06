package pl.lodz.dormConnect.schedule.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.lodz.dormConnect.schedule.entity.CommonRoomAssigment;
import pl.lodz.dormConnect.schedule.repositories.CommonRoomAssigmentRepository;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/common-room-assignment")
public class CommonRoomAssigmentController {

    private final CommonRoomAssigmentRepository repository;

    @Autowired
    public CommonRoomAssigmentController(CommonRoomAssigmentRepository repository) {
        this.repository = repository;
    }

    // Pobierz wszystkie przypisania
    @GetMapping
    public ResponseEntity<List<CommonRoomAssigment>> getAllAssignments() {
        List<CommonRoomAssigment> assignments = repository.findAll();
        return ResponseEntity.ok(assignments);
    }

    // Pobierz przypisanie na podstawie ID
    @GetMapping("/{id}")
    public ResponseEntity<CommonRoomAssigment> getAssignmentById(@PathVariable Long id) {
        Optional<CommonRoomAssigment> assignment = repository.findById(id);
        return assignment.map(ResponseEntity::ok)
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // Utwórz nowe przypisanie
    @PostMapping
    public ResponseEntity<CommonRoomAssigment> createAssignment(@RequestBody CommonRoomAssigment assignment) {
        CommonRoomAssigment savedAssignment = repository.save(assignment);
        return new ResponseEntity<>(savedAssignment, HttpStatus.CREATED);
    }

    // Zaktualizuj istniejące przypisanie
    @PutMapping("/{id}")
    public ResponseEntity<CommonRoomAssigment> updateAssignment(@PathVariable Long id, @RequestBody CommonRoomAssigment updatedAssignment) {
        if (!repository.existsById(id)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        updatedAssignment.setId(id);
        CommonRoomAssigment savedAssignment = repository.save(updatedAssignment);
        return ResponseEntity.ok(savedAssignment);
    }

    // Usuń przypisanie na podstawie ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAssignment(@PathVariable Long id) {
        if (!repository.existsById(id)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        repository.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}