package com.foxminded.university;

import java.time.LocalDate;

import com.foxminded.university.model.Cathedra;
import com.foxminded.university.model.Degree;
import com.foxminded.university.model.Gender;
import com.foxminded.university.model.Group;
import com.foxminded.university.model.Student;
import com.foxminded.university.model.Teacher;

public class DataUpdater {

	public Student createStudent(String firstName, String lastName, String phone, String address, String email,
			Gender gender, String postalCode, String education, LocalDate birthDate, Group group) {
		Student student = new Student.Builder().setFirstName(firstName).setLastName(lastName).setPhone(phone)
				.setAddress(address).setEmail(email).setGender(gender).setPostalCode(postalCode).setEducation(education)
				.setBirthDate(birthDate).setGroup(group).build();
		return student;
	}

	public Teacher createTeacher(String firstName, String lastName, String phone, String address, String email,
			Gender gender, String postalCode, String education, LocalDate birthDate, Degree degree, Cathedra cathedra) {
		Teacher teacher = new Teacher.Builder().setFirstName(firstName).setLastName(lastName).setPhone(phone)
				.setAddress(address).setEmail(email).setGender(gender).setPostalCode(postalCode).setEducation(education)
				.setBirthDate(birthDate).setCathedra(cathedra).setDegree(degree).build();
		return teacher;
	}
}
