package pl.lodz.dormitoryservice.dorm.mapper;

import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;
import pl.lodz.dormitoryservice.dorm.DTO.GroupedRoomsDTO;
import pl.lodz.dormitoryservice.dorm.DTO.RoomInGroupDTO;
import pl.lodz.dormitoryservice.entity.GroupedRoomsEntity;
import pl.lodz.dormitoryservice.entity.RoomEntity;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class GroupedRoomsMapperTest {

    @Test
    void toDtoShouldMapGroupedRoomsEntityToDto() {
        GroupedRoomsEntity groupedRooms = getGroupedRoomsEntity();

        GroupedRoomsDTO dto = GroupedRoomsMapper.toDto(groupedRooms);

        assertThat(dto).isNotNull();
        assertThat(dto.id()).isEqualTo(42L);
        assertThat(dto.groupName()).isEqualTo("Test Group");
        assertThat(dto.rooms()).hasSize(2);

        RoomInGroupDTO dtoRoom1 = dto.rooms().get(0);
        assertThat(dtoRoom1.id()).isEqualTo(1L);
        assertThat(dtoRoom1.number()).isEqualTo("101");
        assertThat(dtoRoom1.capacity()).isEqualTo(2);
        assertThat(dtoRoom1.floor()).isEqualTo(1);
        assertThat(dtoRoom1.active()).isTrue();
        assertThat(dtoRoom1.groupId()).isEqualTo(42L);

        RoomInGroupDTO dtoRoom2 = dto.rooms().get(1);
        assertThat(dtoRoom2.id()).isEqualTo(2L);
        assertThat(dtoRoom2.number()).isEqualTo("102");
        assertThat(dtoRoom2.capacity()).isEqualTo(3);
        assertThat(dtoRoom2.floor()).isEqualTo(1);
        assertThat(dtoRoom2.active()).isFalse();
        assertThat(dtoRoom2.groupId()).isEqualTo(42L);
    }

    @NotNull
    private static GroupedRoomsEntity getGroupedRoomsEntity() {
        GroupedRoomsEntity groupedRooms = new GroupedRoomsEntity();
        groupedRooms.setId(42L);
        groupedRooms.setGroupName("Test Group");

        RoomEntity room1 = new RoomEntity();
        room1.setId(1L);
        room1.setNumber("101");
        room1.setCapacity(2);
        room1.setFloor(1);
        room1.setActive(true);
        room1.setGroupedRooms(groupedRooms);

        RoomEntity room2 = new RoomEntity();
        room2.setId(2L);
        room2.setNumber("102");
        room2.setCapacity(3);
        room2.setFloor(1);
        room2.setActive(false);
        room2.setGroupedRooms(groupedRooms);

        groupedRooms.setRooms(List.of(room1, room2));
        return groupedRooms;
    }

    @Test
    void toRoomDtoShouldHandleNullGroupedRooms() {
        RoomEntity room = new RoomEntity();
        room.setId(5L);
        room.setNumber("200");
        room.setCapacity(1);
        room.setFloor(2);
        room.setActive(true);
        room.setGroupedRooms(null);

        RoomInGroupDTO dto = GroupedRoomsMapper.toRoomDto(room);

        assertThat(dto.groupId()).isNull();
    }
}
