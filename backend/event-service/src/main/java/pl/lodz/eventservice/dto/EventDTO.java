package pl.lodz.eventservice.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import pl.lodz.eventservice.entity.ApprovalStatus;
import pl.lodz.eventservice.validation.ValidEventDates;

import java.time.LocalDateTime;
import java.util.List;


@ValidEventDates
public record EventDTO(
        Long eventId,

        @NotBlank(message = "Event name cannot be blank.")
        String eventName,

        @NotBlank
        String description,

        @NotNull(message = "Start date must be provided.")
        @Future(message = "Start date must be in the future.")
        LocalDateTime startDateTime,

        @NotNull(message = "End date must be provided.")
        LocalDateTime endDateTime,

        @NotBlank
        String location,

        @NotBlank
        String eventType,

        @NotNull(message = "Max participants must be provided.")
        @Min(value = 1, message = "There must be at least 1 participant.")
        Integer maxParticipants,

        @NotBlank
        String imageUrl,

        @NotNull(message = "Organizer ID must be provided.")
        Long organizerId,

        ApprovalStatus approvalStatus,
        List<Long> participantId
) {}
