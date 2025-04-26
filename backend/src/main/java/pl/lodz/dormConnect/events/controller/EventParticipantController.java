package pl.lodz.dormConnect.events.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.lodz.dormConnect.events.dto.EventDTO;
import pl.lodz.dormConnect.events.service.EventPaticipantService;

import java.util.List;

@RestController
@RequestMapping("/api/event/{eventId}/paricipant")
public class EventParticipantController {
    private static final Logger logger = LoggerFactory.getLogger(EventParticipantController.class);

    private EventPaticipantService eventPaticipantService;

    @Autowired
    public EventParticipantController(EventPaticipantService eventPaticipantService) {
        this.eventPaticipantService = eventPaticipantService;
    }

    @PostMapping("/{participantId}")
    public ResponseEntity<String> joinEvent(
            @PathVariable("eventId") Long eventId,
            @PathVariable("participantId") Long participantId
    ) {
        try {
            eventPaticipantService.joinEvent(eventId, participantId);
            return new ResponseEntity<>("Successfully joined event", HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            logger.warn("Participant not found for addition: ", e);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            logger.error("General Exception Participant: ", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{participantId}")
    public ResponseEntity<String> leaveEvent(
            @PathVariable("eventId") Long eventId,
            @PathVariable("participantId") Long participantId
    ) {
        try {
            eventPaticipantService.leaveEvent(eventId, participantId);
            return new ResponseEntity<>("Successfully left event", HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            logger.warn("Participant not found for removal: ", e);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            logger.error("General Exception on leave: ", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{participantId}")
    public ResponseEntity<List<EventDTO>> getAllEvents(@PathVariable Long participantId) {
        try {
            List<EventDTO> events = eventPaticipantService.getAllEvents(participantId);
            return new ResponseEntity<>(events, HttpStatus.NOT_IMPLEMENTED);
        } catch (Exception e) {
            logger.error("Error retrieving events: ", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
