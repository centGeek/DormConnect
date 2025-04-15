package pl.lodz.dormConnect.dorm.controllers;

import pl.lodz.dormConnect.dorm.entities.RoomEntity;

import java.time.LocalDate;


public record RoomAssignDTO(
        RoomEntity roomEntity,
        Long idOfStudent,
        LocalDate fromDate,
        LocalDate toDate
) {
}