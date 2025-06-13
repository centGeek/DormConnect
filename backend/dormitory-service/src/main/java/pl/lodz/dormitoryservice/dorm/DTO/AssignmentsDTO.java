package pl.lodz.dormitoryservice.dorm.DTO;

import java.time.LocalDate;


public record AssignmentsDTO(
        Long id,
        Long userId,
        String userFullName,
        String roomNumber,
        Integer roomFloor,
        LocalDate startDate,
        LocalDate endDate
) {
}
