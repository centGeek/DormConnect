package pl.lodz.dormConnect.dormProblem.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record CreateDormProblemDTO(
        @NotBlank
        Long studentId,
        String name,
        String description,
        @NotNull
        LocalDate problemDate
) {

}
