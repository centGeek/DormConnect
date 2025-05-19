package pl.lodz.dormConnect.dormProblem.dto;

import jakarta.validation.constraints.NotNull;
import org.springframework.lang.Nullable;
import pl.lodz.dormConnect.dormProblem.model.ProblemStatus;

public record UpdateDormProblemDTO(
        @NotNull
        Long id,
        @NotNull
        Long studentId,
        String name,
        @NotNull
        String description,
        @Nullable
        String answer,
        @NotNull
        ProblemStatus problemStatus
) {
}
