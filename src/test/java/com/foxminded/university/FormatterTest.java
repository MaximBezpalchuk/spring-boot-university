package com.foxminded.university;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.foxminded.university.model.Audience;
import com.foxminded.university.model.Cathedra;
import com.foxminded.university.model.Lecture;
import com.foxminded.university.model.LectureTime;
import com.foxminded.university.model.Subject;
import com.foxminded.university.model.Teacher;

class FormatterTest {

	@Test
	void printResultTest() {
		List<Lecture> lectures = new ArrayList<>();
		Subject subject = new Subject("TestSubject", "Desc");
		LocalDate date = LocalDate.of(2020, 1, 1);
		LectureTime time = new LectureTime();
		time.setStart(LocalTime.of(10, 20));
		time.setEnd(LocalTime.of(10, 21));
		Audience audience = new Audience(1, 5);
		Cathedra cathedra = new Cathedra();
		Teacher teacher = new Teacher("Amigo", "Bueno", "999", "Puerto Rico", "dot@dot.com", "shemale", "123", "none",
				LocalDate.of(1999, 1, 1), cathedra);
		lectures.add(new Lecture(subject, date, time, audience, teacher));
		Formatter formatter = new Formatter();
		assertEquals("Date: 2020-01-01 | Subject: TestSubject        | Audience: 1 | Teacher: Amigo Bueno    ",
				formatter.format(lectures));
	}
}
