package pl.lodz.dormConnect.events.mapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import pl.lodz.dormConnect.events.controller.EventController;
import pl.lodz.dormConnect.events.dto.EventDTO;
import pl.lodz.dormConnect.events.model.EventEntity;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class EventMapper {
    private static final Logger logger = LoggerFactory.getLogger(EventMapper.class);

    public EventDTO mapToDTO(EventEntity eventEntity) {
        return new EventDTO(
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

    public EventEntity mapToEntity(EventDTO eventDTO) {
        logger.error("Error creating event: {}", eventDTO);
        return new EventEntity(
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
                eventDTO.participantId()
        );
    }

    public List<EventDTO> mapToDTOList(List<EventEntity> eventEntities) {
        return eventEntities.stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public List<EventEntity> mapToEntityList(List<EventDTO> eventDTOs) {
        return eventDTOs.stream()
                .map(this::mapToEntity)
                .collect(Collectors.toList());
    }
}
