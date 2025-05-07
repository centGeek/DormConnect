package pl.lodz.dormConnect.commonRoom.service;

import org.springframework.stereotype.Service;
import pl.lodz.dormConnect.commonRoom.dto.CommonRoomCreateDTO;
import pl.lodz.dormConnect.commonRoom.entity.CommonRoom;
import pl.lodz.dormConnect.commonRoom.entity.CommonRoomAssigment;
import pl.lodz.dormConnect.commonRoom.repositories.CommonRoomAssigmentRepository;
import pl.lodz.dormConnect.commonRoom.repositories.CommonRoomRepository;
import pl.lodz.dormConnect.commonRoom.scheduler.CommonRoomAssignmentScheduler;

import java.util.List;

@Service
public class CommonRoomAssigmentService {

    private final CommonRoomAssigmentRepository repository;

    public CommonRoomAssigmentService(CommonRoomAssigmentRepository repository) {
        this.repository = repository;
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
}