package pl.lodz.dormConnect.commonRoom.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.lodz.dormConnect.commonRoom.service.CommonRoomAssigmentService;
import pl.lodz.dormConnect.security.service.JwtService;

@RestController
@RequestMapping("/api/common-room-assignment")
class CommonRoomAssignmentController {
    private final CommonRoomAssigmentService service;
    private final JwtService jwtService;

    @Autowired
    CommonRoomAssignmentController(CommonRoomAssigmentService service, JwtService jwtService) {
        this.service = service;
        this.jwtService = jwtService;
    }

    @GetMapping("/get/{common_room_id}")
    public ResponseEntity<?> assignmentsByCommonRoomId(@PathVariable Long common_room_id, @RequestHeader("Authorization") String authorizationHeader) {
        try {
            Long userId = jwtService.getIdFromToken(authorizationHeader.replace("Bearer ", ""));
            return service.getCommonRoomAssignmentsByCommonRoomId(common_room_id, userId);
        }
        catch(Exception e) {
            return ResponseEntity.status(500).body("Error joining common room assigment: " + e.getMessage());
        }
    }

    @PutMapping("/join/{common_room_assigment_id}")
    public ResponseEntity<?> joinAssigment(
            @PathVariable Long common_room_assigment_id,
            @RequestHeader("Authorization") String authorizationHeader
    ) {
        try {
            Long userId = jwtService.getIdFromToken(authorizationHeader.replace("Bearer ", ""));
            return service.joinAssigment(common_room_assigment_id, userId);
        }
        catch (Exception e) {
            return ResponseEntity.status(500).body("Error joining common room assigment: " + e.getMessage());
        }
    }

    @DeleteMapping("/leave/{common_room_assigment_id}")
    public ResponseEntity<?> leaveAssigment(
            @PathVariable Long common_room_assigment_id,
            @RequestHeader("Authorization") String authorizationHeader
    ) {
        try {
            Long userId = jwtService.getIdFromToken(authorizationHeader.replace("Bearer ", ""));
            return service.leaveAssigment(common_room_assigment_id, userId);
        }
        catch (Exception e) {
            return ResponseEntity.status(500).body("Error leaving common room assigment: " + e.getMessage());
        }
    }



}
