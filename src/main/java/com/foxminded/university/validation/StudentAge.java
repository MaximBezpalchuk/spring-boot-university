package com.foxminded.university.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = {StudentAgeValidator.class})
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)

public @interface StudentAge {

    String message() default "{Age should be more than 14!}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
