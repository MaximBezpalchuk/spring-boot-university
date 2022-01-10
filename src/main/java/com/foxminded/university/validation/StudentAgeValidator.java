package com.foxminded.university.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;
import java.time.Period;

public class StudentAgeValidator implements ConstraintValidator<StudentAge, LocalDate> {

    @Override
    public boolean isValid(LocalDate birthDate, ConstraintValidatorContext constraintValidatorContext) {
        return Period.between(birthDate, LocalDate.now()).getYears() > 14;
    }
}
