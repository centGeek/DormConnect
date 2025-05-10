package pl.lodz.dormConnect.dormProblem.dto;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import pl.lodz.dormConnect.dormProblem.model.ProblemStatus;
import pl.lodz.dormConnect.events.validation.ValidEventDates;

import java.time.LocalDate;

public record DormProblemDTO(
        @NotNull
        Long id,
        @NotBlank
        Long studentId,
        String description,
        @NotNull
        LocalDate problemDate,
        @NotNull
        ProblemStatus problemStatus
) {


}
