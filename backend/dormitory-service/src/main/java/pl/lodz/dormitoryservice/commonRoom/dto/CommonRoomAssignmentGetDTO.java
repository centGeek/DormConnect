package pl.lodz.dormitoryservice.commonRoom.dto;

import java.util.Date;

public record CommonRoomAssignmentGetDTO(
        Long id,
        Date startDate,
        Date endDate,
        int numberOfUsers,
        boolean isUserAssigned,
        boolean isFull,
        boolean isArchived
) {
}
