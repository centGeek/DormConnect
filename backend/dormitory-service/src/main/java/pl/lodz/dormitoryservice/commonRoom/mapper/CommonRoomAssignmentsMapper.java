package pl.lodz.dormitoryservice.commonRoom.mapper;

import pl.lodz.dormitoryservice.commonRoom.dto.CommonRoomAssignmentGetDTO;
import pl.lodz.dormitoryservice.entity.CommonRoomAssignmentEntity;

import java.util.List;


public class CommonRoomAssignmentsMapper {
    public static CommonRoomAssignmentGetDTO toCommonRoomAssignmentGetDTO(CommonRoomAssignmentEntity commonRoomAssignmentEntity, Long userId) {
        boolean isAssigned = commonRoomAssignmentEntity.getUsersId().contains(userId);
        boolean isFull = commonRoomAssignmentEntity.getUsersId().size() >= commonRoomAssignmentEntity.getCommonRoom().getCapacity();
        return new CommonRoomAssignmentGetDTO(
                commonRoomAssignmentEntity.getId(),
                commonRoomAssignmentEntity.getStartDate(),
                commonRoomAssignmentEntity.getEndDate(),
                commonRoomAssignmentEntity.getUsersId().size(),
                isAssigned,
                isFull,
                commonRoomAssignmentEntity.isArchived()
        );
    }
    public static List<CommonRoomAssignmentGetDTO> toCommonRoomAssignmentGetDTOs(List<CommonRoomAssignmentEntity> commonRoomAssignmentEntities, Long userId) {
        return commonRoomAssignmentEntities.stream()
                .sorted((a, b) -> a.getStartDate().compareTo(b.getStartDate()))
                .map(entity -> toCommonRoomAssignmentGetDTO(entity, userId))
                .toList();
    }

}

