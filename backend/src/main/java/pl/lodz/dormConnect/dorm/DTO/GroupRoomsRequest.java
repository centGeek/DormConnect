package pl.lodz.dormConnect.dorm.DTO;

import java.util.List;

public record GroupRoomsRequest(
        String groupName,
        List<Long> roomIds
) {
}
