package pl.lodz.dormitoryservice.floors.controllers;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.lodz.dormitoryservice.entity.FloorEntity;
import pl.lodz.dormitoryservice.floors.service.FloorsService;


import java.util.List;


@RestController
@RequestMapping("/api/floors")
public class FloorsController {
    private final FloorsService floorsService;

    public FloorsController(FloorsService floorsService) {
        this.floorsService = floorsService;
    }

    @GetMapping("/get")
    public ResponseEntity<List<Integer>> getFloors(){
        List<Integer> floors = floorsService.getFloors();
        return ResponseEntity.ok(floors);
    }

    @PostMapping("/add")
    public ResponseEntity<FloorEntity> addFloor() {
        return ResponseEntity.status(HttpStatus.CREATED).body(floorsService.addFloor());
    }
    @DeleteMapping("/delete-rooms/{floorNumber}")
    public ResponseEntity<Void> deleteAllRooms(@PathVariable Integer floorNumber) {
        try {
            floorsService.deleteAllRooms(floorNumber);
            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
    @DeleteMapping("/delete-floor/{floorNumber}")
    public ResponseEntity<Void> deleteFloor(@PathVariable Integer floorNumber) {
        try {
            floorsService.deleteFloor(floorNumber);
            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
    @PostMapping("/add-common-room/{commonRoomId}/{floorNumber}")
    public ResponseEntity<Void> addCommonRoomToFloor(@PathVariable Long commonRoomId, @PathVariable Integer floorNumber) {
        try {
            floorsService.addCommonRoomToFloor(commonRoomId, floorNumber);
            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }


}
