package pl.lodz.dormConnect.events.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import pl.lodz.dormConnect.events.dto.EventCreateDTO;
import pl.lodz.dormConnect.events.dto.EventDTO;

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
