package com.foxminded.university;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import com.foxminded.university.model.Lecture;

public class Formatter {

	private List<Lecture> sortByDate(List<Lecture> lectures) {
		Comparator<Lecture> comporator = (d1, d2) -> d1.getDate().compareTo(d2.getDate());
		Collections.sort(lectures, comporator);
		return lectures;
	}

	public String format(List<Lecture> lectures) {
		List<Lecture> formattedLectures = sortByDate(lectures);
		return formattedLectures.stream()
				.map(lecture -> String.format(
						"Date: %s | " + "Subject: %-18s | " + "Audience: %d | " + "Teacher: %-15s", lecture.getDate(),
						lecture.getSubject().getName(), lecture.getAudience().getRoom(),
						lecture.getTeacher().getFirstName() + " " + lecture.getTeacher().getLastName()))
				.collect(Collectors.joining(System.lineSeparator()));
	}
}
