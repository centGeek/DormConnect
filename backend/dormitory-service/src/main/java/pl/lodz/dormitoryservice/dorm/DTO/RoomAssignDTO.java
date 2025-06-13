package pl.lodz.dormitoryservice.dorm.DTO;


import pl.lodz.dormitoryservice.entity.RoomEntity;

import java.time.LocalDate;


public record RoomAssignDTO(
        RoomEntity roomEntity,
        Long idOfStudent,
        LocalDate fromDate,
        LocalDate toDate
) {
}