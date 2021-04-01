package com.foxminded.university.model;

import java.time.LocalDate;
import java.util.List;

public class Teacher extends Person {

	private Cathedra cathedra;
	private List<Subject> subjects;
	private List<Vacation> vacations;
	private String degree;

	public Teacher(String firstName, String lastName, String phone, String address, String email, String gender, String postalCode,
			String education, LocalDate birthDate, Cathedra cathedra) {
		super(firstName, lastName, phone, address, email, gender, postalCode, education, birthDate);
		this.cathedra = cathedra;
	}
	
	public Cathedra getCathedra() {
		return cathedra;
	}

	public List<Subject> getSubjects() {
		return subjects;
	}

	public void setSubjects(List<Subject> subjects) {
		this.subjects = subjects;
	}

	public List<Vacation> getVacations() {
		return vacations;
	}

	public void setVacations(List<Vacation> vacations) {
		this.vacations = vacations;
	}

	public String getDegree() {
		return degree;
	}

	public void setDegree(String degree) {
		this.degree = degree;
	}
}
