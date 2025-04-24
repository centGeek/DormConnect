package pl.lodz.dormConnect.events.dto;

import java.time.LocalDateTime;
import java.util.List;

public record EventCreateDTO(
        String eventName,
        String description,
        LocalDateTime startDateTime,
        LocalDateTime endDateTime,
        String location,
        String eventType,
        Integer maxParticipants,
        String imageUrl,
        List<Long> organizerId,
        List<Long> participantId
)
{}
