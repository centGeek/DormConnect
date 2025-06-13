package pl.lodz.dormitoryservice.dorm.DTO;

public record RoomInGroupDTO(
        Long id,
        String number,
        int capacity,
        int floor,
        boolean active,
        Long groupId
) {}
