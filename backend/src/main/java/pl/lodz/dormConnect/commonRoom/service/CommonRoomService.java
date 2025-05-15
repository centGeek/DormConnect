package pl.lodz.dormConnect.commonRoom.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.lodz.dormConnect.commonRoom.dto.CommonRoomCreateDTO;
import pl.lodz.dormConnect.commonRoom.dto.CommonRoomGetDTO;
import pl.lodz.dormConnect.commonRoom.entity.CommonRoomEntity;
import pl.lodz.dormConnect.commonRoom.repositories.CommonRoomRepository;
import pl.lodz.dormConnect.commonRoom.scheduler.CommonRoomAssignmentScheduler;
import pl.lodz.dormConnect.commonRoom.mapper.CommonRoomMapper;

import java.util.List;

@Service
public class CommonRoomService {

    private final CommonRoomRepository repository;
    private final CommonRoomAssignmentScheduler scheduler;
    private final CommonRoomMapper mapper;


    public CommonRoomService(CommonRoomRepository repository, CommonRoomAssignmentScheduler scheduler, CommonRoomMapper mapper) {
        this.repository = repository;
        this.scheduler = scheduler;
        this.mapper = mapper;
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



        CommonRoomEntity savedRoom = repository.save(mapper.toCommonRoomEntity(commonRoomCreateDTO));

        scheduler.createAssignmentsForNextWeek(savedRoom);

        return savedRoom;
    }

    public List<CommonRoomGetDTO> getAllCommonRooms() {
        return mapper.toCommonRoomGetDTOs(repository.findAll());
    }

    @Transactional
    public boolean deleteCommonRoom(Long id) {
        if (repository.existsById(id)) {
            scheduler.deleteAllAssigmentsForRoom(repository.findById(id).get());
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
        return mapper.toCommonRoomGetDTO(repository.findById(id).orElseThrow(() -> new IllegalArgumentException("Common room not found")));
    }

    public List<String> getCommonRoomTypes() {
        return List.of(CommonRoomEntity.CommonRoomType.values()).stream()
                .map(Enum::name)
                .toList();
    }
}