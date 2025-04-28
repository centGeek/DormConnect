package pl.lodz.dormConnect.events.mapper;

import org.springframework.stereotype.Component;
import pl.lodz.dormConnect.events.dto.EventCreateDTO;
import pl.lodz.dormConnect.events.dto.EventDTO;
import pl.lodz.dormConnect.events.model.EventEntity;

import java.util.List;

@Component
public class EventMapper {
    public EventDTO toEventDTO(EventEntity eventEntity) {
        return new EventDTO(
                eventEntity.getEventId(),
                eventEntity.getEventName(),
                eventEntity.getDescription(),
                eventEntity.getStartDateTime(),
                eventEntity.getEndDateTime(),
                eventEntity.getLocation(),
                eventEntity.getEventType(),
                eventEntity.getMaxParticipants(),
                eventEntity.getImageUrl(),
                eventEntity.getOrganizerId(),
                eventEntity.getIsApproved(),
                eventEntity.getParticipantId()
        );
    }

    public EventCreateDTO toEventCreateDTO(EventEntity eventEntity) {
        return new EventCreateDTO(
                eventEntity.getEventName(),
                eventEntity.getDescription(),
                eventEntity.getStartDateTime(),
                eventEntity.getEndDateTime(),
                eventEntity.getLocation(),
                eventEntity.getEventType(),
                eventEntity.getMaxParticipants(),
                eventEntity.getImageUrl(),
                eventEntity.getOrganizerId(),
                eventEntity.getParticipantId()
        );
    }


    public EventEntity toEntity(EventDTO eventDTO) {
        return new EventEntity(
                eventDTO.eventId(),
                null,
                eventDTO.eventName(),
                eventDTO.description(),
                eventDTO.startDateTime(),
                eventDTO.endDateTime(),
                eventDTO.location(),
                eventDTO.eventType(),
                eventDTO.maxParticipants(),
                eventDTO.imageUrl(),
                eventDTO.organizerId(),
                eventDTO.isApproved(),
                eventDTO.participantId()
        );
    }

    public EventEntity toEntity(EventCreateDTO eventCreateDTO) {
        return new EventEntity(
                null,
                null,
                eventCreateDTO.eventName(),
                eventCreateDTO.description(),
                eventCreateDTO.startDateTime(),
                eventCreateDTO.endDateTime(),
                eventCreateDTO.location(),
                eventCreateDTO.eventType(),
                eventCreateDTO.maxParticipants(),
                eventCreateDTO.imageUrl(),
                eventCreateDTO.organizerId(),
                false,
                eventCreateDTO.participantId()
        );
    }

    public List<EventDTO> toEventDTOList(List<EventEntity> eventEntities) {
        return eventEntities.stream()
                .map(this::toEventDTO)
                .toList();
    }

    public List<EventEntity> toEntityList(List<EventDTO> eventDTOs) {
        return eventDTOs.stream()
                .map(this::toEntity)
                .toList();
    }

}
