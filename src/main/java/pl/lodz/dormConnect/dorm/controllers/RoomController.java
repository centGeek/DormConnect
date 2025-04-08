package pl.lodz.dormConnect.dorm.controllers;


import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.lodz.dormConnect.dorm.entities.RoomEntity;
import pl.lodz.dormConnect.dorm.services.RoomService;

import java.time.LocalDate;
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


    @PostMapping("/create")
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


    @GetMapping("/{id}")
    public ResponseEntity<RoomEntity> getRoomById(@PathVariable Long id) {
        return roomService.findById(id).map(ResponseEntity::ok).
                orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @Transactional
    @PostMapping("/{id}")
    public ResponseEntity<RoomEntity> updateRoom(@PathVariable Long id, @RequestBody RoomAssignDTO roomAssignDTO) {
        if (roomAssignDTO.roomEntity() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }


        if (roomService.assignStudentToRoom(roomAssignDTO.roomEntity(), roomAssignDTO.idOfStudent(), roomAssignDTO.fromDate(), roomAssignDTO.toDate())) {
            return ResponseEntity.status(HttpStatus.OK).build();
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }


}
