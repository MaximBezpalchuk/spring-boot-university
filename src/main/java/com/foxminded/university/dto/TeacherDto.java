package com.foxminded.university.dto;

import com.foxminded.university.model.Degree;
import com.foxminded.university.model.Gender;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class TeacherDto {

    private Integer id;
    private String firstName;
    private String lastName;
    private String phone;
    private String address;
    private String email;
    private Gender gender;
    private String postalCode;
    private String education;
    private LocalDate birthDate;
    private String cathedraName;
    private Degree degree;
    private List<String> subjectNames = new ArrayList<>();

    public TeacherDto(Integer id, String firstName, String lastName, String phone, String address, String email, Gender gender, String postalCode, String education, LocalDate birthDate, String cathedraName, Degree degree, List<String> subjectNames) {
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
        this.cathedraName = cathedraName;
        this.degree = degree;
        this.subjectNames = subjectNames;
    }

    public TeacherDto() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
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

    public Gender getGender() {
        return gender;
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

    public String getCathedraName() {
        return cathedraName;
    }

    public void setCathedraName(String cathedraName) {
        this.cathedraName = cathedraName;
    }

    public Degree getDegree() {
        return degree;
    }

    public void setDegree(Degree degree) {
        this.degree = degree;
    }

    public List<String> getSubjectNames() {
        return subjectNames;
    }

    public void setSubjectNames(List<String> subjectNames) {
        this.subjectNames = subjectNames;
    }
}
