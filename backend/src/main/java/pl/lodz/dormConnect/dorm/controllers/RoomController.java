package pl.lodz.dormConnect.dorm.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.lodz.dormConnect.dorm.DTO.RoomAssignDTO;
import pl.lodz.dormConnect.dorm.DTO.RoomInGroupDTO;
import pl.lodz.dormConnect.dorm.DTO.RoomUpdateDTO;
import pl.lodz.dormConnect.dorm.entities.RoomEntity;
import pl.lodz.dormConnect.dorm.mapper.GroupedRoomsMapper;
import pl.lodz.dormConnect.dorm.services.RoomService;
import pl.lodz.dormConnect.security.service.JwtService;

import java.util.List;

@RestController
@RequestMapping("/api/dorm")
public class RoomController {

    @Autowired
    private final RoomService roomService;

    @Autowired
    private final JwtService jwtService;

    private static final Logger logger = LoggerFactory.getLogger(RoomController.class);

    @Autowired
    public RoomController(RoomService roomService, JwtService jwtService) {
        this.roomService = roomService;
        this.jwtService = jwtService;
    }

    @GetMapping("/room")
    public List<RoomInGroupDTO> getRooms() {
        return roomService.getAllRooms().stream().map(GroupedRoomsMapper::toRoomDto).toList();
    }
    @GetMapping("/room/floor/{floor}")
    public List<RoomInGroupDTO> getRoomsByFloor(@PathVariable Integer floor) {
        return roomService.getRoomsByFloor(floor).stream().map(GroupedRoomsMapper::toRoomDto).toList();
    }


    @PostMapping("/room/create")
    public ResponseEntity<RoomEntity> addRoom(@RequestBody RoomEntity roomEntity) {
        RoomEntity createdRoom = roomService.addRoom(roomEntity);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdRoom);
    }

    @DeleteMapping("/room/{id}")
    public ResponseEntity<RoomEntity> deleteRoom(@PathVariable Long id) {
        try {
            roomService.deleteRoomById(id);
            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }


    @GetMapping("/room/{id}")
    public ResponseEntity<RoomInGroupDTO> getRoomById(@PathVariable Long id) {
        return roomService.findById(id)
                .map(room -> new RoomInGroupDTO(
                        room.getId(),
                        room.getNumber(),
                        room.getCapacity(),
                        room.getFloor(),
                        room.isActive(),
                        room.getGroupedRooms() != null ? room.getGroupedRooms().getId() : null
                ))
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @PatchMapping("/room/{id}")
    public ResponseEntity<RoomEntity> updateRoomPartial(
            @PathVariable Long id,
            @RequestBody RoomUpdateDTO updateDTO) {
        RoomEntity updatedRoom = roomService.updateRoomPartial(id, updateDTO.name(), updateDTO.floor());
        return ResponseEntity.ok(updatedRoom);
    }


    @Transactional
    @PostMapping("/room/{id}/assign")
    public ResponseEntity<RoomEntity> assignRoomToPerson(@PathVariable Long id, @RequestBody RoomAssignDTO roomAssignDTO) {
        if (roomAssignDTO.roomEntity() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        if (roomService.assignStudentToRoom(roomAssignDTO.roomEntity(), roomAssignDTO.idOfStudent(), roomAssignDTO.fromDate(), roomAssignDTO.toDate())) {
            return ResponseEntity.status(HttpStatus.OK).build();
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }


//    @GetMapping("/assign/myAssigns")
//    public ResponseEntity<List<AssignmentsDTO>> getMyAssignments(@RequestHeader("Authorization") String authorizationHeader) {
//        try {
//            Long userId = jwtService.getIdFromToken(authorizationHeader.replace("Bearer ", ""));
//            List<AssignmentsDTO> assignments = roomService.getAssignmentsByUserId(userId);
//            return ResponseEntity.ok(assignments);
//        } catch (Exception e) {
//            logger.error("Error retrieving assignments: ", e);
//            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }
}
