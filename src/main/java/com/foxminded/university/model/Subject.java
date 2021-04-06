package com.foxminded.university.model;

import java.util.ArrayList;
import java.util.List;

public class Subject {

	private String name;
	private String description;
	private List<Teacher> teachers = new ArrayList<>();

	public Subject(String name, String description) {
		this.name = name;
		this.description = description;
	}

	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}

	public List<Teacher> getTeachers() {
		return teachers;
	}

	public void setTeachers(List<Teacher> teachers) {
		this.teachers = teachers;
	}

}
