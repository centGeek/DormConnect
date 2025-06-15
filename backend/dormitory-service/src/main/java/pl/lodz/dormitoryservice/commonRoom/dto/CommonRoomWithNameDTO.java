package pl.lodz.dormitoryservice.commonRoom.dto;

import pl.lodz.dormitoryservice.entity.CommonRoomEntity.CommonRoomType;

public record CommonRoomWithNameDTO(
    long id,
    CommonRoomType type,
    String name,
    int capacity,
    int floor,
    int housrOfTimeWindows,
    int howManyTimesAWeekYouCanUseIt,
    boolean isActive
) {
    
}
