package pl.lodz.dormConnect.commonRoom.service;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.lodz.dormConnect.commonRoom.dto.CommonRoomAssignmentGetDTO;
import pl.lodz.dormConnect.commonRoom.entity.CommonRoomAssignmentEntity;
import pl.lodz.dormConnect.commonRoom.entity.CommonRoomEntity;
import pl.lodz.dormConnect.commonRoom.mapper.CommonRoomAssignmentsMapper;
import pl.lodz.dormConnect.commonRoom.repositories.CommonRoomAssignmentRepository;
import pl.lodz.dormConnect.commonRoom.repositories.CommonRoomRepository;

import java.util.List;


@Service
public class CommonRoomAssigmentService {

    private final CommonRoomAssignmentRepository repository;
    private final CommonRoomRepository commonRoomRepository;
    private final CommonRoomAssignmentsMapper mapper;

    public CommonRoomAssigmentService(CommonRoomAssignmentRepository repository,
                                      CommonRoomRepository commonRoomRepository,
                                      CommonRoomAssignmentsMapper mapper) {
        this.repository = repository;
        this.commonRoomRepository = commonRoomRepository;
        this.mapper = mapper;
    }


    public ResponseEntity<List<CommonRoomAssignmentGetDTO>> getCommonRoomAssignmentsByCommonRoomId(Long commonRoomId, Long userId) {
        List<CommonRoomAssignmentEntity> commonRoomAssigmentEntities = repository.getAssignmentsByCommonRoomId(commonRoomId);
        List<CommonRoomAssignmentGetDTO> dtos = mapper.toCommonRoomAssignmentGetDTOs(commonRoomAssigmentEntities, userId);
        return ResponseEntity.ok(dtos);
    }

    @Transactional
    public ResponseEntity<?> joinAssigment(
            Long commonRoomAssigmentId,
            Long userId
    ) {

        CommonRoomAssignmentEntity commonRoomAssignmentEntity = repository.findById(commonRoomAssigmentId).orElseThrow(() -> new IllegalArgumentException("Common room assigment not found"));
        if (commonRoomAssignmentEntity.getUsersId().contains(userId)) {
            return ResponseEntity.badRequest().body("User already joined the assigment");
        }
        if (commonRoomAssignmentEntity.getUsersId().size()>= commonRoomAssignmentEntity.getCommonRoom().getCapacity()) {
            return ResponseEntity.badRequest().body("Common room assigment is full");
        }
        int assignmentsUserHas = 0;
        for (CommonRoomAssignmentEntity assignment : repository.getAssignmentsByCommonRoomId(commonRoomAssignmentEntity.getCommonRoom().getId())) {
            if (assignment.getUsersId().contains(userId)) {assignmentsUserHas++;}
        }
        if (assignmentsUserHas >= commonRoomAssignmentEntity.getCommonRoom().getHowManyTimesAWeekYouCanUseIt()) {
                return ResponseEntity.badRequest().body("User has too many assignments in this week");
        }
        commonRoomAssignmentEntity.getUsersId().add(userId);
        repository.save(commonRoomAssignmentEntity);
        return ResponseEntity.ok("User joined the assigment");
    }

    public ResponseEntity<?> leaveAssigment(Long commonRoomAssigmentId, Long userId) {
        CommonRoomAssignmentEntity commonRoomAssignmentEntity = repository.findById(commonRoomAssigmentId).orElseThrow(() -> new IllegalArgumentException("Common room assigment not found"));
        if (!commonRoomAssignmentEntity.getUsersId().contains(userId)) {
            return ResponseEntity.badRequest().body("User not in the assigment");}
        commonRoomAssignmentEntity.getUsersId().remove(userId);
        repository.save(commonRoomAssignmentEntity);
        return ResponseEntity.ok("User left the assigment");
    }

    @Transactional
    public void exchangeAssignment(Long commonRoomAssignemntId, Long userGiveId, Long userReceiveId){
        CommonRoomAssignmentEntity commonRoomAssignement = repository.findById(commonRoomAssignemntId).orElseThrow(() -> new IllegalArgumentException("Common room assigment not found"));
        if (!commonRoomAssignement.getUsersId().contains(userGiveId)) {
            throw new IllegalArgumentException("User not in the assigment");
        }
        if (commonRoomAssignement.getUsersId().contains(userReceiveId)) {
            throw new IllegalArgumentException("User already in the assigment");
        }
        commonRoomAssignement.getUsersId().remove(userGiveId);
        commonRoomAssignement.getUsersId().add(userReceiveId);
        repository.save(commonRoomAssignement);
    }

}