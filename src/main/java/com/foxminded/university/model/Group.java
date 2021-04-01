package com.foxminded.university.model;

import java.util.List;

public class Group {

	private List<Student> students;
	private Cathedra cathedra;
	private List<Lecture> lectures;
	private String name;
	
	public Group(String name, Cathedra cathedra) {
		this.name = name;
		this.cathedra = cathedra;
	}

	public List<Student> getStudents() {
		return students;
	}

	public void setStudents(List<Student> students) {
		this.students = students;
	}

	public Cathedra getCathedra() {
		return cathedra;
	}

	public List<Lecture> getLectures() {
		return lectures;
	}

	public void setLectures(List<Lecture> lectures) {
		this.lectures = lectures;
	}

	public String getName() {
		return name;
	}

}
