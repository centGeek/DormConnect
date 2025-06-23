package pl.lodz.dormitoryservice.dorm.controllers;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.lodz.dormitoryservice.common.JwtService;
import pl.lodz.dormitoryservice.dorm.DTO.DormFormCreateDTO;
import pl.lodz.dormitoryservice.dorm.DTO.DormFormDTO;
import pl.lodz.dormitoryservice.dorm.services.DormFormService;
import pl.lodz.dormitoryservice.dorm.services.PdfGeneratorService;
import pl.lodz.dormitoryservice.entity.DormFormEntity;

import java.util.List;

@RestController
@RequestMapping("/api/dorm/form")
@RequiredArgsConstructor
public class DormFormController {
    private static final Logger logger = LoggerFactory.getLogger(DormFormController.class);
    private final DormFormService dormFormService;
    private final JwtService jwtService;
    private final PdfGeneratorService pdfGeneratorService;

    @PostMapping
    public ResponseEntity<?> submitDormForm(
            @RequestBody DormFormCreateDTO dto,
            @RequestHeader("Authorization") String authorizationHeader
    ) {
        try {
            Long userId = jwtService.getIdFromToken(authorizationHeader.replace("Bearer ", ""));

            DormFormEntity entity = new DormFormEntity();
            entity.setUserId(userId);
            entity.setStartDate(dto.startDate());
            entity.setEndDate(dto.endDate());
            entity.setProcessed(dto.isProcessed());
            entity.setComments(dto.comments());
            entity.setPriorityScore(dto.priorityScore());

            String base64Pdf = pdfGeneratorService.generatePdfBase64FromForm(dto);

            return ResponseEntity.status(HttpStatus.CREATED).body(base64Pdf);

        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Form cannot be submitted: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Unexpected error occurred: " + e.getMessage());
        }
    }


    @PutMapping("/{formId}/deactivate")
    public ResponseEntity<?> deleteForm(
            @PathVariable Long formId,
            @RequestHeader("Authorization") String authorizationHeader
    ) {
        try {
            Long userId = jwtService.getIdFromToken(authorizationHeader.replace("Bearer ", ""));
            dormFormService.deleteForm(formId, userId);
            return ResponseEntity.ok().build();
        } catch (IllegalStateException e) {
            logger.error("Error deactivating form: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Cannot deactivate form: " + e.getMessage());
        } catch (Exception e) {
            logger.error("Unexpected error while deactivating form: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Unexpected error occurred");
        }
    }

    @GetMapping("/me")
    public ResponseEntity<?> getUserForms(
            @RequestHeader("Authorization") String authorizationHeader
    ) {
        try {
            Long userId = jwtService.getIdFromToken(authorizationHeader.replace("Bearer ", ""));
            List<DormFormDTO> forms = dormFormService.getUserForms(userId);
            forms = forms.stream().filter(form -> !form.isProcessed()).toList();
            return ResponseEntity.ok(forms);
        } catch (Exception e) {
            logger.error("Error retrieving user forms: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Unexpected error occurred");
        }
    }
}
