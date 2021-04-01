package com.foxminded.university.model;

import java.time.LocalDate;

public class Person {

	private String firstName;
	private String lastName;
	private String phone;
	private String address;
	private String email;
	private String gender;
	private String postalCode;
	private String education;
	private LocalDate birthDate;

	protected Person(String firstName, String lastName, String phone, String address, String email, String gender, String postalCode,
			String education, LocalDate birthDate) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.phone = phone;
		this.address = address;
		this.email = email;
		this.gender = gender;
		this.postalCode = postalCode;
		this.education = education;
		this.birthDate = birthDate;
	}

	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public String getPhone() {
		return phone;
	}

	public String getAddress() {
		return address;
	}

	public String getEmail() {
		return email;
	}

	public String getGender() {
		return gender;
	}

	public String getPostalCode() {
		return postalCode;
	}

	public String getEducation() {
		return education;
	}

	public LocalDate getBirthDate() {
		return birthDate;
	}

}
