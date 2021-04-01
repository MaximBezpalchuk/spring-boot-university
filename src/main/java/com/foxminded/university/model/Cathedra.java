package com.foxminded.university.model;

import java.time.Month;
import java.time.MonthDay;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Cathedra {

	private List<Group> groups;
	private List<Teacher> teachers;
	private Map<String, Lecture> lectures;
	private List<Holiday> holidays;

	public List<Lecture> getTTForDay(Student student, int day, int month) {
		MonthDay md = MonthDay.of(month, day);
		List<Lecture> dayLecture = new ArrayList<>();
		for (Lecture lecture : student.getGroup().getLectures()) {
			if (lecture.getDate().getMonthValue() == md.getMonthValue()
					&& lecture.getDate().getDayOfMonth() == md.getDayOfMonth()) {
				dayLecture.add(lecture);
			}
		}
		return dayLecture;
	}

	public List<Lecture> getTTForDay(Teacher teacher, int day, int month) {
		MonthDay md = MonthDay.of(month, day);
		List<Lecture> dayLecture = new ArrayList<>();
		for (Map.Entry<String, Lecture> entry : lectures.entrySet()) {
			if (entry.getValue().getTeacher().equals(teacher)
					&& (entry.getValue().getDate().getMonthValue() == md.getMonthValue()
							&& entry.getValue().getDate().getDayOfMonth() == md.getDayOfMonth())) {
				dayLecture.add(entry.getValue());
			}
		}
		return dayLecture;
	}

	public List<Lecture> getTTForMonth(Student student, int month) {
		Month date = Month.of(month);
		List<Lecture> monthLecture = new ArrayList<>();
		for (Lecture lecture : student.getGroup().getLectures()) {
			if (lecture.getDate().getMonth().equals(date)) {
				monthLecture.add(lecture);
			}
		}
		return monthLecture;
	}

	public List<Lecture> getTTForMonth(Teacher teacher, int month) {
		Month date = Month.of(month);
		List<Lecture> monthLecture = new ArrayList<>();
		for (Map.Entry<String, Lecture> entry : lectures.entrySet()) {
			if (entry.getValue().getTeacher().equals(teacher) && entry.getValue().getDate().getMonth().equals(date)) {
				monthLecture.add(entry.getValue());
			}
		}
		return monthLecture;
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
