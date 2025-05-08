package pl.lodz.dormConnect.commonRoom.dto;

import pl.lodz.dormConnect.commonRoom.entity.CommonRoomEntity;

public record CommonRoomDTO(
        CommonRoomEntity.CommonRoomType type,
        int capacity,
        int floor,
        int maxTimeYouCanStay,
        int howManyTimesAWeekYouCanUseIt,
        boolean active
) { }