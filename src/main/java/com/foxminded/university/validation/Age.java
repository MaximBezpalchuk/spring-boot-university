package com.foxminded.university.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = {AgeValidator.class})
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)

public @interface Age {

    String message() default "{Wrong age entry!}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
