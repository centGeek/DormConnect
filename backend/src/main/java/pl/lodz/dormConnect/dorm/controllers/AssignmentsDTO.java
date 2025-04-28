package pl.lodz.dormConnect.dorm.controllers;

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
