package pl.lodz.dormConnect.events.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/event/{eventId}/participants")
public class EventParticipantController {
    private static final Logger logger = LoggerFactory.getLogger(EventParticipantController.class);

    

}
