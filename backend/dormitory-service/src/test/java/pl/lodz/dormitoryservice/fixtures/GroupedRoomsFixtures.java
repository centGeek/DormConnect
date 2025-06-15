package pl.lodz.dormitoryservice.fixtures;

import pl.lodz.dormitoryservice.entity.GroupedRoomsEntity;

import java.util.List;

public class GroupedRoomsFixtures {

    public static GroupedRoomsEntity anyGroupedRoomsEntity(){
        return new GroupedRoomsEntity("grupa niepełnosprawna",
                List.of());
    }
}
