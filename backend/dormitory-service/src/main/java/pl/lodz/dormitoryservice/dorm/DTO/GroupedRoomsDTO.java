package pl.lodz.dormitoryservice.dorm.DTO;

import java.util.List;

public record GroupedRoomsDTO(
        Long id,
        String groupName,
        List<RoomInGroupDTO> rooms
) {}
