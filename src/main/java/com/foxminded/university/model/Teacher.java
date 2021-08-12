package com.foxminded.university.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Teacher extends Person {

	private Cathedra cathedra;
	private List<Subject> subjects = new ArrayList<>();
	private Degree degree;

	public Teacher(Builder builder) {
		super(builder);
		this.cathedra = builder.cathedra;
		this.subjects = builder.subjects;
		this.degree = builder.degree;
	}

	public static Builder build(String firstName, String lastName, String address, Gender gender, LocalDate birthDate,
			Cathedra cathedra, Degree degree) {
		return new Builder(firstName, lastName, address, gender, birthDate, cathedra, degree);
	}

	public static class Builder extends Person.Builder<Builder> {

		public Builder(String firstName, String lastName, String address, Gender gender, LocalDate birthDate,
				Cathedra cathedra, Degree degree) {
			super(firstName, lastName, address, gender, birthDate);
			this.cathedra = cathedra;
			this.degree = degree;
		}

		private final Cathedra cathedra; // required field
		private List<Subject> subjects = new ArrayList<>();
		private final Degree degree; // required field

		@Override
		public Builder getThis() {
			return this;
		}

		public Builder setSubjects(List<Subject> subjects) {
			this.subjects = subjects;
			return this;
		}

		public Teacher build() {
			return new Teacher(this);
		}

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
		result = prime * result + Objects.hash(cathedra, degree, subjects);
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
		return Objects.equals(cathedra, other.cathedra) && degree == other.degree
				&& Objects.equals(subjects, other.subjects);
	}

}
