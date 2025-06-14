package pl.lodz.dormitoryservice.dorm.DTO;


import java.time.LocalDate;
import java.util.List;

public record DeleteRoomImpactPreviewDTO(
        Long roomId,
        boolean canDeleteNow,
        LocalDate minRelocationDate,
        List<ResidentReassignmentPreview> currentResidents,
        List<ResidentReassignmentPreview> futureResidents
) {}
