package pl.lodz.eventservice.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.lodz.eventservice.dto.EventDTO;
import pl.lodz.eventservice.service.EventOrganizerService;
import pl.lodz.eventservice.service.JwtService;

@RestController
@RequestMapping("/api/event/organizer")
public class EventOrganizerController {
    private static final Logger logger = LoggerFactory.getLogger(EventOrganizerController.class);

    private final EventOrganizerService eventOrganizerService;

    private final JwtService jwtService;

    @Autowired
    public EventOrganizerController(EventOrganizerService eventOrganizerService, JwtService jwtService) {
        this.eventOrganizerService = eventOrganizerService;
        this.jwtService = jwtService;
    }

    @GetMapping
    public ResponseEntity<Page<EventDTO>> getAllCreatedEvent(
            @RequestHeader("Authorization") String authorizationHeader,
            @PageableDefault Pageable pageable
    ) {
        try {
            String token = authorizationHeader.replace("Bearer ", "");
            Long organizerId = jwtService.getIdFromToken(token);

            Page<EventDTO> eventsPage = eventOrganizerService.getAllCreatedEvent(organizerId, pageable);
            return new ResponseEntity<>(eventsPage, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Error retrieving events: ", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/has-events")
    public ResponseEntity<Boolean> hasOrganizedEvent(
            @RequestHeader("Authorization") String authorizationHeader
    ) {
        try {
            String token = authorizationHeader.replace("Bearer ", "");
            Long organizerId = jwtService.getIdFromToken(token);

            boolean hasEvents = eventOrganizerService.hasOrganizedEvent(organizerId);
            return new ResponseEntity<>(hasEvents, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Error checking if organizer has events: ", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
