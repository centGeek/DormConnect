package pl.lodz.eventservice.unit.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.*;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import pl.lodz.eventservice.controller.EventApprovalController;
import pl.lodz.eventservice.dto.EventDTO;
import pl.lodz.eventservice.entity.ApprovalStatus;
import pl.lodz.eventservice.service.EventApprovalService;

import jakarta.persistence.EntityNotFoundException;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(EventApprovalController.class)
public class EventApprovalControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private EventApprovalService eventApprovalService;

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
                ApprovalStatus.WAITING,
                List.of()
        );
    }

    @Test
    @DisplayName("PUT /api/event/administrate/{eventId}/approve - success")
    void approveEvent_success() throws Exception {
        Mockito.doNothing().when(eventApprovalService).approveEvent(1L);

        mockMvc.perform(put("/api/event/administrate/1/approve"))
                .andExpect(status().isOk())
                .andExpect(content().string("Event approved."));
    }

    @Test
    @DisplayName("PUT /api/event/administrate/{eventId}/approve - event not found")
    void approveEvent_notFound() throws Exception {
        Mockito.doThrow(new EntityNotFoundException("Event not found"))
                .when(eventApprovalService).approveEvent(99L);

        mockMvc.perform(put("/api/event/administrate/99/approve"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Event not found"));
    }

    @Test
    @DisplayName("DELETE /api/event/administrate/{eventId}/reject - success")
    void rejectEvent_success() throws Exception {
        Mockito.doNothing().when(eventApprovalService).rejectEvent(1L);

        mockMvc.perform(delete("/api/event/administrate/1/reject"))
                .andExpect(status().isOk())
                .andExpect(content().string("Event rejected."));
    }

    @Test
    @DisplayName("DELETE /api/event/administrate/{eventId}/reject - event not found")
    void rejectEvent_notFound() throws Exception {
        Mockito.doThrow(new EntityNotFoundException("Event not found"))
                .when(eventApprovalService).rejectEvent(99L);

        mockMvc.perform(delete("/api/event/administrate/99/reject"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Event not found"));
    }

    @Test
    @DisplayName("GET /api/event/administrate - get all events success")
    void getAllEvents_success() throws Exception {
        EventDTO event = exampleEventDTO();
        Page<EventDTO> page = new PageImpl<>(List.of(event), PageRequest.of(0, 10), 1);

        Mockito.when(eventApprovalService.getAllEvents(any(Pageable.class))).thenReturn(page);

        mockMvc.perform(get("/api/event/administrate")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].eventId").value(event.eventId()))
                .andExpect(jsonPath("$.content[0].eventName").value(event.eventName()));
    }

    @Test
    @DisplayName("GET /api/event/administrate - get all events failure")
    void getAllEvents_failure() throws Exception {
        Mockito.when(eventApprovalService.getAllEvents(any(Pageable.class))).thenThrow(new RuntimeException("Error"));

        mockMvc.perform(get("/api/event/administrate"))
                .andExpect(status().isInternalServerError());
    }

    @Test
    @DisplayName("GET /api/event/administrate/waiting - get waiting events success")
    void getAllWaitingEvents_success() throws Exception {
        EventDTO event = exampleEventDTO();
        Page<EventDTO> page = new PageImpl<>(List.of(event), PageRequest.of(0, 10), 1);

        Mockito.when(eventApprovalService.getEventsByApprovalStatus(eq(ApprovalStatus.WAITING), any(Pageable.class)))
                .thenReturn(page);

        mockMvc.perform(get("/api/event/administrate/waiting")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].approvalStatus").value("WAITING"));
    }

    @Test
    @DisplayName("GET /api/event/administrate/waiting - failure")
    void getAllWaitingEvents_failure() throws Exception {
        Mockito.when(eventApprovalService.getEventsByApprovalStatus(eq(ApprovalStatus.WAITING), any(Pageable.class)))
                .thenThrow(new RuntimeException("Error"));

        mockMvc.perform(get("/api/event/administrate/waiting"))
                .andExpect(status().isInternalServerError());
    }

    @Test
    @DisplayName("GET /api/event/administrate/approved - get approved events success")
    void getAllApprovedEvents_success() throws Exception {
        EventDTO event = exampleEventDTO();
        Page<EventDTO> page = new PageImpl<>(List.of(event), PageRequest.of(0, 10), 1);

        Mockito.when(eventApprovalService.getEventsByApprovalStatus(eq(ApprovalStatus.APPROVED), any(Pageable.class)))
                .thenReturn(page);

        mockMvc.perform(get("/api/event/administrate/approved")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("GET /api/event/administrate/declined - get declined events success")
    void getAllDeclinedEvents_success() throws Exception {
        EventDTO event = exampleEventDTO();
        Page<EventDTO> page = new PageImpl<>(List.of(event), PageRequest.of(0, 10), 1);

        Mockito.when(eventApprovalService.getEventsByApprovalStatus(eq(ApprovalStatus.DECLINED), any(Pageable.class)))
                .thenReturn(page);

        mockMvc.perform(get("/api/event/administrate/declined")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk());
    }
}
