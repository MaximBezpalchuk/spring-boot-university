package com.foxminded.university.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = {PhoneNumberValidator.class})
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface PhoneNumber {

    String message() default "{Phone number should contain only (1-11) digits!}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
