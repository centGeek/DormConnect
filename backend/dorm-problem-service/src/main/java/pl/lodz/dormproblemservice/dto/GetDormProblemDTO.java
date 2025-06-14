package pl.lodz.dormproblemservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import pl.lodz.dormproblemservice.ProblemStatus;

import java.time.LocalDate;

public record GetDormProblemDTO(
        @NotNull
        Long id,
        @NotBlank
        Long studentId,
        String name,
        String description,
        String answer,
        @NotNull
        LocalDate problemDate,
        @NotNull
        LocalDate submittedDate,
        @NotNull
        ProblemStatus problemStatus
) {


}
