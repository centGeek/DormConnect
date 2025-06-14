package pl.lodz.dormitoryservice.fixtures;

import pl.lodz.dormitoryservice.entity.RoomAssignEntity;

import java.time.LocalDate;

public class RoomsAssignFixtures {

    public static RoomAssignEntity anyRoomAssignEntity(){
        return new RoomAssignEntity(RoomFixtures.anyRoomEntity(), 1L,
                LocalDate.of(2025, 6, 12),
                LocalDate.of(2025, 6, 13));
    }

    public static RoomAssignEntity anotherRoomAssignEntity(){
        return new RoomAssignEntity(RoomFixtures.anotherRoomEntity(), 2112L,
                LocalDate.of(2025, 7, 12),
                LocalDate.of(2025, 7, 13));
    }
}
