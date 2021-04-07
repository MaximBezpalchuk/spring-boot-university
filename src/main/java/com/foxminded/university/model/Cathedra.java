package com.foxminded.university.model;

import java.time.Month;
import java.time.MonthDay;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Cathedra {

	private List<Group> groups = new ArrayList<>();
	private List<Teacher> teachers = new ArrayList<>();
	private List<Lecture> lectures = new ArrayList<>();
	private List<Holiday> holidays = new ArrayList<>();
	private List<Subject> subjects = new ArrayList<>();
	private List<Audience> audiences = new ArrayList<>();

	public List<Lecture> getTTForDay(Student student, int day, int month) {
		MonthDay md = MonthDay.of(month, day);

		return student.getGroup().getLectures().stream().sorted((d1, d2) -> d1.getDate().compareTo(d2.getDate()))
				.filter(lecture -> lecture.getDate().getMonthValue() == md.getMonthValue()
						&& lecture.getDate().getDayOfMonth() == md.getDayOfMonth())
				.collect(Collectors.toList());
	}

	public List<Lecture> getTTForDay(Teacher teacher, int day, int month) {
		MonthDay md = MonthDay.of(month, day);

		return lectures.stream().sorted((d1, d2) -> d1.getDate().compareTo(d2.getDate()))
				.filter(lecture -> lecture.getTeacher().equals(teacher)
						&& (lecture.getDate().getMonthValue() == md.getMonthValue()
								&& lecture.getDate().getDayOfMonth() == md.getDayOfMonth()))
				.collect(Collectors.toList());
	}

	public List<Lecture> getTTForMonth(Student student, int month) {
		Month date = Month.of(month);

		return student.getGroup().getLectures().stream().sorted((d1, d2) -> d1.getDate().compareTo(d2.getDate()))
				.filter(lecture -> lecture.getDate().getMonth().equals(date)).collect(Collectors.toList());
	}

	public List<Lecture> getTTForMonth(Teacher teacher, int month) {
		Month date = Month.of(month);

		return lectures.stream().sorted((d1, d2) -> d1.getDate().compareTo(d2.getDate()))
				.filter(lecture -> (lecture.getTeacher().equals(teacher) && lecture.getDate().getMonth().equals(date)))
				.collect(Collectors.toList());
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
