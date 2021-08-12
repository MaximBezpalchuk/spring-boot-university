package com.foxminded.university.model;

import java.time.LocalDate;
import java.util.Objects;

public class Student extends Person {

	private Group group;

	public Student(Builder builder) {
		super(builder);
		this.group = builder.group;
	}

	public Group getGroup() {
		return group;
	}

	public void setGroup(Group group) {
		this.group = group;
	}

	public static Builder build(String firstName, String lastName, String address, Gender gender, LocalDate birthDate) {
		return new Builder(firstName, lastName, address, gender, birthDate);
	}

	public static class Builder extends Person.Builder<Builder> {

		private Group group;

		public Builder(String firstName, String lastName, String address, Gender gender, LocalDate birthDate) {
			super(firstName, lastName, address, gender, birthDate);
		}

		@Override
		public Builder getThis() {
			return this;
		}

		public Builder group(Group group) {
			this.group = group;
			return this;
		}

		public Student build() {
			return new Student(this);
		}

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
