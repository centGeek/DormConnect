package pl.lodz.dormConnect.commonRoom.dto;


import java.util.Date;
import java.util.List;


public record CommonRoomAssigmentCreateDTO(
    Long commonRoomId,
    List<Long> userIds,
    boolean archived,
    Date startDate,
    Date endDate
){
}
