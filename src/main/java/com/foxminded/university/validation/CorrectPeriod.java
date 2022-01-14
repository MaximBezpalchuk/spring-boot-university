package com.foxminded.university.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = {CorrectPeriodValidator.class})
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface CorrectPeriod {

    String message() default "{End date can`t be before start date!}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
