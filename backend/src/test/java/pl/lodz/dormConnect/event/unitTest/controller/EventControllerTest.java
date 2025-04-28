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
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import pl.lodz.dormConnect.events.controller.EventController;
import pl.lodz.dormConnect.events.dto.EventCreateDTO;
import pl.lodz.dormConnect.events.dto.EventDTO;
import pl.lodz.dormConnect.events.mapper.EventMapper;
import pl.lodz.dormConnect.events.service.EventService;
import pl.lodz.dormConnect.security.config.JwtAuthenticationFilter;
import pl.lodz.dormConnect.security.service.JwtService;

import java.time.LocalDateTime;
import java.util.*;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class EventControllerTest {
    @Mock
    private EventMapper eventMapper;

    @Mock
    private EventService eventService;

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private EventController eventController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(eventController)
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .addFilters(new JwtAuthenticationFilter(jwtService))
                .build();
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
    }

    @Test
    void shouldCreateEventSuccessfully() throws Exception {
        EventCreateDTO createDto = new EventCreateDTO(
                "Test Event",
                "Opis",
                LocalDateTime.now(),
                LocalDateTime.now().plusHours(2),
                "Location",
                "PUBLIC",
                50,
                "img.png",
                1L,
                new ArrayList<>(List.of(2L, 3L))
        );
        EventDTO eventDTO = new EventDTO(
                1L,
                "Test Event",
                "Opis",
                LocalDateTime.now(),
                LocalDateTime.now().plusHours(2),
                "Location",
                "PUBLIC",
                50,
                "img.png",
                1L,
                false,
                new ArrayList<>()
        );

        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken("user", null, Collections.emptyList())
        );

        when(eventService.createEvent(any(EventCreateDTO.class))).thenReturn(Optional.of(eventDTO));

        mockMvc.perform(post("/api/event/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.eventName").value("Test Event"));

        verify(eventService, times(1)).createEvent(any(EventCreateDTO.class));
    }

    @Test
    void shouldReturnAllEvents() throws Exception {
        EventDTO eventDTO = new EventDTO(
                1L,
                "Test Event",
                "Opis",
                LocalDateTime.now(),
                LocalDateTime.now().plusHours(2),
                "Location",
                "PUBLIC",
                50,
                "img.png",
                1L,
                false,
                new ArrayList<>()
        );

        Page<EventDTO> page = new PageImpl<>(List.of(eventDTO), PageRequest.of(0, 10), 1);

        when(eventService.getAllEvents(any())).thenReturn(page);

        mockMvc.perform(get("/api/event"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].eventName").value("Test Event"));

        verify(eventService, times(1)).getAllEvents(any());
    }

}
