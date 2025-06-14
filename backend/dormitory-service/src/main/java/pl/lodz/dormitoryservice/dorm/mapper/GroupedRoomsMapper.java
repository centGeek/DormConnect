package pl.lodz.dormitoryservice.dorm.mapper;


import pl.lodz.dormitoryservice.dorm.DTO.GroupedRoomsDTO;
import pl.lodz.dormitoryservice.dorm.DTO.RoomInGroupDTO;
import pl.lodz.dormitoryservice.entity.GroupedRoomsEntity;
import pl.lodz.dormitoryservice.entity.RoomEntity;

import java.util.List;
import java.util.stream.Collectors;

public class GroupedRoomsMapper {

    public static GroupedRoomsDTO toDto(GroupedRoomsEntity entity) {
        List<RoomInGroupDTO> roomDtos = entity.getRooms().stream()
                .map(GroupedRoomsMapper::toRoomDto)
                .collect(Collectors.toList());

        return new GroupedRoomsDTO(
                entity.getId(),
                entity.getGroupName(),
                roomDtos
        );
    }

    public static RoomInGroupDTO toRoomDto(RoomEntity room) {
        return new RoomInGroupDTO(
                room.getId(),
                room.getNumber(),
                room.getCapacity(),
                room.getFloor(),
                room.isActive(),
                room.getGroupedRooms() != null ? room.getGroupedRooms().getId() : null
        );
    }
}
