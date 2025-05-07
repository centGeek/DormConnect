package pl.lodz.dormConnect.commonRoom.dto;

public record CommonRoomCreateDTO(
        String type,
        int capacity,
        int floor,
        int maxTimeYouCanStay,
        int howManyTimesAWeekYouCanUseIt,
        boolean active
) { }