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

import static org.assertj.core.api.Assertions.assertThat;

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

    @Test
    @DisplayName("toEventDTO should correctly map EventEntity to EventDTO")
    void testToEventDTO() {
        EventEntity entity = sampleEntity();
        EventDTO dto = eventMapper.toEventDTO(entity);

        assertThat(dto.eventId()).isEqualTo(entity.getEventId());
        assertThat(dto.eventName()).isEqualTo(entity.getEventName());
        assertThat(dto.description()).isEqualTo(entity.getDescription());
        assertThat(dto.startDateTime()).isEqualTo(entity.getStartDateTime());
        assertThat(dto.endDateTime()).isEqualTo(entity.getEndDateTime());
        assertThat(dto.location()).isEqualTo(entity.getLocation());
        assertThat(dto.eventType()).isEqualTo(entity.getEventType());
        assertThat(dto.maxParticipants()).isEqualTo(entity.getMaxParticipants());
        assertThat(dto.imageUrl()).isEqualTo(entity.getImageUrl());
        assertThat(dto.organizerId()).isEqualTo(entity.getOrganizerId());
        assertThat(dto.approvalStatus()).isEqualTo(entity.getApprovalStatus());
        assertThat(dto.participantId()).containsExactlyElementsOf(entity.getParticipantId());
    }

    @Test
    @DisplayName("toEventCreateDTO should correctly map EventEntity to EventCreateDTO")
    void testToEventCreateDTO() {
        EventEntity entity = sampleEntity();
        EventCreateDTO createDTO = eventMapper.toEventCreateDTO(entity);

        assertThat(createDTO.eventName()).isEqualTo(entity.getEventName());
        assertThat(createDTO.description()).isEqualTo(entity.getDescription());
        assertThat(createDTO.startDateTime()).isEqualTo(entity.getStartDateTime());
        assertThat(createDTO.endDateTime()).isEqualTo(entity.getEndDateTime());
        assertThat(createDTO.location()).isEqualTo(entity.getLocation());
        assertThat(createDTO.eventType()).isEqualTo(entity.getEventType());
        assertThat(createDTO.maxParticipants()).isEqualTo(entity.getMaxParticipants());
        assertThat(createDTO.imageUrl()).isEqualTo(entity.getImageUrl());
        assertThat(createDTO.organizerId()).isEqualTo(entity.getOrganizerId());
        assertThat(createDTO.participantId()).containsExactlyElementsOf(entity.getParticipantId());
    }

    @Test
    @DisplayName("toEntity should correctly map EventDTO to EventEntity")
    void testToEntityFromDTO() {
        EventDTO dto = sampleDTO();
        EventEntity entity = eventMapper.toEntity(dto);

        assertThat(entity.getEventId()).isEqualTo(dto.eventId());
        assertThat(entity.getEventName()).isEqualTo(dto.eventName());
        assertThat(entity.getDescription()).isEqualTo(dto.description());
        assertThat(entity.getStartDateTime()).isEqualTo(dto.startDateTime());
        assertThat(entity.getEndDateTime()).isEqualTo(dto.endDateTime());
        assertThat(entity.getLocation()).isEqualTo(dto.location());
        assertThat(entity.getEventType()).isEqualTo(dto.eventType());
        assertThat(entity.getMaxParticipants()).isEqualTo(dto.maxParticipants());
        assertThat(entity.getImageUrl()).isEqualTo(dto.imageUrl());
        assertThat(entity.getOrganizerId()).isEqualTo(dto.organizerId());
        assertThat(entity.getApprovalStatus()).isEqualTo(dto.approvalStatus());
        assertThat(entity.getParticipantId()).containsExactlyElementsOf(dto.participantId());
    }

    @Test
    @DisplayName("toEntity should correctly map EventCreateDTO to EventEntity with ApprovalStatus WAITING")
    void testToEntityFromCreateDTO() {
        EventCreateDTO createDTO = sampleCreateDTO();
        EventEntity entity = eventMapper.toEntity(createDTO);

        assertThat(entity.getEventId()).isNull();
        assertThat(entity.getEventName()).isEqualTo(createDTO.eventName());
        assertThat(entity.getDescription()).isEqualTo(createDTO.description());
        assertThat(entity.getStartDateTime()).isEqualTo(createDTO.startDateTime());
        assertThat(entity.getEndDateTime()).isEqualTo(createDTO.endDateTime());
        assertThat(entity.getLocation()).isEqualTo(createDTO.location());
        assertThat(entity.getEventType()).isEqualTo(createDTO.eventType());
        assertThat(entity.getMaxParticipants()).isEqualTo(createDTO.maxParticipants());
        assertThat(entity.getImageUrl()).isEqualTo(createDTO.imageUrl());
        assertThat(entity.getOrganizerId()).isEqualTo(createDTO.organizerId());
        assertThat(entity.getApprovalStatus()).isEqualTo(ApprovalStatus.WAITING);
        assertThat(entity.getParticipantId()).containsExactlyElementsOf(createDTO.participantId());
    }

    @Test
    @DisplayName("toEventDTOList should map list of EventEntity to list of EventDTO")
    void testToEventDTOList() {
        List<EventEntity> entities = List.of(sampleEntity());
        List<EventDTO> dtos = eventMapper.toEventDTOList(entities);

        assertThat(dtos).hasSize(1);
        assertThat(dtos.getFirst().eventId()).isEqualTo(entities.getFirst().getEventId());
    }

    @Test
    @DisplayName("toEntityList should map list of EventDTO to list of EventEntity")
    void testToEntityList() {
        List<EventDTO> dtos = List.of(sampleDTO());
        List<EventEntity> entities = eventMapper.toEntityList(dtos);

        assertThat(entities).hasSize(1);
        assertThat(entities.getFirst().getEventId()).isEqualTo(dtos.getFirst().eventId());
    }
}
