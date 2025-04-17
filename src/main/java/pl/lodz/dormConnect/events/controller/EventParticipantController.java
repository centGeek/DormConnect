package pl.lodz.dormConnect.events.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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

        return new ResponseEntity<>("Participant added", HttpStatus.OK);
    }

}
