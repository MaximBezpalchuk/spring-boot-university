package com.foxminded.university.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Teacher extends Person {

	private Cathedra cathedra;
	private List<Subject> subjects = new ArrayList<>();
	private List<Vacation> vacations = new ArrayList<>();
	private Degree degree;

	public Teacher(String firstName, String lastName, String phone, String address, String email, Gender gender,
			String postalCode, String education, LocalDate birthDate, Cathedra cathedra) {
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
		result = prime * result + ((cathedra == null) ? 0 : cathedra.hashCode());
		result = prime * result + ((degree == null) ? 0 : degree.hashCode());
		result = prime * result + ((subjects == null) ? 0 : subjects.hashCode());
		result = prime * result + ((vacations == null) ? 0 : vacations.hashCode());
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
		if (cathedra == null) {
			if (other.cathedra != null)
				return false;
		} else if (!cathedra.equals(other.cathedra))
			return false;
		if (degree != other.degree)
			return false;
		if (subjects == null) {
			if (other.subjects != null)
				return false;
		} else if (!subjects.equals(other.subjects))
			return false;
		if (vacations == null) {
			if (other.vacations != null)
				return false;
		} else if (!vacations.equals(other.vacations))
			return false;
		return true;
	}

}
