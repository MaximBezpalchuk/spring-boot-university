package com.foxminded.university.model;

import java.time.LocalDate;
import java.util.Objects;

public class Student extends Person {

	private Group group;

	public Student(String firstName, String lastName, String phone, String address, String email, Gender gender,
			String postalCode, String education, LocalDate birthDate) {
		super(firstName, lastName, phone, address, email, gender, postalCode, education, birthDate);
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
