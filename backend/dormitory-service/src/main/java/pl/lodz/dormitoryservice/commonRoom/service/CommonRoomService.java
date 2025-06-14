package pl.lodz.dormitoryservice.commonRoom.service;

import org.springframework.context.annotation.Lazy;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.lodz.dormitoryservice.commonRoom.dto.CommonRoomCreateDTO;
import pl.lodz.dormitoryservice.commonRoom.dto.CommonRoomGetDTO;
import pl.lodz.dormitoryservice.commonRoom.mapper.CommonRoomMapper;
import pl.lodz.dormitoryservice.commonRoom.scheduler.CommonRoomAssignmentScheduler;
import pl.lodz.dormitoryservice.entity.CommonRoomEntity;
import pl.lodz.dormitoryservice.floors.service.FloorsService;
import pl.lodz.dormitoryservice.repository.CommonRoomRepository;

import java.util.List;

@Service
public class CommonRoomService {

    private final CommonRoomRepository repository;
    private final CommonRoomAssignmentScheduler scheduler;
    private final FloorsService floorService;


    public CommonRoomService(CommonRoomRepository repository, CommonRoomAssignmentScheduler scheduler, @Lazy FloorsService floorService) {
        this.repository = repository;
        this.scheduler = scheduler;
        this.floorService = floorService;
    }

    public CommonRoomEntity addCommonRoom(CommonRoomCreateDTO commonRoomCreateDTO) {
        CommonRoomEntity commonRoom = new CommonRoomEntity();
        if (commonRoomCreateDTO.capacity() < 0) {
            throw new IllegalArgumentException("Capacity cannot be negative");
        }
        if (commonRoomCreateDTO.howManyTimesAWeekYouCanUseIt() < 0) {
            throw new IllegalArgumentException("How many times a week you can use it cannot be negative");
        }
        if (commonRoomCreateDTO.maxTimeYouCanStay() < 0) {
            throw new IllegalArgumentException("Max time you can stay cannot be negative");
        }
        if (commonRoomCreateDTO.maxTimeYouCanStay()>4){
            throw new IllegalArgumentException("Max time you can stay cannot be greater than 4");
        }



        CommonRoomEntity savedRoom = repository.save(CommonRoomMapper.toCommonRoomEntity(commonRoomCreateDTO));
        floorService.addCommonRoomToFloor(savedRoom.getId(), savedRoom.getFloor());
        scheduler.createAssignmentsForNextWeek(savedRoom);

        return savedRoom;
    }

    public List<CommonRoomGetDTO> getAllCommonRooms() {
        return CommonRoomMapper.toCommonRoomGetDTOs(repository.findAll());
    }

    @Transactional
    public boolean deleteCommonRoom(Long id) {
        if (repository.existsById(id)) {
            scheduler.deleteAllAssigmentsForRoom(repository.findById(id).get());
            floorService.removeRoomFromFloor(id, repository.findCommonRoomById(id).getFloor());
            repository.deleteById(id);
            return true;
        }
        return false;
    }

    public CommonRoomEntity updateActiveStatus(Long id, boolean active) {
        CommonRoomEntity commonRoom = repository.findById(id).orElseThrow(() -> new IllegalArgumentException("Common room not found"));
        commonRoom.setActive(active);
        return repository.save(commonRoom);
    }

    public List<CommonRoomEntity> getRoomByFloor(int floor) {
        return repository.findByFloor(floor);
    }
    public CommonRoomGetDTO getCommonRoomById(Long id) {
        return CommonRoomMapper.toCommonRoomGetDTO(repository.findById(id).orElseThrow(() -> new IllegalArgumentException("Common room not found")));
    }

    public List<String> getCommonRoomTypes() {
        return List.of(CommonRoomEntity.CommonRoomType.values()).stream()
                .map(Enum::name)
                .toList();
    }

    public List<Integer> getFloors() {
        return repository.findAll().stream()
                .map(CommonRoomEntity::getFloor)
                .distinct()
                .toList();
    }

    public ResponseEntity<?> resetAssignmentsForNextWeek(Long commonRoomId) {
        CommonRoomEntity commonRoom = repository.findById(commonRoomId).orElseThrow(() -> new IllegalArgumentException("Common room not found"));
        scheduler.deleteAllAssigmentsForRoom(commonRoom);
        scheduler.createAssignmentsForNextWeek(commonRoom);
        return ResponseEntity.ok("Assignments for next week reset successfully");
    }
}