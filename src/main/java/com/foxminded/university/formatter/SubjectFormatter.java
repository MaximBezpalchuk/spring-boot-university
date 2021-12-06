package com.foxminded.university.formatter;

import java.text.ParseException;
import java.util.Locale;

import org.springframework.format.Formatter;

import com.foxminded.university.model.Subject;

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
