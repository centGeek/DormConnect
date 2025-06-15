package pl.lodz.dormitoryservice.fixtures;

import pl.lodz.dormitoryservice.commonRoom.dto.CommonRoomAssignmentGetDTO;
import pl.lodz.dormitoryservice.entity.CommonRoomAssignmentEntity;

import java.time.Instant;
import java.util.Date;
import java.util.List;

public class CommonRoomAssignmentsFixture {

    public static CommonRoomAssignmentGetDTO anyCommonRoomAssignmentGetDTO() {
        return new CommonRoomAssignmentGetDTO(1L,
                Date.from(Instant.now()),
                Date.from(Instant.now().plusSeconds(3600)),
                3, true,
                true, true);
    }

    public static CommonRoomAssignmentEntity anyCommonRoomAssignmentEntity() {
        return new CommonRoomAssignmentEntity(
                CommonRoomFixtures.anyCommonRoomEntity(),
                List.of(),
                false,
                Date.from(Instant.now()),
                Date.from(Instant.now().plusSeconds(3600)));
    }

    public static CommonRoomAssignmentEntity anotherCommonRoomAssignmentEntity() {
        return new CommonRoomAssignmentEntity(
                CommonRoomFixtures.anyCommonRoomEntity(),
                List.of(),
                true,
                Date.from(Instant.now()),
                Date.from(Instant.now().plusSeconds(3500)));
    }
}
