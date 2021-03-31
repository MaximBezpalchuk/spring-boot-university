package com.foxminded.university;

import java.time.LocalDate;
import java.util.List;

public class Teacher extends Person {

	protected Teacher(String firstName, String lastName, String phone, String address, String gender, String postalCode,
			String education, LocalDate birthDate) {
		super(firstName, lastName, phone, address, gender, postalCode, education, birthDate);
	}

	private Cathedra cathedra;
	private List<Subject> subjects;
	private List<Vacation> vacations;
	private String degree;

	public Cathedra getCathedra() {
		return cathedra;
	}

	public void setCathedra(Cathedra cathedra) {
		this.cathedra = cathedra;
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
