package pl.lodz.dormitoryservice.fixtures;

import pl.lodz.dormitoryservice.entity.FloorEntity;

import java.util.List;

public class FloorFixture {

    public static FloorEntity anyFloorEntity() {
        return new FloorEntity(1L, 1, List.of(1L, 2L), List.of(3L, 4L));
    }

    public static FloorEntity anotherFloorEntity() {
        return new FloorEntity(1L, 2, List.of(4L, 5L), List.of(6L, 7L));
    }
}
