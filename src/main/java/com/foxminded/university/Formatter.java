package com.foxminded.university;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import com.foxminded.university.model.Group;
import com.foxminded.university.model.Lecture;
import com.foxminded.university.model.Subject;

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
						"Date: %s | Subject: %-18s | Audience: %d | Teacher: %-15s", lecture.getDate(),
						lecture.getSubject().getName(), lecture.getAudience().getRoom(),
						lecture.getTeacher().getFirstName() + " " + lecture.getTeacher().getLastName()))
				.collect(Collectors.joining(System.lineSeparator()));
	}

	public String formatGroupList(List<Group> groups) {
		AtomicInteger index = new AtomicInteger();
		return groups.stream().map(group -> String.format("%d. %s", index.incrementAndGet(), group.getName()))
				.collect(Collectors.joining(System.lineSeparator()));
	}
	
	public String formatSubjectList(List<Subject> subjects) {
		AtomicInteger index = new AtomicInteger();
		return subjects.stream().map(subject -> String.format("%d. Name: %-7s |" + "Description: %s", index.incrementAndGet(), subject.getName(), subject.getDescription()))
				.collect(Collectors.joining(System.lineSeparator()));
	}
}
