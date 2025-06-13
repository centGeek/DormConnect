package pl.lodz.eventservice.controller;

import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.lodz.eventservice.dto.EventDTO;
import pl.lodz.eventservice.entity.ApprovalStatus;
import pl.lodz.eventservice.service.EventApprovalService;

@RestController
@RequestMapping("/api/event/administrate")
public class EventApprovalController {

    private final EventApprovalService eventApprovalService;
    private static final Logger logger = LoggerFactory.getLogger(EventApprovalController.class);

    @Autowired
    public EventApprovalController(EventApprovalService eventApprovalService) {
        this.eventApprovalService = eventApprovalService;
    }

    @PutMapping("/{eventId}/approve")
    public ResponseEntity<String> approveEvent(@PathVariable("eventId") Long eventId) {
        try {
            eventApprovalService.approveEvent(eventId);
            return ResponseEntity.ok("Event approved.");
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @DeleteMapping("/{eventId}/reject")
    public ResponseEntity<String> rejectEvent(@PathVariable("eventId") Long eventId) {
        try {
            eventApprovalService.rejectEvent(eventId);
            return ResponseEntity.ok("Event rejected.");
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<Page<EventDTO>> getAllEvents(@PageableDefault Pageable pageable) {
        try {
            Page<EventDTO> eventsPage = eventApprovalService.getAllEvents(pageable);
            return ResponseEntity.ok(eventsPage);
        } catch (Exception e) {
            logger.error("Error retrieving events: ", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/waiting")
    public ResponseEntity<Page<EventDTO>> getAllWaitingEvents(@PageableDefault Pageable pageable) {

        try {
            Page<EventDTO> waitingEvents = eventApprovalService.getEventsByApprovalStatus(ApprovalStatus.WAITING, pageable);
            return ResponseEntity.ok(waitingEvents);
        } catch (Exception e) {
            logger.error("Error retrieving waiting events: ", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/approved")
    public ResponseEntity<Page<EventDTO>> getAllApprovedEvents(@PageableDefault Pageable pageable) {
        try {
            Page<EventDTO> approvedEvents = eventApprovalService.getEventsByApprovalStatus(ApprovalStatus.APPROVED, pageable);
            return ResponseEntity.ok(approvedEvents);
        } catch (Exception e) {
            logger.error("Error retrieving approved events: ", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/declined")
    public ResponseEntity<Page<EventDTO>> getAllDeclinedEvents(@PageableDefault Pageable pageable) {
        try {
            Page<EventDTO> declinedEvents = eventApprovalService.getEventsByApprovalStatus(ApprovalStatus.DECLINED, pageable);
            return ResponseEntity.ok(declinedEvents);
        } catch (Exception e) {
            logger.error("Error retrieving declined events: ", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}
