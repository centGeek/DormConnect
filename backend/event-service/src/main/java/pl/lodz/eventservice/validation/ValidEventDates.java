package pl.lodz.eventservice.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = EventDatesValidator.class)
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidEventDates {
    String message() default "End date must be after start date.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
