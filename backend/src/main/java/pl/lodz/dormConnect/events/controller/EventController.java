package pl.lodz.dormConnect.events.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.lodz.dormConnect.events.dto.EventCreateDTO;
import pl.lodz.dormConnect.events.dto.EventDTO;
import pl.lodz.dormConnect.events.service.EventService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.lodz.dormConnect.security.service.JwtService;


@RestController
@RequestMapping("/api/event")
public class EventController {

    private final EventService eventService;
    private final JwtService jwtService;
    private static final Logger logger = LoggerFactory.getLogger(EventController.class);

    @Autowired
    public EventController(EventService eventService, JwtService jwtService) {
        this.eventService = eventService;
        this.jwtService = jwtService;
    }

    // To przyjmuje prostego JSON bez id
    @PostMapping("/create")
    public ResponseEntity<EventDTO> createEvent(@RequestBody EventCreateDTO eventCreateDTO) {
        try {
            return eventService.createEvent(eventCreateDTO)
                    .map(event -> new ResponseEntity<>(event, HttpStatus.CREATED))
                    .orElseGet(() -> new ResponseEntity<>(HttpStatus.BAD_REQUEST));
        } catch (Exception e) {
            logger.error("Error creating event: ", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping
    public ResponseEntity<Page<EventDTO>> getAllEvents(@PageableDefault Pageable pageable) {
        try {
            Page<EventDTO> eventsPage = eventService.getAllEvents(pageable);
            return ResponseEntity.ok(eventsPage);
        } catch (Exception e) {
            logger.error("Error retrieving events: ", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/participants")
    public ResponseEntity<Page<EventDTO>> getAllEventsForParticipant(
            @RequestHeader("Authorization") String authorizationHeader,
            @PageableDefault Pageable pageable
    ) {
        try {
            String token = authorizationHeader.replace("Bearer ", "");
            Long participantId = 2L;// jwtService.extractUserId(token);

            Page<EventDTO> eventsPage = eventService.getAllEventsForParticipant(participantId, pageable);
            return ResponseEntity.ok(eventsPage);
        } catch (Exception e) {
            logger.error("Error retrieving events: ", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{eventId}")
    public ResponseEntity<EventDTO> getEventById(@PathVariable Long eventId) {
        return eventService.getEventById(eventId)
                .map(ResponseEntity::ok)
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PutMapping("/{eventId}")
    public ResponseEntity<EventDTO> updateEvent(@PathVariable Long eventId, @RequestBody EventDTO eventDTO) {
        return eventService.updateEvent(eventId, eventDTO)
                .map(ResponseEntity::ok)
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping("/{eventId}")
    public ResponseEntity<Void> deleteEvent(@PathVariable Long eventId) {
        try {
            eventService.deleteEvent(eventId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (IllegalArgumentException e) {
            logger.warn("Event not found for deletion: ", e);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            logger.error("Error deleting event: ", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}
