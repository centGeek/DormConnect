package pl.lodz.dormConnect.dorm.DTO;

import pl.lodz.commons.entity.RoomEntity;

import java.time.LocalDate;


public record RoomAssignDTO(
        RoomEntity roomEntity,
        Long idOfStudent,
        LocalDate fromDate,
        LocalDate toDate
) {
}