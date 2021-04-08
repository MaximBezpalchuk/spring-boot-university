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

}
