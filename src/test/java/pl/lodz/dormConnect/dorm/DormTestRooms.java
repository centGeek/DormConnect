package pl.lodz.dormConnect.dorm;

import pl.lodz.dormConnect.dorm.entities.RoomAssignEntity;
import pl.lodz.dormConnect.dorm.entities.RoomEntity;

import java.time.LocalDate;
import java.time.Month;
import java.util.Date;

public class DormTestRooms {
    public static RoomEntity testRoom1(){
        RoomEntity room = new RoomEntity();
        room.setNumber("TEST_ROOM_1");
        room.setFloor(1);
        room.setActive(true);
        room.setCapacity(4);
        room.setGroupedRooms(null);
        return room;
    }

    public static RoomAssignEntity testRoomAssign1(){
        RoomAssignEntity roomAssignEntity = new RoomAssignEntity();
        roomAssignEntity.setRoom(testRoom1());

        roomAssignEntity.setFromDate(LocalDate.of(2024, Month.JANUARY, 1));
        return roomAssignEntity;
    }
}
