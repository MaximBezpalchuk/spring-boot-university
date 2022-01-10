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
    public boolean isValid(Person person, ConstraintValidatorContext constraintValidatorContext) {
        if (person instanceof Student) {
            return Period.between(person.getBirthDate(), LocalDate.now()).getYears() > 14;
        } else if (person instanceof Teacher){
            return Period.between(person.getBirthDate(), LocalDate.now()).getYears() > 20;
        }

        return false;
    }
}
