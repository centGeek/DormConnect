package pl.lodz.dormConnect.event.unitTest.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import pl.lodz.dormConnect.events.controller.EventParticipantController;
import pl.lodz.dormConnect.events.dto.EventDTO;
import pl.lodz.dormConnect.events.mapper.EventMapper;
import pl.lodz.dormConnect.events.service.EventParticipantService;
import pl.lodz.dormConnect.security.config.JwtAuthenticationFilter;
import pl.lodz.dormConnect.security.service.JwtService;

import java.time.LocalDateTime;
import java.util.*;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class EventParticipantControllerTest {
    @Mock
    private EventMapper eventMapper;

    @Mock
    private EventParticipantService eventParticipantService;

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private EventParticipantController eventParticipantController;

    private MockMvc mockMvc;

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(eventParticipantController)
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .addFilters(new JwtAuthenticationFilter(jwtService))
                .build();
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
    }

    @Test
    void shouldReturnEventsForParticipant() throws Exception {
        Long participantId = 2L;
        String token = "Bearer valid-jwt-token";

        EventDTO eventDTO = new EventDTO(1L, "Sample Event", "Opis", LocalDateTime.now(), LocalDateTime.now().plusHours(2), "Sample Location", "PUBLIC", 100, "event-image.png", 1L, false, List.of(participantId));
        Page<EventDTO> page = new PageImpl<>(List.of(eventDTO), PageRequest.of(0, 10), 1);

        when(jwtService.getIdFromToken("valid-jwt-token")).thenReturn(participantId);
        when(eventParticipantService.getAllEventsForParticipant(eq(participantId), any(Pageable.class))).thenReturn(page);

        mockMvc.perform(get("/api/event/participant")
                        .header(HttpHeaders.AUTHORIZATION, token)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].eventName").value("Sample Event"))
                .andExpect(jsonPath("$.content[0].location").value("Sample Location"));
    }


}
