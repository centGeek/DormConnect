package pl.lodz.eventservice.integration.eventMicroserviceTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;
import pl.lodz.eventservice.config.PostgresContainerConfig;
import pl.lodz.eventservice.dto.EventCreateDTO;
import pl.lodz.eventservice.dto.EventDTO;
import pl.lodz.eventservice.service.JwtService;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@Import(PostgresContainerConfig.class)
public class EventApprovalTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private JwtService jwtService;

    @LocalServerPort
    private int port;

    private String baseUrl() {
        return "http://localhost:" + port + "/api/event/administrate";
    }

    private String eventUrl() {
        return "http://localhost:" + port + "/api/event";
    }

    private HttpHeaders defaultHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + jwtService.generateToken(1L, "admin@email.com", List.of("ADMIN")));
        return headers;
    }

    private EventCreateDTO validEventCreateDTO() {
        return new EventCreateDTO(
                "Test Approval Event",
                "Desc",
                LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(1).plusHours(2),
                "Lodz",
                "Lecture",
                30,
                "http://image.png",
                1L,
                List.of()
        );
    }

    private Long createEvent() throws Exception {
        EventCreateDTO dto = validEventCreateDTO();
        HttpHeaders headers = defaultHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> request = new HttpEntity<>(objectMapper.writeValueAsString(dto), headers);
        ResponseEntity<EventDTO> response = restTemplate.postForEntity(eventUrl() + "/create", request, EventDTO.class);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        return response.getBody().eventId();
    }

    @Test
    @DisplayName("Should approve event")
    public void shouldApproveEvent() throws Exception {
        Long eventId = createEvent();
        HttpHeaders headers = defaultHeaders();

        ResponseEntity<String> response = restTemplate.exchange(
                baseUrl() + "/" + eventId + "/approve",
                HttpMethod.PUT,
                new HttpEntity<>(headers),
                String.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Event approved.", response.getBody());
    }

    @Test
    @DisplayName("Should reject event")
    public void shouldRejectEvent() throws Exception {
        Long eventId = createEvent();
        HttpHeaders headers = defaultHeaders();

        ResponseEntity<String> response = restTemplate.exchange(
                baseUrl() + "/" + eventId + "/reject",
                HttpMethod.DELETE,
                new HttpEntity<>(headers),
                String.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Event rejected.", response.getBody());
    }

    @Test
    @DisplayName("Should return NOT_FOUND when approving non-existent event")
    public void shouldNotApproveNonExistentEvent() {
        HttpHeaders headers = defaultHeaders();
        ResponseEntity<String> response = restTemplate.exchange(
                baseUrl() + "/999999/approve",
                HttpMethod.PUT,
                new HttpEntity<>(headers),
                String.class);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertTrue(response.getBody().contains("Event not found"));
    }

    @Test
    @DisplayName("Should get all events")
    public void shouldGetAllEvents() {
        HttpHeaders headers = defaultHeaders();
        ResponseEntity<String> response = restTemplate.exchange(
                baseUrl() + "?page=0&size=5",
                HttpMethod.GET,
                new HttpEntity<>(headers),
                String.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    @DisplayName("Should get waiting events")
    public void shouldGetWaitingEvents() {
        HttpHeaders headers = defaultHeaders();
        ResponseEntity<String> response = restTemplate.exchange(
                baseUrl() + "/waiting",
                HttpMethod.GET,
                new HttpEntity<>(headers),
                String.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    @DisplayName("Should get approved events")
    public void shouldGetApprovedEvents() {
        HttpHeaders headers = defaultHeaders();
        ResponseEntity<String> response = restTemplate.exchange(
                baseUrl() + "/approved",
                HttpMethod.GET,
                new HttpEntity<>(headers),
                String.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    @DisplayName("Should get declined events")
    public void shouldGetDeclinedEvents() {
        HttpHeaders headers = defaultHeaders();
        ResponseEntity<String> response = restTemplate.exchange(
                baseUrl() + "/declined",
                HttpMethod.GET,
                new HttpEntity<>(headers),
                String.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
}