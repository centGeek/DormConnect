package pl.lodz.dormConnect.commonRoom.mapper;

import org.springframework.stereotype.Service;
import pl.lodz.dormConnect.commonRoom.dto.CommonRoomAssignmentGetDTO;
import pl.lodz.dormConnect.commonRoom.entity.CommonRoomAssignmentEntity;


import java.util.List;


@Service
public class CommonRoomAssignmentsMapper {
    public CommonRoomAssignmentGetDTO toCommonRoomAssignmentGetDTO(CommonRoomAssignmentEntity commonRoomAssignmentEntity, Long userId) {
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
    public List<CommonRoomAssignmentGetDTO> toCommonRoomAssignmentGetDTOs(List<CommonRoomAssignmentEntity> commonRoomAssignmentEntities, Long userId) {
        return commonRoomAssignmentEntities.stream()
                .sorted((a, b) -> a.getStartDate().compareTo(b.getStartDate())) // Sortowanie po dacie rozpoczęcia
                .map(entity -> toCommonRoomAssignmentGetDTO(entity, userId))
                .toList();
    }

}

