package pl.lodz.eventservice.integration.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import pl.lodz.eventservice.controller.EventController;
import pl.lodz.eventservice.dto.EventCreateDTO;
import pl.lodz.eventservice.dto.EventDTO;
import pl.lodz.eventservice.entity.ApprovalStatus;
import pl.lodz.eventservice.service.EventService;
import pl.lodz.eventservice.service.JwtService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(EventController.class)
public class EventControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private EventService eventService;

    @MockitoBean
    private JwtService jwtService;

    private static final String AUTH_HEADER = "Bearer token";

    private EventCreateDTO validCreateDTO() {
        return new EventCreateDTO(
                "Sample Event",
                "Description",
                LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(2),
                "Warsaw",
                "Meeting",
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

    @Test
    @DisplayName("POST /api/event/create - should return 201 Created when input is valid")
    void createEvent_shouldReturnCreated_whenValidInput() throws Exception {
        EventCreateDTO createDTO = validCreateDTO();
        EventDTO responseDTO = validEventDTO();

        Mockito.when(eventService.createEvent(any(EventCreateDTO.class)))
                .thenReturn(Optional.of(responseDTO));

        mockMvc.perform(post("/api/event/create")
                        .header("Authorization", AUTH_HEADER)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.eventId").value(responseDTO.eventId()))
                .andExpect(jsonPath("$.eventName").value(responseDTO.eventName()));
    }

    @Test
    @DisplayName("POST /api/event/create - should return 400 Bad Request when creation fails")
    void createEvent_shouldReturnBadRequest_whenCreateFails() throws Exception {
        EventCreateDTO createDTO = validCreateDTO();

        Mockito.when(eventService.createEvent(any(EventCreateDTO.class)))
                .thenReturn(Optional.empty());

        mockMvc.perform(post("/api/event/create")
                        .header("Authorization", AUTH_HEADER)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("POST /api/event/create - should return 400 Bad Request when validation fails")
    void createEvent_shouldReturnBadRequest_whenValidationFails() throws Exception {
        EventCreateDTO invalidDTO = new EventCreateDTO(
                "",
                "Desc",
                LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(2),
                "Location",
                "Type",
                10,
                null,
                1L,
                List.of()
        );

        mockMvc.perform(post("/api/event/create")
                        .header("Authorization", AUTH_HEADER)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("GET /api/event - should return page of approved events")
    void getAllApprovedEvents_shouldReturnPage() throws Exception {
        EventDTO event = validEventDTO();

        Mockito.when(eventService.getAllApprovedEvents(any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of(event), PageRequest.of(0, 10), 1));

        mockMvc.perform(get("/api/event")
                        .header("Authorization", AUTH_HEADER))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].eventId").value(event.eventId()));
    }

    @Test
    @DisplayName("GET /api/event/{eventId} - should return event when it exists")
    void getEventById_shouldReturnEvent_whenExists() throws Exception {
        EventDTO event = validEventDTO();

        Mockito.when(eventService.getEventById(1L))
                .thenReturn(Optional.of(event));

        mockMvc.perform(get("/api/event/1")
                        .header("Authorization", AUTH_HEADER))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.eventId").value(event.eventId()));
    }

    @Test
    @DisplayName("GET /api/event/{eventId} - should return 404 Not Found when event does not exist")
    void getEventById_shouldReturnNotFound_whenMissing() throws Exception {
        Mockito.when(eventService.getEventById(99L))
                .thenReturn(Optional.empty());

        mockMvc.perform(get("/api/event/99")
                        .header("Authorization", AUTH_HEADER))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("PUT /api/event/{eventId} - should return updated event when update succeeds")
    void updateEvent_shouldReturnUpdatedEvent_whenSuccess() throws Exception {
        EventDTO event = validEventDTO();

        Mockito.when(eventService.updateEvent(eq(1L), any(EventDTO.class)))
                .thenReturn(Optional.of(event));

        mockMvc.perform(put("/api/event/1")
                        .header("Authorization", AUTH_HEADER)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(event)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.eventId").value(event.eventId()));
    }

    @Test
    @DisplayName("PUT /api/event/{eventId} - should return 404 Not Found when update fails")
    void updateEvent_shouldReturnNotFound_whenUpdateFails() throws Exception {
        EventDTO event = validEventDTO();

        Mockito.when(eventService.updateEvent(eq(99L), any(EventDTO.class)))
                .thenReturn(Optional.empty());

        mockMvc.perform(put("/api/event/99")
                        .header("Authorization", AUTH_HEADER)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(event)))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("DELETE /api/event/{eventId} - should return 204 No Content when deletion succeeds")
    void deleteEvent_shouldReturnNoContent_whenSuccess() throws Exception {

        mockMvc.perform(delete("/api/event/1")
                        .header("Authorization", AUTH_HEADER))
                .andExpect(status().isNoContent());

        Mockito.verify(eventService).deleteEvent(1L);
    }

    @Test
    @DisplayName("DELETE /api/event/{eventId} - should return 404 Not Found when event to delete does not exist")
    void deleteEvent_shouldReturnNotFound_whenEventMissing() throws Exception {
        Mockito.doThrow(new IllegalArgumentException("Event not found"))
                .when(eventService).deleteEvent(99L);

        mockMvc.perform(delete("/api/event/99")
                        .header("Authorization", AUTH_HEADER))
                .andExpect(status().isNotFound());
    }
}
