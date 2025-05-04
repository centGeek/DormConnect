package pl.lodz.dormConnect.events.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import pl.lodz.dormConnect.events.validation.ValidEventDates;

import java.time.LocalDateTime;
import java.util.List;


@ValidEventDates
public record EventDTO(
        Long eventId,
        @NotBlank(message = "Event name cannot be blank.")
        String eventName,
        String description,
        @NotNull(message = "Start date must be provided.")
        @Future(message = "Start date must be in the future.")
        LocalDateTime startDateTime,
        @NotNull(message = "End date must be provided.")
        LocalDateTime endDateTime,
        String location,
        String eventType,
        Integer maxParticipants,
        String imageUrl,
        Long organizerId,
        Boolean isApproved,
        List<Long> participantId
) {}
