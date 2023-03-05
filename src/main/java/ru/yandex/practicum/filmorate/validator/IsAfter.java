package ru.yandex.practicum.filmorate.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = DateValidator.class)
@Documented
public @interface IsAfter{
    String message() default "Date must be after {fromDate}";
    String fromDate();
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}