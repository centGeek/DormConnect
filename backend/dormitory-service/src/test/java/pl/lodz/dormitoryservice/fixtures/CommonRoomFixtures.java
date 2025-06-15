package pl.lodz.dormitoryservice.fixtures;

import pl.lodz.dormitoryservice.commonRoom.dto.CommonRoomCreateDTO;
import pl.lodz.dormitoryservice.commonRoom.dto.CommonRoomGetDTO;
import pl.lodz.dormitoryservice.entity.CommonRoomEntity;

import java.util.List;

public class CommonRoomFixtures {

    public static CommonRoomEntity anyCommonRoomEntity(){
        return new CommonRoomEntity(CommonRoomEntity
                .CommonRoomType.BILLARD_ROOM,
                2, 1, 1, 1,
                true, List.of());
    }

    public static CommonRoomEntity commonRoomEntityWithFloor(int floor){
        return new CommonRoomEntity(CommonRoomEntity
                .CommonRoomType.BILLARD_ROOM,
                2, floor, 1, 1,
                true, List.of());
    }

    public static CommonRoomEntity anotherCommonRoomEntity(){
        return new CommonRoomEntity(CommonRoomEntity
                .CommonRoomType.FITNESS_ROOM,
                2, 2, 1, 1,
                true, List.of());
    }

    public static CommonRoomGetDTO anyCommonRoomGetDTO(){
        return new CommonRoomGetDTO(1L, CommonRoomEntity
                .CommonRoomType.BILLARD_ROOM,
                2, 1, 1);
    }

    public static CommonRoomCreateDTO anyCommonRoomCreateDTO(){
        return new CommonRoomCreateDTO(CommonRoomEntity
                .CommonRoomType.BILLARD_ROOM,
                2, 1, 1, 1);
    }
}
