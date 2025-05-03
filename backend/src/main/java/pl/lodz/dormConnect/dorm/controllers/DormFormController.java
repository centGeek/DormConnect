package pl.lodz.dormConnect.dorm.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.lodz.dormConnect.dorm.DTO.DormFormCreateDTO;
import pl.lodz.dormConnect.dorm.DTO.DormFormDTO;
import pl.lodz.dormConnect.dorm.entities.DormFormEntity;
import pl.lodz.dormConnect.dorm.services.DormFormService;

@RestController
@RequestMapping("/api/dorm/form")
@RequiredArgsConstructor
public class DormFormController {

    private final DormFormService dormFormService;


    @PostMapping("/new")
    public ResponseEntity<?> submitDormForm(@RequestBody DormFormCreateDTO dto) {
        try {
            DormFormEntity entity = new DormFormEntity();
            entity.setUserId(dto.userId());
            entity.setStartDate(dto.startDate());
            entity.setEndDate(dto.endDate());
            entity.setProcessed(dto.isProcessed());
            entity.setComments(dto.comments());
            entity.setPriorityScore(dto.priorityScore());

            DormFormEntity saved = dormFormService.submitDormForm(entity);

            DormFormDTO response = new DormFormDTO(
                    saved.getId(),
                    saved.getUserId(),
                    saved.getStartDate(),
                    saved.getEndDate(),
                    saved.isProcessed(),
                    saved.getComments(),
                    saved.getPriorityScore()
            );

            return ResponseEntity.status(HttpStatus.CREATED).body(response);

        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Form cannot be submitted: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Unexpected error occurred: " + e.getMessage());
        }
    }
}
