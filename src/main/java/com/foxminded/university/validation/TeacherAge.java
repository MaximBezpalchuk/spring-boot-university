package com.foxminded.university.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = {TeacherAgeValidator.class})
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)

public @interface TeacherAge {

    String message() default "{Age should be more than 20!}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
