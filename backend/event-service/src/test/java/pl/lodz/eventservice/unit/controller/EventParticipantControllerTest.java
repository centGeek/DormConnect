package pl.lodz.eventservice.unit.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.*;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import pl.lodz.eventservice.controller.EventParticipantController;
import pl.lodz.eventservice.dto.EventDTO;
import pl.lodz.eventservice.service.EventParticipantService;
import pl.lodz.eventservice.service.JwtService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(EventParticipantController.class)
public class EventParticipantControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private EventParticipantService eventParticipantService;

    @MockitoBean
    private JwtService jwtService;

    private static final String AUTH_HEADER = "Bearer token";

    private EventDTO exampleEventDTO() {
        return new EventDTO(
                1L,
                "Test Event",
                "Description",
                LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(2),
                "City",
                "Type",
                50,
                "http://image.url/sample.png",
                42L,
                null,
                List.of()
        );
    }

    @Test
    @DisplayName("PUT /api/event/participant/{eventId} - joinEvent success")
    void joinEvent_success() throws Exception {
        Long participantId = 123L;
        Long eventId = 1L;
        EventDTO eventDTO = exampleEventDTO();

        Mockito.when(jwtService.getIdFromToken(anyString())).thenReturn(participantId);
        Mockito.when(eventParticipantService.joinEvent(eventId, participantId))
                .thenReturn(Optional.of(eventDTO));

        mockMvc.perform(put("/api/event/participant/{eventId}", eventId)
                        .header("Authorization", AUTH_HEADER)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.eventId").value(eventDTO.eventId()))
                .andExpect(jsonPath("$.eventName").value(eventDTO.eventName()));
    }

    @Test
    @DisplayName("PUT /api/event/participant/{eventId} - joinEvent conflict")
    void joinEvent_conflict() throws Exception {
        Long participantId = 123L;
        Long eventId = 1L;

        Mockito.when(jwtService.getIdFromToken(anyString())).thenReturn(participantId);
        Mockito.when(eventParticipantService.joinEvent(eventId, participantId))
                .thenReturn(Optional.empty());

        mockMvc.perform(put("/api/event/participant/{eventId}", eventId)
                        .header("Authorization", AUTH_HEADER)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isConflict());
    }

    @Test
    @DisplayName("PUT /api/event/participant/{eventId} - joinEvent exception returns 500")
    void joinEvent_exception() throws Exception {
        Long eventId = 1L;

        Mockito.when(jwtService.getIdFromToken(anyString())).thenThrow(new RuntimeException("JWT error"));

        mockMvc.perform(put("/api/event/participant/{eventId}", eventId)
                        .header("Authorization", AUTH_HEADER)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError());
    }

    @Test
    @DisplayName("DELETE /api/event/participant/{eventId} - leaveEvent success")
    void leaveEvent_success() throws Exception {
        Long participantId = 123L;
        Long eventId = 1L;
        EventDTO eventDTO = exampleEventDTO();

        Mockito.when(jwtService.getIdFromToken(anyString())).thenReturn(participantId);
        Mockito.when(eventParticipantService.leaveEvent(eventId, participantId))
                .thenReturn(Optional.of(eventDTO));

        mockMvc.perform(delete("/api/event/participant/{eventId}", eventId)
                        .header("Authorization", AUTH_HEADER)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.eventId").value(eventDTO.eventId()))
                .andExpect(jsonPath("$.eventName").value(eventDTO.eventName()));
    }

    @Test
    @DisplayName("DELETE /api/event/participant/{eventId} - leaveEvent not found")
    void leaveEvent_notFound() throws Exception {
        Long participantId = 123L;
        Long eventId = 1L;

        Mockito.when(jwtService.getIdFromToken(anyString())).thenReturn(participantId);
        Mockito.when(eventParticipantService.leaveEvent(eventId, participantId))
                .thenReturn(Optional.empty());

        mockMvc.perform(delete("/api/event/participant/{eventId}", eventId)
                        .header("Authorization", AUTH_HEADER)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("DELETE /api/event/participant/{eventId} - leaveEvent exception returns 500")
    void leaveEvent_exception() throws Exception {
        Long eventId = 1L;

        Mockito.when(jwtService.getIdFromToken(anyString())).thenThrow(new RuntimeException("JWT error"));

        mockMvc.perform(delete("/api/event/participant/{eventId}", eventId)
                        .header("Authorization", AUTH_HEADER)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError());
    }

    @Test
    @DisplayName("GET /api/event/participant - getAllEventsForParticipant success")
    void getAllEventsForParticipant_success() throws Exception {
        Long participantId = 123L;
        EventDTO eventDTO = exampleEventDTO();

        Page<EventDTO> page = new PageImpl<>(List.of(eventDTO), PageRequest.of(0, 10), 1);

        Mockito.when(jwtService.getIdFromToken(anyString())).thenReturn(participantId);
        Mockito.when(eventParticipantService.getAllEventsForParticipant(eq(participantId), any(Pageable.class)))
                .thenReturn(page);

        mockMvc.perform(get("/api/event/participant")
                        .header("Authorization", AUTH_HEADER)
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].eventId").value(eventDTO.eventId()))
                .andExpect(jsonPath("$.content[0].eventName").value(eventDTO.eventName()));
    }

    @Test
    @DisplayName("GET /api/event/participant - getAllEventsForParticipant exception returns 500")
    void getAllEventsForParticipant_exception() throws Exception {
        Mockito.when(jwtService.getIdFromToken(anyString())).thenThrow(new RuntimeException("JWT error"));

        mockMvc.perform(get("/api/event/participant")
                        .header("Authorization", AUTH_HEADER)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError());
    }
}
