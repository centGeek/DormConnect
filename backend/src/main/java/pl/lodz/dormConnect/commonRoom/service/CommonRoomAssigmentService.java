package pl.lodz.dormConnect.commonRoom.service;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.lodz.dormConnect.commonRoom.dto.CommonRoomCreateDTO;
import pl.lodz.dormConnect.commonRoom.entity.CommonRoom;
import pl.lodz.dormConnect.commonRoom.entity.CommonRoomAssigment;
import pl.lodz.dormConnect.commonRoom.repositories.CommonRoomAssigmentRepository;
import pl.lodz.dormConnect.commonRoom.repositories.CommonRoomRepository;
import pl.lodz.dormConnect.commonRoom.scheduler.CommonRoomAssignmentScheduler;
import pl.lodz.dormConnect.database.repository.jpa.UserRepository;
import pl.lodz.dormConnect.database.entity.UserEntity;

import java.util.List;

@Service
public class CommonRoomAssigmentService {

    private final CommonRoomAssigmentRepository repository;
    private final CommonRoomRepository commonRoomRepository;
    private final UserRepository userRepository;

    public CommonRoomAssigmentService(CommonRoomAssigmentRepository repository,
                                      CommonRoomRepository commonRoomRepository,
                                      UserRepository userRepository) {
        this.repository = repository;
        this.commonRoomRepository = commonRoomRepository;
        this.userRepository = userRepository;
    }

    public List<CommonRoomAssigment> getAllCommonRoomAssigments() {
        return repository.findAll();
    }
    public CommonRoomAssigment getCommonRoomAssigmentById(Long id) {
        return repository.findById(id).orElseThrow(() -> new IllegalArgumentException("Common room assigment not found"));
    }

    public List<CommonRoomAssigment> getCommonRoomAssigmentsByUserId(Long userId) {
        return repository.findAssignmentsByUserId(userId);
    }

    public List<CommonRoomAssigment> getCommonRoomAssgimentsByCommonRoomId(Long commonRoomId) {
        List<CommonRoomAssigment> commonRoomAssigments = repository.findCommonRoomAssigmentByCommonRoomId(commonRoomId);
        return commonRoomAssigments;
    }

    @Transactional
    public ResponseEntity<?> joinAssigment(
            Long commonRoomAssigmentId,
            Long userId
    ) {

        CommonRoomAssigment commonRoomAssigment = repository.findById(commonRoomAssigmentId).orElseThrow(() -> new IllegalArgumentException("Common room assigment not found"));
        UserEntity user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("User not found"));
        if (commonRoomAssigment.getUsers().contains(userId)) {
            return ResponseEntity.badRequest().body("User already joined the assigment");
        }
        if (commonRoomAssigment.getUsers().size()>= commonRoomAssigment.getCommonRoom().getCapacity()) {
            return ResponseEntity.badRequest().body("Common room assigment is full");
        }
        commonRoomAssigment.getUsers().add(user);
        repository.save(commonRoomAssigment);
        return ResponseEntity.ok("User joined the assigment");
    }

    public ResponseEntity<?> leaveAssigment(Long commonRoomAssigmentId, Long userId) {
        CommonRoomAssigment commonRoomAssigment = repository.findById(commonRoomAssigmentId).orElseThrow(() -> new IllegalArgumentException("Common room assigment not found"));
        UserEntity user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("User not found"));
        if (!commonRoomAssigment.getUsers().contains(user)) {
            return ResponseEntity.badRequest().body("User not in the assigment");}
        commonRoomAssigment.getUsers().remove(user);
        repository.save(commonRoomAssigment);
        return ResponseEntity.ok("User left the assigment");
    }
}