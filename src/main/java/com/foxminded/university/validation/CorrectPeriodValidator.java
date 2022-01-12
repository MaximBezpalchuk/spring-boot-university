package com.foxminded.university.validation;

import com.foxminded.university.model.Person;
import com.foxminded.university.model.Vacation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;
import java.time.Period;

public class CorrectPeriodValidator implements ConstraintValidator<CorrectPeriod, Vacation> {

    @Override
    public boolean isValid(Vacation vacation, ConstraintValidatorContext context) {
        return vacation.getStart().isBefore(vacation.getEnd());
    }
}
