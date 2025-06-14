package pl.lodz.dormitoryservice.dorm.DTO;

import java.time.LocalDate;

public record DormFormDTO(
        Long id,
        Long userId,
        LocalDate startDate,
        LocalDate endDate,
        boolean isProcessed,
        String comments,
        Integer priorityScore
) {
}
