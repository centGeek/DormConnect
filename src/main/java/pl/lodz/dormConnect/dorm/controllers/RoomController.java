package pl.lodz.dormConnect.dorm.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.lodz.dormConnect.dorm.entities.RoomEntity;
import pl.lodz.dormConnect.dorm.services.RoomService;

import java.util.List;

@RestController
@RequestMapping("/dorm/room")
public class RoomController {

    @Autowired
    private final RoomService roomService;

    @Autowired
    public RoomController(RoomService roomService) {
        this.roomService = roomService;
    }

    @GetMapping("")
    public List<RoomEntity> getRooms() {
        return roomService.getAllRooms();
    }


    @PostMapping("")
    public ResponseEntity<RoomEntity> addRoom(@RequestBody RoomEntity roomEntity) {
        RoomEntity createdRoom = roomService.addRoom(roomEntity);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdRoom);

    }

    @DeleteMapping("")
    public ResponseEntity<RoomEntity> deleteRoom(@RequestBody Long id) {
        try {
            roomService.deleteRoomById(id);
            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

}
