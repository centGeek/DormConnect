package pl.lodz.dormConnect.events.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.lodz.dormConnect.events.dto.EventDTO;
import pl.lodz.dormConnect.events.service.EventParticipantService;
import pl.lodz.dormConnect.security.service.JwtService;


@RestController
@RequestMapping("/api/event/participant")
public class EventParticipantController {
    private static final Logger logger = LoggerFactory.getLogger(EventParticipantController.class);

    private final EventParticipantService eventParticipantService;

    private final JwtService jwtService;

    @Autowired
    public EventParticipantController(EventParticipantService eventParticipantService, JwtService jwtService) {
        this.eventParticipantService = eventParticipantService;
        this.jwtService = jwtService;
    }

    @PostMapping("/{eventId}")
    public ResponseEntity<EventDTO> joinEvent(
            @PathVariable Long eventId,
            @RequestHeader("Authorization") String authorizationHeader
    ) {
        try {
            Long participantId = jwtService.getIdFromToken(authorizationHeader.replace("Bearer ", ""));
            return eventParticipantService.joinEvent(eventId, participantId)
                    .map(ResponseEntity::ok)
                    .orElseGet(() -> ResponseEntity.status(HttpStatus.CONFLICT).build());
        } catch (Exception e) {
            logger.error("Error joining event: ", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{eventId}")
    public ResponseEntity<EventDTO> leaveEvent(
            @PathVariable Long eventId,
            @RequestHeader("Authorization") String authorizationHeader
    ) {
        try {
            Long participantId = jwtService.getIdFromToken(authorizationHeader.replace("Bearer ", ""));
            return eventParticipantService.leaveEvent(eventId, participantId)
                    .map(ResponseEntity::ok)
                    .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build()); // Not found: nie był zapisany
        } catch (Exception e) {
            logger.error("Error leaving event: ", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
