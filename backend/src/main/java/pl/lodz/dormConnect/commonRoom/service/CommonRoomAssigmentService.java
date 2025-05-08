package pl.lodz.dormConnect.commonRoom.service;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.lodz.dormConnect.commonRoom.entity.CommonRoomAssignmentEntity;
import pl.lodz.dormConnect.commonRoom.repositories.CommonRoomAssignmentRepository;
import pl.lodz.dormConnect.commonRoom.repositories.CommonRoomRepository;
import pl.lodz.dormConnect.database.repository.jpa.UserRepository;
import pl.lodz.dormConnect.database.entity.UserEntity;

import java.util.List;

@Service
public class CommonRoomAssigmentService {

    private final CommonRoomAssignmentRepository repository;
    private final CommonRoomRepository commonRoomRepository;
    private final UserRepository userRepository;

    public CommonRoomAssigmentService(CommonRoomAssignmentRepository repository,
                                      CommonRoomRepository commonRoomRepository,
                                      UserRepository userRepository) {
        this.repository = repository;
        this.commonRoomRepository = commonRoomRepository;
        this.userRepository = userRepository;
    }

    public List<CommonRoomAssignmentEntity> getAllCommonRoomAssigments() {
        return repository.findAll();
    }
    public CommonRoomAssignmentEntity getCommonRoomAssigmentById(Long id) {
        return repository.findById(id).orElseThrow(() -> new IllegalArgumentException("Common room assigment not found"));
    }

    public List<CommonRoomAssignmentEntity> getCommonRoomAssigmentsByUserId(Long userId) {
        return repository.findAssignmentsByUserId(userId);
    }

    public List<CommonRoomAssignmentEntity> getCommonRoomAssgimentsByCommonRoomId(Long commonRoomId) {
        List<CommonRoomAssignmentEntity> commonRoomAssigmentEntities = repository.findCommonRoomAssigmentByCommonRoomId(commonRoomId);
        return commonRoomAssigmentEntities;
    }

    @Transactional
    public ResponseEntity<?> joinAssigment(
            Long commonRoomAssigmentId,
            Long userId
    ) {

        CommonRoomAssignmentEntity commonRoomAssignmentEntity = repository.findById(commonRoomAssigmentId).orElseThrow(() -> new IllegalArgumentException("Common room assigment not found"));
        UserEntity user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("User not found"));
        if (commonRoomAssignmentEntity.getUsers().contains(userId)) {
            return ResponseEntity.badRequest().body("User already joined the assigment");
        }
        if (commonRoomAssignmentEntity.getUsers().size()>= commonRoomAssignmentEntity.getCommonRoom().getCapacity()) {
            return ResponseEntity.badRequest().body("Common room assigment is full");
        }
        commonRoomAssignmentEntity.getUsers().add(user);
        repository.save(commonRoomAssignmentEntity);
        return ResponseEntity.ok("User joined the assigment");
    }

    public ResponseEntity<?> leaveAssigment(Long commonRoomAssigmentId, Long userId) {
        CommonRoomAssignmentEntity commonRoomAssignmentEntity = repository.findById(commonRoomAssigmentId).orElseThrow(() -> new IllegalArgumentException("Common room assigment not found"));
        UserEntity user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("User not found"));
        if (!commonRoomAssignmentEntity.getUsers().contains(user)) {
            return ResponseEntity.badRequest().body("User not in the assigment");}
        commonRoomAssignmentEntity.getUsers().remove(user);
        repository.save(commonRoomAssignmentEntity);
        return ResponseEntity.ok("User left the assigment");
    }
}