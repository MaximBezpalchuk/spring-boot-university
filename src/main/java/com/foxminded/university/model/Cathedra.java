package com.foxminded.university.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Cathedra {
	private int id;

	private String name;
	private List<Group> groups = new ArrayList<>();
	private List<Teacher> teachers = new ArrayList<>();
	private List<Lecture> lectures = new ArrayList<>();
	private List<Holiday> holidays = new ArrayList<>();
	private List<Subject> subjects = new ArrayList<>();
	private List<Audience> audiences = new ArrayList<>();
	private List<LectureTime> lectureTimes = new ArrayList<>();

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Cathedra(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

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
		return Objects.hash(audiences, groups, holidays, id, lectureTimes, lectures, name, subjects, teachers);
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
		return Objects.equals(audiences, other.audiences) && Objects.equals(groups, other.groups)
				&& Objects.equals(holidays, other.holidays) && id == other.id
				&& Objects.equals(lectureTimes, other.lectureTimes) && Objects.equals(lectures, other.lectures)
				&& Objects.equals(name, other.name) && Objects.equals(subjects, other.subjects)
				&& Objects.equals(teachers, other.teachers);
	}

}
