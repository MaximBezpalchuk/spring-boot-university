package com.foxminded.university.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Teacher extends Person {

	private int id;
	private Cathedra cathedra;
	private List<Subject> subjects = new ArrayList<>();
	private Degree degree;

	public Teacher(String firstName, String lastName, String phone, String address, String email, Gender gender,
			String postalCode, String education, LocalDate birthDate, Cathedra cathedra) {
		super(firstName, lastName, phone, address, email, gender, postalCode, education, birthDate);
		this.cathedra = cathedra;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
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

	public Degree getDegree() {
		return degree;
	}

	public void setDegree(Degree degree) {
		this.degree = degree;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + Objects.hash(cathedra, degree, id, subjects);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		Teacher other = (Teacher) obj;
		return Objects.equals(cathedra, other.cathedra) && degree == other.degree && id == other.id
				&& Objects.equals(subjects, other.subjects);
	}

}
