package pl.lodz.dormConnect.events.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.LocalDateTime;
import java.util.List;


public record EventDTO(
        Long eventId,
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
) {}
