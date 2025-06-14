package pl.lodz.dormitoryservice.fixtures;

import pl.lodz.dormitoryservice.entity.RoomEntity;

import java.util.List;

public class RoomFixtures {
    public static RoomEntity anyRoomEntity(){
        return new RoomEntity("420", 43, 4, true, List.of(),
                null);
    }

    public static RoomEntity anotherRoomEntity(){
        return new RoomEntity("422", 43, 4, true, List.of(),
                null);
    }
}
