package com.foxminded.university.model;

import java.util.List;

public class Cathedra {

	private List<Group> groups;
	private List<Teacher> teachers;
	private List<Lecture> lectures;
	private List<Holiday> holidays;

	public void getTTForDay(Student student, int day, int month) {
	}

	public void getTTForDay(Teacher teacher, int day, int month) {
	}

	public void getTTForMonth(Student student, int month) {
	}

	public void getTTForMonth(Teacher teacher, int month) {
	}
	
	public List<Group> getGroups() {
		return groups;
	}

	public void setGroups(List<Group> groups) {
		this.groups = groups;
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

	public void setLectures(List<Lecture> lectures) {
		this.lectures = lectures;
	}

	public List<Holiday> getHolidays() {
		return holidays;
	}

	public void setHolidays(List<Holiday> holidays) {
		this.holidays = holidays;
	}
}
