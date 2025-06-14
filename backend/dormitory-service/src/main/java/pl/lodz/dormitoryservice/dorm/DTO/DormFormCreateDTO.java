package pl.lodz.dormitoryservice.dorm.DTO;

import java.time.LocalDate;

public record DormFormCreateDTO(
        Long userId,
        LocalDate startDate,
        LocalDate endDate,
        boolean isProcessed,
        String comments,
        Integer priorityScore
) {
}
