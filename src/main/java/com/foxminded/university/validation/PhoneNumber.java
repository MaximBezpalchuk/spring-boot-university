package com.foxminded.university.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.constraints.Pattern;
import java.lang.annotation.*;

@Pattern(regexp = "^[0-9]{1,13}$")
@Documented
@Constraint(validatedBy = {})
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface PhoneNumber {

    String message() default "{Phone number should contain only (1-11) digits!}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
