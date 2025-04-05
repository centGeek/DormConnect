package pl.lodz.dormConnect.dorm;

import pl.lodz.dormConnect.dorm.entities.RoomEntity;

public class DormTestRooms {
    public static RoomEntity testRoom1(){
        RoomEntity room = new RoomEntity();
        room.setNumber("TEST_ROOM_1");
        return room;
    }

}
