package pl.lodz.dormitoryservice.commonRoom.dto;


import pl.lodz.dormitoryservice.entity.CommonRoomEntity;

public record CommonRoomCreateDTO(
        CommonRoomEntity.CommonRoomType type,
        int capacity,
        int floor,
        int maxTimeYouCanStay,
        int howManyTimesAWeekYouCanUseIt
) { }

