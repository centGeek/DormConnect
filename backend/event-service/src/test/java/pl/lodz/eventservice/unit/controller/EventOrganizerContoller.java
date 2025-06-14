package pl.lodz.eventservice.unit.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import pl.lodz.eventservice.controller.EventOrganizerController;
import pl.lodz.eventservice.dto.EventCreateDTO;
import pl.lodz.eventservice.dto.EventDTO;
import pl.lodz.eventservice.entity.ApprovalStatus;
import pl.lodz.eventservice.service.EventOrganizerService;
import pl.lodz.eventservice.service.JwtService;

import java.time.LocalDateTime;
import java.util.List;

@WebMvcTest(EventOrganizerController.class)
public class EventOrganizerContoller {

    @Autowired
    private EventOrganizerController eventOrganizerController;

    @MockitoBean
    private EventOrganizerService eventOrganizerService;

    @MockitoBean
    private JwtService jwtService;

    @Autowired
    private ObjectMapper objectMapper;

    private EventCreateDTO validCreateDTO() {
        return new EventCreateDTO(
                "Sample Event",
                "Description",
                LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(2),
                "Warsaw",
                "Conference",
                100,
                "http://image.url/sample.png",
                42L,
                List.of()
        );
    }

    private EventDTO validEventDTO() {
        return new EventDTO(
                1L,
                "Sample Event",
                "Description",
                LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(2),
                "Warsaw",
                "Conference",
                100,
                "http://image.url/sample.png",
                42L,
                ApprovalStatus.APPROVED,
                List.of()
        );
    }



}
