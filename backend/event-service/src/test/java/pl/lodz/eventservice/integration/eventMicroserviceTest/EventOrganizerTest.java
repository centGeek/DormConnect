package pl.lodz.eventservice.integration.eventMicroserviceTest;

import com.fasterxml.jackson.core.JsonProcessingException;
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

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@Import(PostgresContainerConfig.class)
public class EventOrganizerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private JwtService jwtService;


    @LocalServerPort
    private int port;

    private String baseUrl() {
        return "http://localhost:" + port + "/api/event/organizer";
    }

    private HttpHeaders authHeaderForUser(Long userId) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + jwtService
                .generateToken(userId, "user" + userId + "@email.com", List.of("STUDENT")));
        return headers;
    }

    private HttpHeaders authHeaderWithToken(String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", token);
        return headers;
    }

    private EventCreateDTO validEventCreateDTO() {
        return new EventCreateDTO(
                "Test Event",
                "Valid description",
                LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(1).plusHours(2),
                "Łódź",
                "Meetup",
                100,
                "http://img.com/pic.png",
                1L,
                List.of()
        );
    }

    @Test
    @DisplayName("Should get page of future events for organizer by userId")
    public void shouldGetPageOfFutureEvents() {
        HttpHeaders headers = authHeaderForUser(1L);
        HttpEntity<Void> request = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(
                baseUrl() + "?page=0&size=10",
                HttpMethod.GET,
                request,
                String.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().contains("\"content\""));
    }

    @Test
    @DisplayName("Should return empty page when no future events for organizer")
    public void shouldReturnEmptyIfNoFutureEvents() {
        HttpHeaders headers = authHeaderForUser(9999L);
        HttpEntity<Void> request = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(
                baseUrl(),
                HttpMethod.GET,
                request,
                String.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().contains("\"content\":[]") || response.getBody().contains("\"totalElements\":0"));
    }

    @Test
    @DisplayName("Should return INTERNAL_SERVER_ERROR on invalid token")
    public void shouldReturnInternalServerErrorOnInvalidToken() {
        HttpHeaders headers = authHeaderWithToken("Bearer invalid.token.here");
        HttpEntity<Void> request = new HttpEntity<>(headers);

        ResponseEntity<Void> response = restTemplate.exchange(
                baseUrl(),
                HttpMethod.GET,
                request,
                Void.class);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    @DisplayName("Should return OK if Authorization header missing Bearer prefix")
    public void shouldReturnInternalServerErrorIfMissingBearerPrefix() {
        // Token bez "Bearer "
        String token = jwtService.generateToken(1L, "user1@email.com", List.of("STUDENT"));
        HttpHeaders headers = authHeaderWithToken(token);
        HttpEntity<Void> request = new HttpEntity<>(headers);

        ResponseEntity<Void> response = restTemplate.exchange(
                baseUrl(),
                HttpMethod.GET,
                request,
                Void.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    @DisplayName("Should return INTERNAL_SERVER_ERROR if Authorization header empty")
    public void shouldReturnInternalServerErrorIfAuthorizationEmpty() {
        HttpHeaders headers = authHeaderWithToken("");
        HttpEntity<Void> request = new HttpEntity<>(headers);

        ResponseEntity<Void> response = restTemplate.exchange(
                baseUrl(),
                HttpMethod.GET,
                request,
                Void.class);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    @DisplayName("Should return BAD_REQUEST if Authorization header missing")
    public void shouldReturnBadRequestIfAuthorizationMissing() {
        HttpHeaders headers = new HttpHeaders(); // brak Authorization
        HttpEntity<Void> request = new HttpEntity<>(headers);

        ResponseEntity<Void> response = restTemplate.exchange(
                baseUrl(),
                HttpMethod.GET,
                request,
                Void.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    @DisplayName("Should return false if user has no organized events")
    public void shouldReturnFalseIfNoEvents() {
        HttpHeaders headers = authHeaderForUser(9999L);
        HttpEntity<Void> request = new HttpEntity<>(headers);

        ResponseEntity<Boolean> response = restTemplate.exchange(
                baseUrl() + "/has-events",
                HttpMethod.GET,
                request,
                Boolean.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotEquals(Boolean.TRUE, response.getBody());
    }

    @Test
    @DisplayName("Should return true if user has organized events")
    public void shouldReturnTrueIfHasEvents() throws JsonProcessingException {
        EventCreateDTO dto = validEventCreateDTO();
        HttpHeaders createHeaders = new HttpHeaders();
        createHeaders.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> createRequest = new HttpEntity<>(objectMapper.writeValueAsString(dto), createHeaders);
        ResponseEntity<EventDTO> createResponse = restTemplate.postForEntity("http://localhost:" + port + "/api/event/create", createRequest, EventDTO.class);

        assertEquals(HttpStatus.CREATED, createResponse.getStatusCode());
        assertNotNull(createResponse.getBody());
        assertEquals(dto.eventName(), createResponse.getBody().eventName());

        HttpHeaders headers = authHeaderForUser(1L);
        HttpEntity<Void> request = new HttpEntity<>(headers);

        ResponseEntity<Boolean> response = restTemplate.exchange(
                baseUrl() + "/has-events",
                HttpMethod.GET,
                request,
                Boolean.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(Boolean.TRUE, response.getBody());
    }

    @Test
    @DisplayName("Should return INTERNAL_SERVER_ERROR on invalid token for has-events endpoint")
    public void shouldReturnInternalServerErrorOnInvalidTokenHasEvents() {
        HttpHeaders headers = authHeaderWithToken("Bearer invalid.token.here");
        HttpEntity<Void> request = new HttpEntity<>(headers);

        ResponseEntity<Void> response = restTemplate.exchange(
                baseUrl() + "/has-events",
                HttpMethod.GET,
                request,
                Void.class);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }
}
