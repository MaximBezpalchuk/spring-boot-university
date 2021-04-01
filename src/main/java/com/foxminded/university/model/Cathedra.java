package com.foxminded.university.model;

import java.util.List;
import java.util.Map;

public class Cathedra {

	private List<Group> groups;
	private List<Teacher> teachers;
	private Map<String, Lecture> lectures;
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

	public void setLectures(Map<String, Lecture> lectures) {
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

	public Map<String, Lecture> getLectures() {
		return lectures;
	}

	public List<Holiday> getHolidays() {
		return holidays;
	}

}
