package pl.lodz.eventservice.integration.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import pl.lodz.eventservice.controller.EventOrganizerController;
import pl.lodz.eventservice.dto.EventDTO;
import pl.lodz.eventservice.service.EventOrganizerService;
import pl.lodz.eventservice.service.JwtService;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(EventOrganizerController.class)
public class EventOrganizerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private EventOrganizerService eventOrganizerService;

    @MockitoBean
    private JwtService jwtService;

    private static final String AUTH_HEADER = "Bearer token";

    @Test
    @DisplayName("GET /api/event/organizer - should return paged list of events with 200")
    public void getAllCreatedEvent_ReturnsPageOfEvents() throws Exception {
        Long organizerId = 1L;

        EventDTO event = new EventDTO(
                1L,
                "Test Event",
                "Test Description",
                LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(2),
                "Test Location",
                "Test Type",
                10,
                "http://image.url",
                organizerId,
                null,
                List.of(1L, 2L)
        );
        Page<EventDTO> page = new PageImpl<>(List.of(event));

        Mockito.when(jwtService.getIdFromToken(anyString())).thenReturn(organizerId);
        Mockito.when(eventOrganizerService.getAllCreatedEvent(eq(organizerId), any(Pageable.class))).thenReturn(page);

        mockMvc.perform(get("/api/event/organizer")
                        .header("Authorization", AUTH_HEADER)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content[0].eventId").value(1))
                .andExpect(jsonPath("$.content[0].eventName").value("Test Event"));
    }

    @Test
    @DisplayName("GET /api/event/organizer - should return 500 on exception")
    public void getAllCreatedEvent_ReturnsInternalServerErrorOnException() throws Exception {
        Mockito.when(jwtService.getIdFromToken(anyString())).thenThrow(new RuntimeException("Invalid token"));

        mockMvc.perform(get("/api/event/organizer")
                        .header("Authorization", AUTH_HEADER)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError());
    }

    @Test
    @DisplayName("GET /api/event/organizer/has-events - should return true when organizer has events")
    public void hasOrganizedEvent_ReturnsTrue() throws Exception {
        Long organizerId = 1L;

        Mockito.when(jwtService.getIdFromToken(anyString())).thenReturn(organizerId);
        Mockito.when(eventOrganizerService.hasOrganizedEvent(organizerId)).thenReturn(true);

        mockMvc.perform(get("/api/event/organizer/has-events")
                        .header("Authorization", AUTH_HEADER)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }

    @Test
    @DisplayName("GET /api/event/organizer/has-events - should return false when organizer has no events")
    public void hasOrganizedEvent_ReturnsFalse() throws Exception {
        Long organizerId = 1L;

        Mockito.when(jwtService.getIdFromToken(anyString())).thenReturn(organizerId);
        Mockito.when(eventOrganizerService.hasOrganizedEvent(organizerId)).thenReturn(false);

        mockMvc.perform(get("/api/event/organizer/has-events")
                        .header("Authorization", AUTH_HEADER)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("false"));
    }

    @Test
    @DisplayName("GET /api/event/organizer/has-events - should return 500 on exception")
    public void hasOrganizedEvent_ReturnsInternalServerErrorOnException() throws Exception {
        Mockito.when(jwtService.getIdFromToken(anyString())).thenThrow(new RuntimeException("Invalid token"));

        mockMvc.perform(get("/api/event/organizer/has-events")
                        .header("Authorization", AUTH_HEADER)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError());
    }
}
