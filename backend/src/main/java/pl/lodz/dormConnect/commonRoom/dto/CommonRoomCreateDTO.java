package pl.lodz.dormConnect.schedule.dto;

public record CommonRoomCreateDTO(
        String type,
        int capacity,
        int floor,
        int maxTimeYouCanStay,
        int howManyTimesAWeekYouCanUseIt,
        boolean active
) { }