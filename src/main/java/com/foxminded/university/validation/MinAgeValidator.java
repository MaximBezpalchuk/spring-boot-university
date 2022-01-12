package com.foxminded.university.validation;

import com.foxminded.university.model.Person;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;
import java.time.Period;

public class MinAgeValidator implements ConstraintValidator<MinAge, Person> {

    private int value;

    @Override
    public void initialize(MinAge annotation) {
        this.value = annotation.value();
    }

    @Override
    public boolean isValid(Person person, ConstraintValidatorContext context) {
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate("Person`s age should be more than " + value).addConstraintViolation();

        return Period.between(person.getBirthDate(), LocalDate.now()).getYears() > value;
    }
}
