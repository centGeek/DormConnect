package pl.lodz.dormConnect.commonRoom.mapper;

import org.springframework.stereotype.Service;
import pl.lodz.dormConnect.commonRoom.dto.CommonRoomAssignmentGetDTO;
import pl.lodz.dormConnect.commonRoom.entity.CommonRoomAssignmentEntity;
import pl.lodz.dormConnect.database.entity.UserEntity;

import java.util.List;
import java.util.Optional;

@Service
public class CommonRoomAssignmentsMapper {
    public CommonRoomAssignmentGetDTO toCommonRoomAssignmentGetDTO(CommonRoomAssignmentEntity commonRoomAssignmentEntity, Optional<UserEntity> user) {
        boolean isAssigned = commonRoomAssignmentEntity.getUsers().contains(user);
        boolean isFull = commonRoomAssignmentEntity.getUsers().size() >= commonRoomAssignmentEntity.getCommonRoom().getCapacity();
        return new CommonRoomAssignmentGetDTO(
                commonRoomAssignmentEntity.getId(),
                commonRoomAssignmentEntity.getStartDate(),
                commonRoomAssignmentEntity.getEndDate(),
                commonRoomAssignmentEntity.getUsers().size(),
                isAssigned,
                isFull
        );
    }
    public List<CommonRoomAssignmentGetDTO> toCommonRoomAssignmentGetDTOs(List<CommonRoomAssignmentEntity> commonRoomAssignmentEntities, Optional<UserEntity> user) {
        return commonRoomAssignmentEntities.stream()
                .map(entity -> toCommonRoomAssignmentGetDTO(entity, user))
                .toList();
    }

}

