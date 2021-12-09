package com.foxminded.university.model;

import org.springframework.format.annotation.DateTimeFormat;

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
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthDate;

    protected Person(int id, String firstName, String lastName, String phone, String address, String email,
                     Gender gender, String postalCode, String education, LocalDate birthDate) {
        this.id = id;
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

    protected Person() {
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

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGender() {
        return gender.name();
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getEducation() {
        return education;
    }

    public void setEducation(String education) {
        this.education = education;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
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

    public abstract static class Builder<T extends Builder<T>> {

        protected int id;
        protected String firstName;
        protected String lastName;
        protected String phone;
        protected String address;
        protected String email;
        protected Gender gender;
        protected String postalCode;
        protected String education;
        protected LocalDate birthDate;

        public abstract T getThis();

        public T phone(String phone) {
            this.phone = phone;
            return this.getThis();
        }

        public T email(String email) {
            this.email = email;
            return this.getThis();
        }

        public T postalCode(String postalCode) {
            this.postalCode = postalCode;
            return this.getThis();
        }

        public T education(String education) {
            this.education = education;
            return this.getThis();
        }

        public T id(int id) {
            this.id = id;
            return this.getThis();
        }

        public T firstName(String firstName) {
            this.firstName = firstName;
            return this.getThis();
        }

        public T lastName(String lastName) {
            this.lastName = lastName;
            return this.getThis();
        }

        public T address(String address) {
            this.address = address;
            return this.getThis();
        }

        public T gender(Gender gender) {
            this.gender = gender;
            return this.getThis();
        }

        public T birthDate(LocalDate birthDate) {
            this.birthDate = birthDate;
            return this.getThis();
        }

        public Person build() {
            return new Person(id, firstName, lastName, phone, address, email, gender, postalCode, education, birthDate);
        }
    }

}
