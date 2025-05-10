package pl.lodz.dormConnect.commonRoom.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.lodz.dormConnect.commonRoom.dto.CommonRoomCreateDTO;
import pl.lodz.dormConnect.commonRoom.dto.CommonRoomGetDTO;
import pl.lodz.dormConnect.commonRoom.entity.CommonRoomEntity;
import pl.lodz.dormConnect.commonRoom.service.CommonRoomService;

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
    public List<CommonRoomEntity> showCommonRooms(@PathVariable Long floor) {
        return service.getRoomByFloor(floor.intValue());
    }


}
