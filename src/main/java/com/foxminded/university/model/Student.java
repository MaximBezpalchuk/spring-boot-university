package com.foxminded.university.model;

import java.time.LocalDate;

public class Student extends Person {

	private Group group;

	public Student(String firstName, String lastName, String phone, String address, String gender, String postalCode,
			String education, LocalDate birthDate) {
		super(firstName, lastName, phone, address, gender, postalCode, education, birthDate);
	}

	public Group getGroup() {
		return group;
	}

	public void setGroup(Group group) {
		this.group = group;
	}

}
