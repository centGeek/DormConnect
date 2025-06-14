package pl.lodz.eventservice.unit.validation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pl.lodz.eventservice.dto.EventCreateDTO;
import pl.lodz.eventservice.dto.EventDTO;
import pl.lodz.eventservice.validation.EventDatesValidator;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class EventDatesValidatorTest {

    private EventDatesValidator validator;

    @BeforeEach
    void setup() {
        validator = new EventDatesValidator();
    }

    @Test
    @DisplayName("Should return true when end date is after start date for EventCreateDTO")
    void isValid_returnsTrue_whenEndDateIsAfterStartDate_forEventCreateDTO() {
        EventCreateDTO dto = new EventCreateDTO(
                "Event",
                "Desc",
                LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(2),
                "Loc",
                "Type",
                10,
                "url",
                1L,
                List.of()
        );

        assertTrue(validator.isValid(dto, null));
    }

    @Test
    @DisplayName("Should return false when end date is before start date for EventCreateDTO")
    void isValid_returnsFalse_whenEndDateIsBeforeStartDate_forEventCreateDTO() {
        EventCreateDTO dto = new EventCreateDTO(
                "Event",
                "Desc",
                LocalDateTime.now().plusDays(2),
                LocalDateTime.now().plusDays(1),
                "Loc",
                "Type",
                10,
                "url",
                1L,
                List.of()
        );

        assertFalse(validator.isValid(dto, null));
    }

    @Test
    @DisplayName("Should return true when end date is after start date for EventDTO")
    void isValid_returnsTrue_whenEndDateIsAfterStartDate_forEventDTO() {
        EventDTO dto = new EventDTO(
                1L,
                "Event",
                "Desc",
                LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(3),
                "Loc",
                "Type",
                10,
                "url",
                1L,
                null,
                List.of()
        );

        assertTrue(validator.isValid(dto, null));
    }

    @Test
    @DisplayName("Should return false when end date is before start date for EventDTO")
    void isValid_returnsFalse_whenEndDateIsBeforeStartDate_forEventDTO() {
        EventDTO dto = new EventDTO(
                1L,
                "Event",
                "Desc",
                LocalDateTime.now().plusDays(3),
                LocalDateTime.now().plusDays(1),
                "Loc",
                "Type",
                10,
                "url",
                1L,
                null,
                List.of()
        );

        assertFalse(validator.isValid(dto, null));
    }

    @Test
    @DisplayName("Should return true when start or end dates are null")
    void isValid_returnsTrue_whenDatesAreNull() {
        EventCreateDTO dto = new EventCreateDTO(
                "Event",
                "Desc",
                null,
                null,
                "Loc",
                "Type",
                10,
                "url",
                1L,
                List.of()
        );

        assertTrue(validator.isValid(dto, null));
    }

    @Test
    @DisplayName("Should return true when DTO is null")
    void isValid_returnsTrue_whenDtoIsNull() {
        assertTrue(validator.isValid(null, null));
    }
}
