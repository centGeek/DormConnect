package pl.lodz.dormConnect.commonRoom.dto;

import pl.lodz.dormConnect.commonRoom.entity.CommonRoomEntity;

public record CommonRoomGetDTO(
    Long id,
    CommonRoomEntity.CommonRoomType type,
    int floor,
    int capacity,
    int timesAWeekYouCanUseIt
    ){}
