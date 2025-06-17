package pl.lodz.dormitoryservice.commonRoom.service;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.lodz.dormitoryservice.commonRoom.dto.CommonRoomAssignmentGetDTO;
import pl.lodz.dormitoryservice.commonRoom.mapper.CommonRoomAssignmentsMapper;
import pl.lodz.dormitoryservice.entity.CommonRoomAssignmentEntity;
import pl.lodz.dormitoryservice.repository.CommonRoomAssignmentRepository;
import pl.lodz.dormitoryservice.repository.CommonRoomRepository;
import pl.lodz.dormitoryservice.repository.RoomAssignmentRepository;
import pl.lodz.dormitoryservice.repository.RoomRepository;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;


@Service
public class CommonRoomAssigmentService {

    private final CommonRoomAssignmentRepository repository;
    private final CommonRoomRepository commonRoomRepository;
    private final RoomRepository roomRepository;
    private final RoomAssignmentRepository roomAssignmentRepository;

    public CommonRoomAssigmentService(CommonRoomAssignmentRepository repository,
                                      CommonRoomRepository commonRoomRepository, RoomRepository roomRepository, RoomAssignmentRepository roomAssignmentRepository) {
        this.repository = repository;
        this.commonRoomRepository = commonRoomRepository;
        this.roomRepository = roomRepository;
        this.roomAssignmentRepository = roomAssignmentRepository;
    }


    public ResponseEntity<List<CommonRoomAssignmentGetDTO>> getCommonRoomAssignmentsByCommonRoomId(Long commonRoomId, Long userId) {
        List<CommonRoomAssignmentEntity> commonRoomAssigmentEntities = repository.getAssignmentsByCommonRoomId(commonRoomId);
        List<CommonRoomAssignmentGetDTO> dtos = CommonRoomAssignmentsMapper.toCommonRoomAssignmentGetDTOs(commonRoomAssigmentEntities, userId);
        return ResponseEntity.ok(dtos);
    }

    @Transactional
    public ResponseEntity<?> joinAssigment(
            Long commonRoomAssigmentId,
            Long userId
    ) {

        CommonRoomAssignmentEntity commonRoomAssignmentEntity = repository.findById(commonRoomAssigmentId).orElseThrow(() -> new IllegalArgumentException("Common room assigment not found"));
//        LocalDate startDate = commonRoomAssignmentEntity.getStartDate()
//                .toInstant()
//                .atZone(ZoneId.systemDefault())
//                .toLocalDate();
//
//        if (!roomAssignmentRepository.existsAssignmentForStudentDuring(userId, startDate, startDate)) {
//            return ResponseEntity.badRequest().body("User does not have any room assigned at this day");
//        }
        if (commonRoomAssignmentEntity.getUsersId().contains(userId)) {
            return ResponseEntity.badRequest().body("User already joined the assigment");
        }
        if (commonRoomAssignmentEntity.getUsersId().size() >= commonRoomAssignmentEntity.getCommonRoom().getCapacity()) {
            return ResponseEntity.badRequest().body("Common room assigment is full");
        }
        int assignmentsUserHas = 0;
        for (CommonRoomAssignmentEntity assignment : repository.getAssignmentsByCommonRoomId(commonRoomAssignmentEntity.getCommonRoom().getId())) {
            if (assignment.getUsersId().contains(userId)) {
                assignmentsUserHas++;
            }
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
            return ResponseEntity.badRequest().body("User not in the assigment");
        }
        commonRoomAssignmentEntity.getUsersId().remove(userId);
        repository.save(commonRoomAssignmentEntity);
        return ResponseEntity.ok("User left the assigment");
    }

    @Transactional
    public void exchangeAssignment(Long commonRoomAssignemntId, Long userGiveId, Long userReceiveId) {
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