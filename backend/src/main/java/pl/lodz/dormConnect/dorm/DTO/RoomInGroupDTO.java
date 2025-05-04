package pl.lodz.dormConnect.dorm.DTO;

public record RoomInGroupDTO(
        Long id,
        String number,
        int capacity,
        int floor,
        boolean active,
        Long groupId
) {}
