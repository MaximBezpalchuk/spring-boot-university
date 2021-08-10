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

	protected Person(Builder<?> builder) {
		this.id = builder.id;
		this.firstName = builder.firstName;
		this.lastName = builder.lastName;
		this.phone = builder.phone;
		this.address = builder.address;
		this.email = builder.email;
		this.gender = builder.gender;
		this.postalCode = builder.postalCode;
		this.education = builder.education;
		this.birthDate = builder.birthDate;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setGender(Gender gender) {
		this.gender = gender;
	}

	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}

	public void setEducation(String education) {
		this.education = education;
	}

	public void setBirthDate(LocalDate birthDate) {
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

	// Builder

	public abstract static class Builder<T extends Builder<T>> {

		public abstract T getThis();

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
		
		/*
		public Builder(String firstName, String lastName, String address, Gender gender, LocalDate birthDate) {
			this.firstName = firstName;
            this.lastName = lastName;
            this.address = address;
            this.gender = gender;
            this.birthDate = birthDate;
		}*/

		public T setFirstName(String firstName) {
			this.firstName = firstName;
			return this.getThis();
		}

		public T setLastName(String lastName) {
			this.lastName = lastName;
			return this.getThis();
		}

		public T setPhone(String phone) {
			this.phone = phone;
			return this.getThis();
		}

		public T setAddress(String address) {
			this.address = address;
			return this.getThis();
		}

		public T setEmail(String email) {
			this.email = email;
			return this.getThis();
		}

		public T setGender(Gender gender) {
			this.gender = gender;
			return this.getThis();
		}

		public T setPostalCode(String postalCode) {
			this.postalCode = postalCode;
			return this.getThis();
		}

		public T setEducation(String education) {
			this.education = education;
			return this.getThis();
		}

		public T setBirthDate(LocalDate birthDate) {
			this.birthDate = birthDate;
			return this.getThis();
		}

		public T setId(int id) {
			this.id = id;
			return this.getThis();
		}

		public Person build() {
			return new Person(this);
		}
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
