package pl.lodz.dormConnect.floors.service;

import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.lodz.dormConnect.commonRoom.service.CommonRoomService;
import pl.lodz.dormConnect.dorm.services.RoomService;
import pl.lodz.commons.entity.FloorEntity;
import pl.lodz.commons.repository.jpa.FloorsRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class FloorsService {
    private final FloorsRepository floorsRepository;
    private RoomService roomService;
    private CommonRoomService commonRoomService;

    public FloorsService(FloorsRepository floorsRepository) {
        this.floorsRepository = floorsRepository;
    }

    @Autowired
    public void setCommonRoomService(CommonRoomService commonRoomService, RoomService roomService) {
        this.commonRoomService = commonRoomService;
        this.roomService = roomService;
    }

    public List<Integer> getFloors(){
        return floorsRepository.findAll().stream()
                .map(FloorEntity::getFloorNumber)
                .sorted((a, b) -> b.compareTo(a)).toList().reversed();
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
    public void deleteAllRooms(Integer floorNumber){
        FloorEntity floor = floorsRepository.findByFloorNumber(floorNumber);
        for(Long commonRoomId : floor.getCommonRooms()){
            commonRoomService.deleteCommonRoom(commonRoomId);
        }
        for(Long roomId : floor.getRooms()){
            roomService.deleteRoomById(roomId);
        }
    }

    @Transactional
    public void deleteFloor(Integer floorNumber) {
        FloorEntity floor = floorsRepository.findByFloorNumber(floorNumber);
        if (floor != null) {
            deleteAllRooms(floorNumber);
            floorsRepository.delete(floor);
        }
    }
    public void addCommonRoomToFloor(Long commonRoomId, Integer floorNumber) {
        FloorEntity floor = floorsRepository.findByFloorNumber(floorNumber);
        if (floor != null) {
            floor.getCommonRooms().add(commonRoomId);
            floorsRepository.save(floor);
        }
    }
    public void addRoomToFloor(Long roomId, Integer floorNumber) {
        FloorEntity floor = floorsRepository.findByFloorNumber(floorNumber);
        if (floor != null) {
            floor.getRooms().add(roomId);
            floorsRepository.save(floor);
        }
    }
    public FloorEntity getFloorByNumber(Integer floorNumber) {
        return floorsRepository.findByFloorNumber(floorNumber);
    }
    public boolean removeCommonRoomFromFloor(Long commonRoomId, Integer floorNumber) {
        FloorEntity floor = floorsRepository.findByFloorNumber(floorNumber);
        if (floor != null && floor.getCommonRooms().remove(commonRoomId)) {
            floorsRepository.save(floor);
            return true;
        }
        return false;
    }
    public boolean removeRoomFromFloor(Long roomId, Integer floorNumber) {
        FloorEntity floor = floorsRepository.findByFloorNumber(floorNumber);
        if (floor != null && floor.getRooms().remove(roomId)) {
            floorsRepository.save(floor);
            return true;
        }
        return false;
    }
}
