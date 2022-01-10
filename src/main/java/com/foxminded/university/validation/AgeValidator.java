package com.foxminded.university.validation;

import com.foxminded.university.model.Person;
import com.foxminded.university.model.Student;
import com.foxminded.university.model.Teacher;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;
import java.time.Period;

public class AgeValidator implements ConstraintValidator<Age, Person> {

    @Override
    public boolean isValid(Person person, ConstraintValidatorContext context) {
        context.disableDefaultConstraintViolation();
        if (person instanceof Student) {
            context.buildConstraintViolationWithTemplate("Student`s age should be more than 14!").addConstraintViolation();

            return Period.between(person.getBirthDate(), LocalDate.now()).getYears() > 14;
        } else if (person instanceof Teacher) {
            context.buildConstraintViolationWithTemplate("Teacher`s age should be more than 20!").addConstraintViolation();

            return Period.between(person.getBirthDate(), LocalDate.now()).getYears() > 20;
        }

        return false;
    }
}
