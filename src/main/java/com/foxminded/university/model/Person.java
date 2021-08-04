package com.foxminded.university.model;

import java.time.LocalDate;
import java.util.Objects;

public class Person {

	private int id;
	private String firstName;
	private String lastName;
	private String phone;
	private String address;
	private String email;
	private Gender gender;
	private String postalCode;
	private String education;
	private LocalDate birthDate;

	protected Person(String firstName, String lastName, String phone, String address, String email, Gender gender,
			String postalCode, String education, LocalDate birthDate) {
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

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
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
		return gender.name();
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

	@Override
	public int hashCode() {
		return Objects.hash(address, birthDate, education, email, firstName, gender, id, lastName, phone, postalCode);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Person other = (Person) obj;
		return Objects.equals(address, other.address) && Objects.equals(birthDate, other.birthDate)
				&& Objects.equals(education, other.education) && Objects.equals(email, other.email)
				&& Objects.equals(firstName, other.firstName) && gender == other.gender && id == other.id
				&& Objects.equals(lastName, other.lastName) && Objects.equals(phone, other.phone)
				&& Objects.equals(postalCode, other.postalCode);
	}

}
