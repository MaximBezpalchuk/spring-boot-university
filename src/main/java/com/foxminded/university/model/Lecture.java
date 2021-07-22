package com.foxminded.university.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Lecture {

	private Cathedra cathedra;
	private List<Group> groups = new ArrayList<>();
	private Teacher teacher;
	private Audience audience;
	private LocalDate date;
	private Subject subject;
	private LectureTime time;

	public Lecture(Cathedra cathedra, Subject subject, LocalDate date, LectureTime time, Audience audience, Teacher teacher) {
		this.cathedra = cathedra;
		this.subject = subject;
		this.date = date;
		this.time = time;
		this.audience = audience;
		this.teacher = teacher;
	}

	public Cathedra getCathedra() {
		return cathedra;
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

	@Override
	public int hashCode() {
		return Objects.hash(audience, cathedra, date, groups, subject, teacher, time);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Lecture other = (Lecture) obj;
		return Objects.equals(audience, other.audience) && Objects.equals(cathedra, other.cathedra)
				&& Objects.equals(date, other.date) && Objects.equals(groups, other.groups)
				&& Objects.equals(subject, other.subject) && Objects.equals(teacher, other.teacher)
				&& Objects.equals(time, other.time);
	}

}
