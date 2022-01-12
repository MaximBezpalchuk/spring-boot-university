package com.foxminded.university.validation;

import com.foxminded.university.model.Person;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;
import java.time.Period;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PhoneNumberValidator implements ConstraintValidator<PhoneNumber, String> {

    @Override
    public boolean isValid(String phoneNumber, ConstraintValidatorContext context) {
        Pattern pattern = Pattern.compile("^[0-9]{1,11}$");
        Matcher matcher = pattern.matcher(phoneNumber);

        return (matcher.matches());
    }
}
