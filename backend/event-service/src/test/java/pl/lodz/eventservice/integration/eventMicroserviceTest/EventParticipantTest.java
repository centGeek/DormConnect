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
import pl.lodz.eventservice.service.EventParticipantService;
import pl.lodz.eventservice.service.JwtService;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@Import(PostgresContainerConfig.class)
public class EventParticipantTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private EventParticipantService eventParticipantService;

    @Autowired
    private JwtService jwtService;

    @LocalServerPort
    private int port;

    private String eventBaseUrl() {
        return "http://localhost:" + port + "/api/event";
    }

    private String participantBaseUrl() {
        return "http://localhost:" + port + "/api/event/participant";
    }

    private EventCreateDTO validEvent() {
        return new EventCreateDTO(
                "Joinable Event",
                "Desc",
                LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(1).plusHours(2),
                "Lodz",
                "Workshop",
                10,
                "http://img.com/pic.png",
                1L,
                List.of()
        );
    }

    private Long createEvent() throws Exception {
        EventCreateDTO dto = validEvent();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> request = new HttpEntity<>(objectMapper.writeValueAsString(dto), headers);
        ResponseEntity<EventDTO> response = restTemplate.postForEntity(eventBaseUrl() + "/create", request, EventDTO.class);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        return response.getBody().eventId();
    }

    private HttpHeaders authHeaderStudentOrganizer() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + jwtService
                .generateToken(1L, "test@email.com", List.of("STUDENT")));
        return headers;
    }

    private HttpHeaders authHeaderAdmin() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + jwtService
                .generateToken(2L, "test_admin@email.com", List.of("ADMIN")));
        return headers;
    }

    private HttpHeaders authHeaderStudentParticipant() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + jwtService
                .generateToken(3L, "test_student@email.com", List.of("STUDENT")));
        return headers;
    }

    @Test
    @DisplayName("Should allow participant to join event")
    public void shouldJoinEvent() throws Exception {
        Long eventId = createEvent();

        HttpHeaders headers = authHeaderStudentParticipant();
        HttpEntity<Void> request = new HttpEntity<>(headers);

        ResponseEntity<EventDTO> response = restTemplate.exchange(
                participantBaseUrl() + "/" + eventId,
                HttpMethod.PUT,
                request,
                EventDTO.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());


        assertNotNull(response.getBody());
        assertTrue(response.getBody().participantId().contains(3L));
    }

    @Test
    @DisplayName("Should allow participant to leave event")
    public void shouldLeaveEvent() throws Exception {
        Long eventId = createEvent();

        HttpHeaders headers = authHeaderStudentParticipant();

        HttpEntity<Void> joinRequest = new HttpEntity<>(headers);
        restTemplate.exchange(participantBaseUrl() + "/" + eventId, HttpMethod.PUT, joinRequest, EventDTO.class);

        HttpEntity<Void> leaveRequest = new HttpEntity<>(headers);
        ResponseEntity<EventDTO> response = restTemplate.exchange(
                participantBaseUrl() + "/" + eventId,
                HttpMethod.DELETE,
                leaveRequest,
                EventDTO.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());

        assertNotNull(response.getBody());
        assertFalse(response.getBody().participantId().contains(3L));
    }

    @Test
    @DisplayName("Should retrieve events for participant")
    public void shouldGetEventsForParticipant() throws Exception {
        Long eventId = createEvent();

        HttpHeaders headers = authHeaderStudentParticipant();

        restTemplate.exchange(
                participantBaseUrl() + "/" + eventId,
                HttpMethod.PUT,
                new HttpEntity<>(headers),
                EventDTO.class);

        ResponseEntity<String> response = restTemplate.exchange(
                participantBaseUrl(),
                HttpMethod.GET,
                new HttpEntity<>(headers),
                String.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().contains("Joinable Event"));
    }

    @Test
    @DisplayName("Should return CONFLICT when joining full event")
    public void shouldNotJoinFullEvent() throws Exception {
        Long eventId = createEvent();

        // Wypełnij event do maxParticipants
        for (long i = 1; i <= 10; i++) {
            eventParticipantService.joinEvent(eventId, i);
        }

        HttpHeaders headers = authHeaderStudentParticipant();
        HttpEntity<Void> request = new HttpEntity<>(headers);

        ResponseEntity<EventDTO> response = restTemplate.exchange(
                participantBaseUrl() + "/" + eventId,
                HttpMethod.PUT,
                request,
                EventDTO.class);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
    }

    @Test
    @DisplayName("Should return CONFLICT when participant already joined event")
    public void shouldNotJoinIfAlreadyParticipant() throws Exception {
        Long eventId = createEvent();

        HttpHeaders headers = authHeaderStudentParticipant();
        HttpEntity<Void> request = new HttpEntity<>(headers);

        restTemplate.exchange(participantBaseUrl() + "/" + eventId, HttpMethod.PUT, request, EventDTO.class);

        ResponseEntity<EventDTO> response = restTemplate.exchange(
                participantBaseUrl() + "/" + eventId,
                HttpMethod.PUT,
                request,
                EventDTO.class);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
    }

    @Test
    @DisplayName("Should return NOT_FOUND when leaving event not joined")
    public void shouldReturnNotFoundWhenLeavingNotJoinedEvent() throws Exception {
        Long eventId = createEvent();

        HttpHeaders headers = authHeaderStudentParticipant();
        HttpEntity<Void> leaveRequest = new HttpEntity<>(headers);

        ResponseEntity<EventDTO> response = restTemplate.exchange(
                participantBaseUrl() + "/" + eventId,
                HttpMethod.DELETE,
                leaveRequest,
                EventDTO.class);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    @DisplayName("Should return INTERNAL_SERVER_ERROR on invalid token")
    public void shouldReturnInternalServerErrorOnInvalidToken() throws Exception {
        Long eventId = createEvent();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer invalid.token.here");

        HttpEntity<Void> request = new HttpEntity<>(headers);

        ResponseEntity<EventDTO> response = restTemplate.exchange(
                participantBaseUrl() + "/" + eventId,
                HttpMethod.PUT,
                request,
                EventDTO.class);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    @DisplayName("Should get paged events for participant")
    public void shouldGetPagedEventsForParticipant() throws Exception {
        Long eventId1 = createEvent();
        Long eventId2 = createEvent();

        HttpHeaders headers = authHeaderStudentParticipant();
        HttpEntity<Void> request = new HttpEntity<>(headers);

        restTemplate.exchange(participantBaseUrl() + "/" + eventId1, HttpMethod.PUT, request, EventDTO.class);
        restTemplate.exchange(participantBaseUrl() + "/" + eventId2, HttpMethod.PUT, request, EventDTO.class);

        ResponseEntity<String> response = restTemplate.exchange(
                participantBaseUrl() + "?page=0&size=1",
                HttpMethod.GET,
                new HttpEntity<>(headers),
                String.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

}
