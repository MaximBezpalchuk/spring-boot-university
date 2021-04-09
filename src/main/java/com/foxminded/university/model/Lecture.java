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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((audience == null) ? 0 : audience.hashCode());
		result = prime * result + ((date == null) ? 0 : date.hashCode());
		result = prime * result + ((groups == null) ? 0 : groups.hashCode());
		result = prime * result + ((subject == null) ? 0 : subject.hashCode());
		result = prime * result + ((teacher == null) ? 0 : teacher.hashCode());
		result = prime * result + ((time == null) ? 0 : time.hashCode());
		return result;
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
		if (audience == null) {
			if (other.audience != null)
				return false;
		} else if (!audience.equals(other.audience))
			return false;
		if (date == null) {
			if (other.date != null)
				return false;
		} else if (!date.equals(other.date))
			return false;
		if (groups == null) {
			if (other.groups != null)
				return false;
		} else if (!groups.equals(other.groups))
			return false;
		if (subject == null) {
			if (other.subject != null)
				return false;
		} else if (!subject.equals(other.subject))
			return false;
		if (teacher == null) {
			if (other.teacher != null)
				return false;
		} else if (!teacher.equals(other.teacher))
			return false;
		if (time == null) {
			if (other.time != null)
				return false;
		} else if (!time.equals(other.time))
			return false;
		return true;
	}

}
