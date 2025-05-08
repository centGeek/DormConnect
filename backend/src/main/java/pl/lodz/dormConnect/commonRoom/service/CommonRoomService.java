package pl.lodz.dormConnect.commonRoom.service;

import org.springframework.stereotype.Service;
import pl.lodz.dormConnect.commonRoom.dto.CommonRoomDTO;
import pl.lodz.dormConnect.commonRoom.entity.CommonRoomEntity;
import pl.lodz.dormConnect.commonRoom.repositories.CommonRoomRepository;
import pl.lodz.dormConnect.commonRoom.scheduler.CommonRoomAssignmentScheduler;

import java.util.List;

@Service
public class CommonRoomService {

    private final CommonRoomRepository repository;
    private final CommonRoomAssignmentScheduler scheduler;

    public CommonRoomService(CommonRoomRepository repository, CommonRoomAssignmentScheduler scheduler) {
        this.repository = repository;
        this.scheduler = scheduler;
    }

    public CommonRoomEntity addCommonRoom(CommonRoomDTO commonRoomDTO) {
        CommonRoomEntity commonRoom = new CommonRoomEntity();
        if (commonRoomDTO.capacity() < 0) {
            throw new IllegalArgumentException("Capacity cannot be negative");
        }
        if (commonRoomDTO.howManyTimesAWeekYouCanUseIt() < 0) {
            throw new IllegalArgumentException("How many times a week you can use it cannot be negative");
        }
        if (commonRoomDTO.maxTimeYouCanStay() < 0) {
            throw new IllegalArgumentException("Max time you can stay cannot be negative");
        }
        commonRoom.setCommonRoomType(commonRoomDTO.type());
        commonRoom.setCapacity(commonRoomDTO.capacity());
        commonRoom.setFloor(commonRoomDTO.floor());
        commonRoom.setHoursOfTimeWindows(commonRoomDTO.maxTimeYouCanStay());
        commonRoom.setHowManyTimesAWeekYouCanUseIt(commonRoomDTO.howManyTimesAWeekYouCanUseIt());
        commonRoom.setActive(commonRoomDTO.active());

        CommonRoomEntity savedRoom = repository.save(commonRoom);

        // Wywołanie metody tworzącej przypisania
        scheduler.createAssignmentsForNextWeek(savedRoom);

        return savedRoom;
    }

    public List<CommonRoomEntity> getAllCommonRooms() {
        return repository.findAll();
    }

    public boolean deleteCommonRoom(Long id) {
        if (repository.existsById(id)) {
            scheduler.deleteAllActiveAssigmentsForRoom(repository.findById(id).get());
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
}