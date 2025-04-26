package pl.lodz.dormConnect.event;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import pl.lodz.dormConnect.config.PersistenceContainersTestConfiguration;
import pl.lodz.dormConnect.events.dto.EventDTO;
import pl.lodz.dormConnect.events.model.EventEntity;
import pl.lodz.dormConnect.events.repository.EventRepository;

import java.time.LocalDateTime;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
@Transactional
@ContextConfiguration(classes = {PersistenceContainersTestConfiguration.class})
public class EventControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private EventRepository eventRepository;

    @Test
    @WithMockUser(username = "admin@edu.p.lodz.pl", authorities = "ADMIN")
    void shouldCreateEventSuccessfully() throws Exception {
        EventDTO dto = new EventDTO(null, "Test Event", "Opis", LocalDateTime.now(), LocalDateTime.now().plusHours(2), "Location", "PUBLIC", 50, "img.png", 1L, List.of());

        mockMvc.perform(post("/api/event/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.eventName").value("Test Event"));

        List<EventEntity> events = eventRepository.findAll();
        assert(events.size() == 1);
        assert(events.getFirst().getEventName().equals("Test Event"));
    }

    @Test
    @WithMockUser(username = "admin@edu.p.lodz.pl", authorities = "ADMIN")
    void shouldReturnAllEvents() throws Exception {
        eventRepository.save(EventEntity.builder()
                .eventName("Name")
                .description("Desc")
                .startDateTime(LocalDateTime.now())
                .endDateTime(LocalDateTime.now().plusHours(1))
                .location("Loc")
                .eventType("PUBLIC")
                .maxParticipants(10)
                .imageUrl("img")
                .organizerId(1L)
                .participantId(List.of(2L))
                .build());

        mockMvc.perform(get("/api/event")
                        .contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].eventName").value("Name"));
    }
}
