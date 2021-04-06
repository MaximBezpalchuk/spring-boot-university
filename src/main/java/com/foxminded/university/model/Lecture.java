package com.foxminded.university.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Lecture {

	private List<Group> groups = new ArrayList<>();
	private Teacher teacher;
	private Audience audience;
	private LocalDate date;
	private Subject subject;
	private LectureTime time;

	public Lecture(Subject subject, LocalDate date, LectureTime time, Audience audience, Teacher teacher) {
		this.subject = subject;
		this.date = date;
		this.time = time;
		this.audience = audience;
		this.teacher = teacher;
	}

	public List<Group> getGroups() {
		return groups;
	}

	public void setGroups(List<Group> groups) {
		this.groups = groups;
	}

	public Teacher getTeacher() {
		return teacher;
	}

	public void setAudience(Audience audience) {
		this.audience = audience;
	}

	public Audience getAudience() {
		return audience;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

	public void setTime(LectureTime time) {
		this.time = time;
	}

	public LocalDate getDate() {
		return date;
	}

	public Subject getSubject() {
		return subject;
	}

	public LectureTime getTime() {
		return time;
	}

}
