package pl.lodz.dormitoryservice.dorm.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.lodz.dormitoryservice.common.JwtService;
import pl.lodz.dormitoryservice.dorm.DTO.*;
import pl.lodz.dormitoryservice.dorm.mapper.GroupedRoomsMapper;
import pl.lodz.dormitoryservice.dorm.services.RoomService;
import pl.lodz.dormitoryservice.entity.RoomEntity;


import java.time.LocalDate;
import java.util.List;
import java.util.Map;

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

    @GetMapping("/assign/myAssigns")
    public ResponseEntity<List<AssignmentsDTO>> getMyAssignments(@RequestHeader("Authorization") String authorizationHeader) {
        try {
            Long userId = jwtService.getIdFromToken(authorizationHeader.replace("Bearer ", ""));
            List<AssignmentsDTO> assignments = roomService.getAssignmentsByUserId(userId);
            return ResponseEntity.ok(assignments);
        } catch (Exception e) {
            logger.error("Error retrieving assignments: ", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/assign/{assignmentId}/shorten")
    public ResponseEntity<?> shortenAssignment(
            @PathVariable Long assignmentId,
            @RequestParam("newEndDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate newEndDate,
            @RequestHeader("Authorization") String authorizationHeader
    ) {
        try {
            Long userId = jwtService.getIdFromToken(authorizationHeader.replace("Bearer ", ""));
            roomService.shortenAssignmentEndDate(assignmentId, userId, newEndDate);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            logger.error("Error shortening assignment: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Unexpected error occurred");
        }
    }

    @DeleteMapping("/rooms/{roomId}")
    public ResponseEntity<?> deleteRoom(
            @PathVariable Long roomId,
            @RequestParam(defaultValue = "false") boolean areYouSure,
            @RequestHeader("Authorization") String authorizationHeader
    ) {
        if (!areYouSure) {
            // symulacja – kto będzie przeniesiony i czy się da
            DeleteRoomImpactPreviewDTO simulation =
                    roomService.simulateRoomDeletionImpact(roomId, authorizationHeader);
            return ResponseEntity.status(266)
                    .body(simulation);
        } else {
            //faktyczna operacja usunięcia pokoju + relokacje
            roomService.deleteRoomWithRelocations(roomId);
            return ResponseEntity.ok("Room deleted and residents reassigned successfully.");
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

