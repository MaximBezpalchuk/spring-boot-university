package com.foxminded.university;

import java.time.LocalDate;

import com.foxminded.university.model.Cathedra;
import com.foxminded.university.model.Degree;
import com.foxminded.university.model.Gender;
import com.foxminded.university.model.Student;
import com.foxminded.university.model.Teacher;

public class DataUpdater {

	public Student createStudent(String firstName, String lastName, String phone, String address, String email,
			Gender gender, String postalCode, String education, LocalDate birthDate) {
		Student student = new Student.Builder(firstName, lastName, address, gender, birthDate).setPhone(phone)
				.setEmail(email).setPostalCode(postalCode).setEducation(education).build();
		return student;
	}

	public Teacher createTeacher(String firstName, String lastName, String phone, String address, String email,
			Gender gender, String postalCode, String education, LocalDate birthDate, Degree degree, Cathedra cathedra) {
		Teacher teacher = new Teacher.Builder(firstName, lastName, address, gender, birthDate, cathedra, degree)
				.setPhone(phone).setEmail(email).setPostalCode(postalCode).setEducation(education).build();
		return teacher;
	}
}
