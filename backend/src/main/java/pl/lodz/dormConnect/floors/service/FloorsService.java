package pl.lodz.dormConnect.floors.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.lodz.dormConnect.commonRoom.repositories.CommonRoomRepository;
import pl.lodz.dormConnect.commonRoom.service.CommonRoomService;
import pl.lodz.dormConnect.dorm.services.RoomService;
import pl.lodz.dormConnect.floors.entity.FloorEntity;
import pl.lodz.dormConnect.floors.repositories.FloorsRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class FloorsService {
    private final FloorsRepository floorsRepository;
    private final CommonRoomService commonRoomService;
    private final RoomService roomService;

    public FloorsService(FloorsRepository floorsRepository, CommonRoomService commonRoomService, RoomService roomService) {
        this.floorsRepository = floorsRepository;
        this.commonRoomService = commonRoomService;
        this.roomService = roomService;
    }

    public List<Integer> getFloors(){
        return floorsRepository.findAll().stream()
                .map(FloorEntity::getFloorNumber)
                .toList();
    }

    public FloorEntity addFloor(){
        int floorNumber = getFloors().stream().max(Integer::compare).orElse(-1) + 1;
        FloorEntity floor = new FloorEntity();
        floor.setFloorNumber(floorNumber);
        floor.setRooms(new ArrayList<>());
        floor.setCommonRooms(new ArrayList<>());
        floorsRepository.save(floor);
        return floor;
    }

    @Transactional
    public boolean deleteFloor(Long floor_id) {
        FloorEntity floor = floorsRepository.findById(floor_id).orElse(null);
        if (floor != null) {
            for (Long room_id : floor.getRooms()){
                roomService.deleteRoomById(room_id);
            }
            for (Long commonRoom_id : floor.getCommonRooms()){
                commonRoomService.deleteCommonRoom(commonRoom_id);
            }
            floorsRepository.delete(floor);
            return true;
        }
        return false;
    }
}
