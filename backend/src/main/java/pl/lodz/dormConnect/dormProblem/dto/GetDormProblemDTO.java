package pl.lodz.dormConnect.dormProblem.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import pl.lodz.dormConnect.dormProblem.model.ProblemStatus;

import java.time.LocalDate;

public record GetDormProblemDTO(
        @NotNull
        Long id,
        @NotBlank
        Long studentId,
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
