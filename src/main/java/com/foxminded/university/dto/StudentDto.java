package com.foxminded.university.dto;

import com.foxminded.university.model.Gender;
import com.foxminded.university.validation.MinAge;
import com.foxminded.university.validation.PhoneNumber;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.Objects;

@MinAge(14)
public class StudentDto {

    private Integer id;
    @NotBlank(message = "{FirstName.person.notBlank}")
    private String firstName;
    @NotBlank(message = "{LastName.person.notBlank}")
    private String lastName;
    @PhoneNumber
    @NotBlank(message = "{Phone.person.notBlank}")
    private String phone;
    @NotBlank(message = "{Address.person.notBlank}")
    private String address;
    @NotBlank(message = "{Email.person.notBlank}")
    @Email
    private String email;
    private Gender gender;
    @NotBlank(message = "{PostalCode.person.notBlank}")
    private String postalCode;
    @NotBlank(message = "{Education.person.notBlank}")
    private String education;
    @NotNull(message = "{BirthDate.person.notNull}")
    private LocalDate birthDate;
    private GroupDto group;

    public StudentDto(Integer id, String firstName, String lastName, String phone, String address, String email, Gender gender, String postalCode, String education, LocalDate birthDate, GroupDto group) {
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
        this.group = group;
    }

    public StudentDto() {
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

    public GroupDto getGroup() {
        return group;
    }

    public void setGroup(GroupDto group) {
        this.group = group;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StudentDto that = (StudentDto) o;
        return id.equals(that.id) &&
            firstName.equals(that.firstName) &&
            lastName.equals(that.lastName) &&
            phone.equals(that.phone) &&
            address.equals(that.address) &&
            email.equals(that.email) &&
            gender == that.gender &&
            postalCode.equals(that.postalCode) &&
            education.equals(that.education) &&
            birthDate.equals(that.birthDate) &&
            group.equals(that.group);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, firstName, lastName, phone, address, email, gender, postalCode, education, birthDate, group);
    }
}
