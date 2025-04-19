package pl.lodz.dormConnect.events.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.lodz.dormConnect.events.dto.EventDTO;
import pl.lodz.dormConnect.events.service.EventPaticipantService;

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
    public ResponseEntity<String> addParticipant(
            @PathVariable("eventId") Long eventId,
            @PathVariable("participantId") String participantId
    ) {
        try {
            return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
        } catch (IllegalArgumentException e) {
            logger.warn("Participant not found for addition: ", e);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            logger.warn("General Exception Participant: ", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{participantId}")
    public ResponseEntity<Void> getParticipantById(
            @PathVariable Long participantId
    ) {

        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);

    }

    @GetMapping

    @DeleteMapping("/{participantId}")
    public ResponseEntity<String> deleteParticipant(@PathVariable Long participantId) {

        return new ResponseEntity<>("Participant deleted", HttpStatus.OK);
    }



}
