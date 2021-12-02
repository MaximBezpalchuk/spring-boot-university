package com.foxminded.university.formatter;

import com.foxminded.university.model.Subject;
import org.springframework.format.Formatter;

import java.text.ParseException;
import java.util.Locale;

public class SubjectFormatter implements Formatter<Subject> {

    @Override
    public String print(Subject subject, Locale locale) {
        return Long.toString(subject.getId());
    }

    @Override
    public Subject parse(String id, Locale locale) throws ParseException {
        return Subject.builder().id(Integer.parseInt(id)).build();
    }

}
