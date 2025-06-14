package pl.lodz.dormitoryservice.commonRoom.dto;


import pl.lodz.dormitoryservice.entity.CommonRoomEntity;

public record CommonRoomGetDTO(
    Long id,
    CommonRoomEntity.CommonRoomType type,
    int floor,
    int capacity,
    int timesAWeekYouCanUseIt
    ){}
