package pl.lodz.dormitoryservice.dorm.DTO;

import java.util.List;

public record GroupRoomsRequest(
        String groupName,
        List<Long> roomIds
) {
}
