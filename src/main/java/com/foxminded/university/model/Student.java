package com.foxminded.university.model;

import java.time.LocalDate;
import java.util.Objects;

public class Student extends Person {

	private Group group;

	public Student(Builder builder) {
		super(builder);
		this.group = builder.group;
	}

	public static class Builder extends Person.Builder<Builder> {
		/*
		public Builder(String firstName, String lastName, String address, Gender gender, LocalDate birthDate) {
			super(firstName, lastName, address, gender, birthDate);
			// TODO Auto-generated constructor stub
		}*/

		private Group group;

		@Override
		public Builder getThis() {
			return this;
		}

		public Builder setGroup(Group group) {
			this.group = group;
			return this;
		}

		public Student build() {
			return new Student(this);
		}

	}

	public Group getGroup() {
		return group;
	}

	public void setGroup(Group group) {
		this.group = group;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + Objects.hash(group);
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
		Student other = (Student) obj;
		return Objects.equals(group, other.group);
	}

}
