package pl.lodz.dormConnect.schedule.service;

import org.springframework.stereotype.Service;
import pl.lodz.dormConnect.schedule.dto.CommonRoomCreateDTO;
import pl.lodz.dormConnect.schedule.entity.CommonRoom;
import pl.lodz.dormConnect.schedule.repositories.CommonRoomRepository;
import java.util.List;

@Service
public class CommonRoomService {

    private final CommonRoomRepository repository;

    public CommonRoomService(CommonRoomRepository repository) {
        this.repository = repository;
    }

    public CommonRoom addCommonRoom(CommonRoomCreateDTO commonRoomCreateDTO) {
        CommonRoom commonRoom = new CommonRoom();
        if (commonRoomCreateDTO.capacity()<0){
            throw new IllegalArgumentException("Capacity cannot be negative");
        }
        if (commonRoomCreateDTO.howManyTimesAWeekYouCanUseIt()<0){
            throw new IllegalArgumentException("How many times a week you can use it cannot be negative");
        }
        if (commonRoomCreateDTO.maxTimeYouCanStay()<0){
            throw new IllegalArgumentException("Max time you can stay cannot be negative");
        }
        commonRoom.setType(commonRoomCreateDTO.type());
        commonRoom.setCapacity(commonRoomCreateDTO.capacity());
        commonRoom.setFloor(commonRoomCreateDTO.floor());
        commonRoom.setMaxTimeYouCanStay(commonRoomCreateDTO.maxTimeYouCanStay());
        commonRoom.setHowManyTimesAWeekYouCanUseIt(commonRoomCreateDTO.howManyTimesAWeekYouCanUseIt());
        commonRoom.setActive(commonRoomCreateDTO.active());
        return repository.save(commonRoom);
    }

    public List<CommonRoom> getAllCommonRooms() {
        return repository.findAll();
    }

    public boolean deleteCommonRoom(Long id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
            return true;
        }
        return false;
    }

    public CommonRoom updateActiveStatus(Long id, boolean active) {
        CommonRoom commonRoom = repository.findById(id).orElseThrow(() -> new IllegalArgumentException("Common room not found"));
        commonRoom.setActive(active);
        return repository.save(commonRoom);
    }

    public List<CommonRoom> getRoomByFloor(int floor) {
        return repository.findByFloor(floor);
    }
}
