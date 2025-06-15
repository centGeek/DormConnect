package pl.lodz.dormitoryservice.floors.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.lodz.dormitoryservice.commonRoom.service.CommonRoomService;
import pl.lodz.dormitoryservice.dorm.services.RoomService;
import pl.lodz.dormitoryservice.entity.FloorEntity;
import pl.lodz.dormitoryservice.repository.FloorsRepository;

import java.util.ArrayList;
import java.util.Comparator;
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
                .sorted(Comparator.reverseOrder()).toList().reversed();
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
        if (floor.getCommonRooms() != null) {
            for(Long commonRoomId : floor.getCommonRooms()){
                commonRoomService.deleteCommonRoom(commonRoomId);
            }
        }
        if (floor.getRooms() != null) {
            for(Long roomId : floor.getRooms()){
                roomService.deleteRoomById(roomId);
            }
        }
    }

    @Transactional
    public ResponseEntity<String> deleteFloor(Integer floorNumber) {
        FloorEntity floor = floorsRepository.findByFloorNumber(floorNumber);
        if (floor == null) {
            return ResponseEntity.status(404).body("Floor " + floorNumber + " not found.");
        }
        if(floor.getFloorNumber() == getFloors().stream().max(Integer::compare).orElse(-1)){
            deleteAllRooms(floorNumber);
            floorsRepository.delete(floor);
            return ResponseEntity.ok("Floor " + floorNumber + " deleted successfully.");
        }
        else{
            return ResponseEntity.status(400).body("Cannot delete floor " + floorNumber + " because it is not the last floor.");
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
