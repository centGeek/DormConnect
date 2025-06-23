package pl.lodz.dormitoryservice.dorm.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
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
import pl.lodz.dormitoryservice.entity.DormFormEntity;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/dorm/form")
@RequiredArgsConstructor
public class DormFormController {
    private static final Logger logger = LoggerFactory.getLogger(DormFormController.class);
    private final DormFormService dormFormService;
    private final JwtService jwtService;

    @PostMapping
    public ResponseEntity<?> submitDormForm(
            @RequestBody DormFormCreateDTO dto,
            @RequestHeader("Authorization") String authorizationHeader
    ) {
        try {
            Long userId = jwtService.getIdFromToken(authorizationHeader.replace("Bearer ", ""));

            DormFormEntity entity = new DormFormEntity();
            entity.setUserId(userId); // we take userId from a token
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


            //Python script
            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule());
            String jsonDormForm = mapper.writeValueAsString(dto);

            ProcessBuilder pb = new ProcessBuilder(
                    "./.venv/Scripts/python.exe",
                    "./dormitory-service/src/main/resources/pl/lodz/dormitoryservice/scripts/generate_pdf.py"
            );

            Process process = pb.start();

            try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(process.getOutputStream()))) {
                writer.write(jsonDormForm);
                writer.flush();
                process.getOutputStream().close();
            }

            StringBuilder output = new StringBuilder();
            StringBuilder error = new StringBuilder();

            Thread outputThread = new Thread(() -> {
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        output.append(line).append("\n");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });

            Thread errorThread = new Thread(() -> {
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getErrorStream()))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        error.append(line).append("\n");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });

            outputThread.start();
            errorThread.start();

            int exitCode = process.waitFor();

            outputThread.join();
            errorThread.join();

            if (exitCode != 0) {
                logger.error("Python script error output:\n{}", error.toString());
                throw new RuntimeException("PDF generation failed. Details: " + error.toString());
            }

            String base64Pdf = output.toString().trim();


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
