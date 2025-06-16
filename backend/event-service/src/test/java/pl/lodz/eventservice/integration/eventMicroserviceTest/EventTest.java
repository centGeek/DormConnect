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
import pl.lodz.eventservice.entity.ApprovalStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@Import(PostgresContainerConfig.class)
public class EventTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @LocalServerPort
    private int port;

    private String baseUrl() {
        return "http://localhost:" + port + "/api/event";
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
    @DisplayName("Should create event successfully")
    public void shouldCreateEventSuccessfully() throws Exception {
        EventCreateDTO dto = validEventCreateDTO();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> request = new HttpEntity<>(objectMapper.writeValueAsString(dto), headers);
        ResponseEntity<EventDTO> response = restTemplate.postForEntity(baseUrl() + "/create", request, EventDTO.class);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(dto.eventName(), response.getBody().eventName());
    }

    @Test
    @DisplayName("Should return BAD_REQUEST for invalid event")
    public void shouldReturnBadRequestForInvalidEvent() throws Exception {
        EventCreateDTO dto = new EventCreateDTO("", "desc", LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(2), "", "", 0, "", null, List.of());
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> request = new HttpEntity<>(objectMapper.writeValueAsString(dto), headers);
        ResponseEntity<String> response = restTemplate.postForEntity(baseUrl() + "/create", request, String.class);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    @DisplayName("Should get all approved events")
    public void shouldGetAllApprovedEvents() {
        ResponseEntity<String> response = restTemplate.getForEntity(baseUrl(), String.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    @DisplayName("Should get event by ID")
    public void shouldGetEventById() throws Exception {
        EventCreateDTO dto = validEventCreateDTO();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> request = new HttpEntity<>(objectMapper.writeValueAsString(dto), headers);
        ResponseEntity<EventDTO> createResponse = restTemplate.postForEntity(baseUrl() + "/create", request, EventDTO.class);
        Long id = Objects.requireNonNull(createResponse.getBody()).eventId();

        ResponseEntity<EventDTO> getResponse = restTemplate.getForEntity(baseUrl() + "/" + id, EventDTO.class);
        assertEquals(HttpStatus.OK, getResponse.getStatusCode());
        assertEquals(id, Objects.requireNonNull(getResponse.getBody()).eventId());
    }

    @Test
    @DisplayName("Should return NOT_FOUND for missing event")
    public void shouldReturnNotFoundForMissingEvent() {
        ResponseEntity<EventDTO> response = restTemplate.getForEntity(baseUrl() + "/999999", EventDTO.class);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    @DisplayName("Should update event")
    public void shouldUpdateEvent() throws Exception {
        EventCreateDTO dto = validEventCreateDTO();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> request = new HttpEntity<>(objectMapper.writeValueAsString(dto), headers);
        ResponseEntity<EventDTO> createResponse = restTemplate.postForEntity(baseUrl() + "/create", request, EventDTO.class);
        Long id = Objects.requireNonNull(createResponse.getBody()).eventId();

        EventDTO updateDto = new EventDTO(id, "Updated Event", "Updated Desc",
                LocalDateTime.now().plusDays(2), LocalDateTime.now().plusDays(3),
                "Warsaw", "Conference", 200, "http://img.com/new.png",
                1L, ApprovalStatus.APPROVED, List.of());

        HttpEntity<String> updateRequest = new HttpEntity<>(objectMapper.writeValueAsString(updateDto), headers);
        ResponseEntity<EventDTO> updateResponse = restTemplate.exchange(
                baseUrl() + "/" + id, HttpMethod.PUT, updateRequest, EventDTO.class);

        assertEquals(HttpStatus.OK, updateResponse.getStatusCode());
        assertEquals("Updated Event", Objects.requireNonNull(updateResponse.getBody()).eventName());
    }

    @Test
    @DisplayName("Should delete event")
    public void shouldDeleteEvent() throws Exception {
        EventCreateDTO dto = validEventCreateDTO();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> request = new HttpEntity<>(objectMapper.writeValueAsString(dto), headers);
        ResponseEntity<EventDTO> createResponse = restTemplate.postForEntity(baseUrl() + "/create", request, EventDTO.class);
        Long id = Objects.requireNonNull(createResponse.getBody()).eventId();

        ResponseEntity<Void> deleteResponse = restTemplate.exchange(
                baseUrl() + "/" + id, HttpMethod.DELETE, null, Void.class);

        assertEquals(HttpStatus.NO_CONTENT, deleteResponse.getStatusCode());
    }

    @Test
    @DisplayName("Should return NOT_FOUND for deleting non-existing event")
    public void shouldReturnNotFoundForDeletingMissingEvent() {
        ResponseEntity<Void> response = restTemplate.exchange(
                baseUrl() + "/9999999", HttpMethod.DELETE, null, Void.class);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
}
