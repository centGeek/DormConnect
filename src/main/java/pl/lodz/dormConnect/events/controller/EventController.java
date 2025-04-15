package pl.lodz.dormConnect.events.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.lodz.dormConnect.events.dto.EventDTO;
import pl.lodz.dormConnect.events.service.EventService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@RestController
@RequestMapping("/api/event")
public class EventController {

    private final EventService eventService;
    private static final Logger logger = LoggerFactory.getLogger(EventController.class);

    @Autowired
    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @PostMapping("/create")
    public ResponseEntity<String> createEvent(@RequestBody EventDTO eventDTO) {
        try {
            eventService.createEvent(eventDTO);
            return new ResponseEntity<>("Event created successfully", HttpStatus.CREATED);
        } catch (Exception e) {
            logger.error("Error creating event: ", e);
            return new ResponseEntity<>("Error creating event", HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping
    public ResponseEntity<List<EventDTO>> getEvents() {
        try {
            List<EventDTO> events = eventService.getAllEvents();
            return new ResponseEntity<>(events, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Error retrieving events: ", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{eventId}")
    public ResponseEntity<EventDTO> getEventById(@PathVariable Long eventId) {
        try {
            EventDTO event = eventService.getEventById(eventId);
            return new ResponseEntity<>(event, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            logger.warn("Event not found: ", e);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            logger.error("Error retrieving event by ID: ", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
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
