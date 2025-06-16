package pl.lodz.eventservice.unit.mapper;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pl.lodz.eventservice.dto.EventCreateDTO;
import pl.lodz.eventservice.dto.EventDTO;
import pl.lodz.eventservice.entity.ApprovalStatus;
import pl.lodz.eventservice.entity.EventEntity;
import pl.lodz.eventservice.mapper.EventMapper;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class EventMapperTest {

    private final EventMapper eventMapper = new EventMapper();

    private EventEntity sampleEntity() {
        return new EventEntity(
                1L,
                null,
                "EventName",
                "Description",
                LocalDateTime.of(2025, 6, 1, 10, 0),
                LocalDateTime.of(2025, 6, 2, 10, 0),
                "Location",
                "Type",
                100,
                "http://image.url",
                42L,
                ApprovalStatus.APPROVED,
                List.of(10L, 20L)
        );
    }

    private EventCreateDTO sampleCreateDTO() {
        return new EventCreateDTO(
                "EventName",
                "Description",
                LocalDateTime.of(2025, 6, 1, 10, 0),
                LocalDateTime.of(2025, 6, 2, 10, 0),
                "Location",
                "Type",
                100,
                "http://image.url",
                42L,
                List.of(10L, 20L)
        );
    }

    private EventDTO sampleDTO() {
        return new EventDTO(
                1L,
                "EventName",
                "Description",
                LocalDateTime.of(2025, 6, 1, 10, 0),
                LocalDateTime.of(2025, 6, 2, 10, 10, 0),
                "Location",
                "Type",
                100,
                "http://image.url",
                42L,
                ApprovalStatus.APPROVED,
                List.of(10L, 20L)
        );
    }

    @Test
    @DisplayName("toEventDTO should correctly map EventEntity to EventDTO")
    void testToEventDTO() {
        EventEntity entity = sampleEntity();
        EventDTO dto = eventMapper.toEventDTO(entity);

        assertEquals(entity.getEventId(), dto.eventId());
        assertEquals(entity.getEventName(), dto.eventName());
        assertEquals(entity.getDescription(), dto.description());
        assertEquals(entity.getStartDateTime(), dto.startDateTime());
        assertEquals(entity.getEndDateTime(), dto.endDateTime());
        assertEquals(entity.getLocation(), dto.location());
        assertEquals(entity.getEventType(), dto.eventType());
        assertEquals(entity.getMaxParticipants(), dto.maxParticipants());
        assertEquals(entity.getImageUrl(), dto.imageUrl());
        assertEquals(entity.getOrganizerId(), dto.organizerId());
        assertEquals(entity.getApprovalStatus(), dto.approvalStatus());
        assertIterableEquals(entity.getParticipantId(), dto.participantId());
    }

    @Test
    @DisplayName("toEventCreateDTO should correctly map EventEntity to EventCreateDTO")
    void testToEventCreateDTO() {
        EventEntity entity = sampleEntity();
        EventCreateDTO createDTO = eventMapper.toEventCreateDTO(entity);

        assertEquals(entity.getEventName(), createDTO.eventName());
        assertEquals(entity.getDescription(), createDTO.description());
        assertEquals(entity.getStartDateTime(), createDTO.startDateTime());
        assertEquals(entity.getEndDateTime(), createDTO.endDateTime());
        assertEquals(entity.getLocation(), createDTO.location());
        assertEquals(entity.getEventType(), createDTO.eventType());
        assertEquals(entity.getMaxParticipants(), createDTO.maxParticipants());
        assertEquals(entity.getImageUrl(), createDTO.imageUrl());
        assertEquals(entity.getOrganizerId(), createDTO.organizerId());
        assertIterableEquals(entity.getParticipantId(), createDTO.participantId());
    }

    @Test
    @DisplayName("toEntity should correctly map EventDTO to EventEntity")
    void testToEntityFromDTO() {
        EventDTO dto = sampleDTO();
        EventEntity entity = eventMapper.toEntity(dto);

        assertEquals(dto.eventId(), entity.getEventId());
        assertEquals(dto.eventName(), entity.getEventName());
        assertEquals(dto.description(), entity.getDescription());
        assertEquals(dto.startDateTime(), entity.getStartDateTime());
        assertEquals(dto.endDateTime(), entity.getEndDateTime());
        assertEquals(dto.location(), entity.getLocation());
        assertEquals(dto.eventType(), entity.getEventType());
        assertEquals(dto.maxParticipants(), entity.getMaxParticipants());
        assertEquals(dto.imageUrl(), entity.getImageUrl());
        assertEquals(dto.organizerId(), entity.getOrganizerId());
        assertEquals(dto.approvalStatus(), entity.getApprovalStatus());
        assertIterableEquals(dto.participantId(), entity.getParticipantId());
    }

    @Test
    @DisplayName("toEntity should correctly map EventCreateDTO to EventEntity with ApprovalStatus WAITING")
    void testToEntityFromCreateDTO() {
        EventCreateDTO createDTO = sampleCreateDTO();
        EventEntity entity = eventMapper.toEntity(createDTO);

        assertNull(entity.getEventId());
        assertEquals(createDTO.eventName(), entity.getEventName());
        assertEquals(createDTO.description(), entity.getDescription());
        assertEquals(createDTO.startDateTime(), entity.getStartDateTime());
        assertEquals(createDTO.endDateTime(), entity.getEndDateTime());
        assertEquals(createDTO.location(), entity.getLocation());
        assertEquals(createDTO.eventType(), entity.getEventType());
        assertEquals(createDTO.maxParticipants(), entity.getMaxParticipants());
        assertEquals(createDTO.imageUrl(), entity.getImageUrl());
        assertEquals(createDTO.organizerId(), entity.getOrganizerId());
        assertEquals(ApprovalStatus.WAITING, entity.getApprovalStatus());
        assertIterableEquals(createDTO.participantId(), entity.getParticipantId());
    }

    @Test
    @DisplayName("toEventDTOList should map list of EventEntity to list of EventDTO")
    void testToEventDTOList() {
        List<EventEntity> entities = List.of(sampleEntity());
        List<EventDTO> dtos = eventMapper.toEventDTOList(entities);

        assertEquals(1, dtos.size());
        assertEquals(entities.getFirst().getEventId(), dtos.getFirst().eventId());
    }

    @Test
    @DisplayName("toEntityList should map list of EventDTO to list of EventEntity")
    void testToEntityList() {
        List<EventDTO> dtos = List.of(sampleDTO());
        List<EventEntity> entities = eventMapper.toEntityList(dtos);

        assertEquals(1, entities.size());
        assertEquals(dtos.getFirst().eventId(), entities.getFirst().getEventId());
    }
}
