package pl.lodz.dormitoryservice.dorm.DTO;

import java.time.LocalDate;

public record ResidentReassignmentPreview(
        Long userId,
        String fullName,
        LocalDate fromDate,
        LocalDate toDate,
        LocalDate plannedNewStartDate,
        boolean roomAvailable
) {}
