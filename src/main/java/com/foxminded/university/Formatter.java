package com.foxminded.university;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.foxminded.university.model.Lecture;

public class Formatter {
	
	private List<Lecture> format(List<Lecture> lectures) {
		Comparator<Lecture> comporator = (d1, d2) -> d1.getDate().compareTo(d2.getDate());
		Collections.sort(lectures, comporator);
		return lectures;
	}
	
	public void printResult(List<Lecture> lectures) {
		List<Lecture> formattedLectures = format(lectures);
		formattedLectures.forEach(lecture -> System.out.printf("Date: %s | " +  "Subject: %-18s | " + "Audience: %d | " + "Teacher: %-15s" + "%n", lecture.getDate(), lecture.getSubject().getName(), lecture.getAudience().getRoom(), lecture.getTeacher().getFirstName() + " " + lecture.getTeacher().getLastName()));
	}
}
