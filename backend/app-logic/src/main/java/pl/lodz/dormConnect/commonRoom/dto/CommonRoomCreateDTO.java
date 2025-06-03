package pl.lodz.dormConnect.commonRoom.dto;

import pl.lodz.commons.entity.CommonRoomEntity;

public record CommonRoomCreateDTO(
        CommonRoomEntity.CommonRoomType type,
        int capacity,
        int floor,
        int maxTimeYouCanStay,
        int howManyTimesAWeekYouCanUseIt
) { }

