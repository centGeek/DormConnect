package pl.lodz.dormitoryservice.commonRoom.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.lodz.dormitoryservice.commonRoom.dto.CommonRoomCreateDTO;
import pl.lodz.dormitoryservice.commonRoom.dto.CommonRoomGetDTO;
import pl.lodz.dormitoryservice.commonRoom.service.CommonRoomService;
import pl.lodz.dormitoryservice.entity.CommonRoomEntity;

import java.util.List;

@RestController
@RequestMapping("/api/common-room")
class CommonRoomController {
    private final CommonRoomService service;

    public CommonRoomController(CommonRoomService service) {
        this.service = service;
    }

    @PostMapping("/add")
    public ResponseEntity<CommonRoomEntity> addCommonRoom(@RequestBody CommonRoomCreateDTO commonRoomCreateDTO) {
        CommonRoomEntity newRoom = service.addCommonRoom(commonRoomCreateDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(newRoom);
    }


    @GetMapping("/show")
    public List<CommonRoomGetDTO> showCommonRooms() {
        return service.getAllCommonRooms();
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteCommonRoom(@PathVariable Long id) {
        if (service.deleteCommonRoom(id)) {
            return ResponseEntity.status(HttpStatus.OK).build();
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<CommonRoomEntity> updateActiveStatus(@PathVariable Long id, @RequestParam boolean active) {
        try {
            CommonRoomEntity updatedRoom = service.updateActiveStatus(id, active);
            return ResponseEntity.ok(updatedRoom);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping("/show/{floor}")
    public List<CommonRoomEntity> showCommonRoomsByFloor(@PathVariable Long floor) {
        return service.getRoomByFloor(floor.intValue());
    }
    @GetMapping("/get/{id}")
    public ResponseEntity<CommonRoomGetDTO> showCommonRoom(@PathVariable Long id) {
        CommonRoomGetDTO commonRoom = service.getCommonRoomById(id);
        if (commonRoom != null) {
            return ResponseEntity.ok(commonRoom);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
    @GetMapping("/get/enum")
    public ResponseEntity<List<String>> getCommonRoomTypes() {
        List<String> commonRoomTypes = service.getCommonRoomTypes();
        return ResponseEntity.ok(commonRoomTypes);
    }
    @GetMapping("/floors")
    public ResponseEntity<List<Integer>> getFloors() {
        List<Integer> floors = service.getFloors();
        return ResponseEntity.ok(floors);
    }

    @PutMapping("/reset-assignments/{commonRoomId}")
    public ResponseEntity<?> resetAssignmentsForNextWeek(@PathVariable Long commonRoomId) {
        try {
            return service.resetAssignmentsForNextWeek(commonRoomId);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Common room not found");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error resetting assignments: " + e.getMessage());
        }
    }

}