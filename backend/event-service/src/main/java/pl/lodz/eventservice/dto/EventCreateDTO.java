package pl.lodz.eventservice.dto;

import jakarta.validation.constraints.*;
import pl.lodz.eventservice.validation.ValidEventDates;

import java.time.LocalDateTime;
import java.util.List;

@ValidEventDates
public record EventCreateDTO(
        @NotBlank(message = "Event name cannot be blank.")
        @Size(min = 3, max = 100, message = "Event name must be between 3 and 100 characters.")
        String eventName,

        @Size(max = 500, message = "Description cannot be longer than 500 characters.")
        String description,

        @NotNull(message = "Start date must be provided.")
        @Future(message = "Dzień startu musi być w przyszłości")
        LocalDateTime startDateTime,

        @NotNull(message = "End date must be provided.")
        LocalDateTime endDateTime,

        @NotBlank(message = "Location cannot be blank.")
        String location,

        @NotBlank(message = "Event type cannot be blank.")
        String eventType,

        @NotNull(message = "Max participants must be provided.")
        @Min(value = 1, message = "There must be at least 1 participant.")
        Integer maxParticipants,

        String imageUrl,

        @NotNull(message = "Organizer ID must be provided.")
        Long organizerId,

        List<Long> participantId
) {}
