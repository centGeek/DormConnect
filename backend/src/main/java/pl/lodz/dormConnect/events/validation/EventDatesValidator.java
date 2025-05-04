package pl.lodz.dormConnect.events.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import pl.lodz.dormConnect.events.dto.EventCreateDTO;

public class EventDatesValidator implements ConstraintValidator<ValidEventDates, EventCreateDTO> {

    @Override
    public boolean isValid(EventCreateDTO dto, ConstraintValidatorContext context) {
        if (dto == null) {
            return true;
        }
        if (dto.startDateTime() == null || dto.endDateTime() == null) {
            return true;
        }
        return dto.endDateTime().isAfter(dto.startDateTime());
    }
}

