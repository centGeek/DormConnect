package pl.lodz.eventservice.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import pl.lodz.eventservice.dto.EventCreateDTO;
import pl.lodz.eventservice.dto.EventDTO;

import java.time.LocalDateTime;

public class EventDatesValidator implements ConstraintValidator<ValidEventDates, Object> {

    @Override
    public boolean isValid(Object dto, ConstraintValidatorContext context) {
        if (dto == null) {
            return true;
        }

        LocalDateTime startDateTime = switch (dto) {
            case EventCreateDTO eventCreateDTO -> eventCreateDTO.startDateTime();
            case EventDTO eventDTO -> eventDTO.startDateTime();
            default -> null;
        };

        LocalDateTime endDateTime = switch (dto) {
            case EventCreateDTO eventCreateDTO -> eventCreateDTO.endDateTime();
            case EventDTO eventDTO -> eventDTO.endDateTime();
            default -> null;
        };

        if (startDateTime == null || endDateTime == null) {
            return true;
        }

        return endDateTime.isAfter(startDateTime);
    }
}
