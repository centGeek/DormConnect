package pl.lodz.dormproblemservice.dto;

import jakarta.validation.constraints.NotNull;
import org.springframework.lang.Nullable;
import pl.lodz.dormproblemservice.ProblemStatus;

public record UpdateDormProblemDTO(
        @NotNull
        Long id,
        @NotNull
        Long studentId,
        String userName,
        String name,
        @NotNull
        String description,
        @Nullable
        String answer,
        @NotNull
        ProblemStatus problemStatus
) {
}
