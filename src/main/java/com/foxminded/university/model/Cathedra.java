package com.foxminded.university.model;

import java.util.ArrayList;
import java.util.List;

public class Cathedra {

	private List<Group> groups = new ArrayList<>();
	private List<Teacher> teachers = new ArrayList<>();
	private List<Lecture> lectures = new ArrayList<>();
	private List<Holiday> holidays = new ArrayList<>();
	private List<Subject> subjects = new ArrayList<>();
	private List<Audience> audiences = new ArrayList<>();
	private List<LectureTime> lectureTimes = new ArrayList<>();

	public List<LectureTime> getLectureTimes() {
		return lectureTimes;
	}

	public void setLectureTimes(List<LectureTime> lectureTimes) {
		this.lectureTimes = lectureTimes;
	}

	public List<Audience> getAudiences() {
		return audiences;
	}

	public void setAudiences(List<Audience> audiences) {
		this.audiences = audiences;
	}

	public List<Subject> getSubjects() {
		return subjects;
	}

	public void setSubjects(List<Subject> subjects) {
		this.subjects = subjects;
	}

	public List<Group> getGroups() {
		return groups;
	}

	public void setGroups(List<Group> groups) {
		this.groups = groups;
	}

	public void setLectures(List<Lecture> lectures) {
		this.lectures = lectures;
	}

	public void setHolidays(List<Holiday> holidays) {
		this.holidays = holidays;
	}

	public List<Teacher> getTeachers() {
		return teachers;
	}

	public void setTeachers(List<Teacher> teachers) {
		this.teachers = teachers;
	}

	public List<Lecture> getLectures() {
		return lectures;
	}

	public List<Holiday> getHolidays() {
		return holidays;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((audiences == null) ? 0 : audiences.hashCode());
		result = prime * result + ((groups == null) ? 0 : groups.hashCode());
		result = prime * result + ((holidays == null) ? 0 : holidays.hashCode());
		result = prime * result + ((lectureTimes == null) ? 0 : lectureTimes.hashCode());
		result = prime * result + ((lectures == null) ? 0 : lectures.hashCode());
		result = prime * result + ((subjects == null) ? 0 : subjects.hashCode());
		result = prime * result + ((teachers == null) ? 0 : teachers.hashCode());
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
		Cathedra other = (Cathedra) obj;
		if (audiences == null) {
			if (other.audiences != null)
				return false;
		} else if (!audiences.equals(other.audiences))
			return false;
		if (groups == null) {
			if (other.groups != null)
				return false;
		} else if (!groups.equals(other.groups))
			return false;
		if (holidays == null) {
			if (other.holidays != null)
				return false;
		} else if (!holidays.equals(other.holidays))
			return false;
		if (lectureTimes == null) {
			if (other.lectureTimes != null)
				return false;
		} else if (!lectureTimes.equals(other.lectureTimes))
			return false;
		if (lectures == null) {
			if (other.lectures != null)
				return false;
		} else if (!lectures.equals(other.lectures))
			return false;
		if (subjects == null) {
			if (other.subjects != null)
				return false;
		} else if (!subjects.equals(other.subjects))
			return false;
		if (teachers == null) {
			if (other.teachers != null)
				return false;
		} else if (!teachers.equals(other.teachers))
			return false;
		return true;
	}

}
