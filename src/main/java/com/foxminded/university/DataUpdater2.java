package com.foxminded.university;

import java.time.LocalDate;
import java.util.List;

import com.foxminded.university.model.Cathedra;
import com.foxminded.university.model.Degree;
import com.foxminded.university.model.Gender;
import com.foxminded.university.model.Group;
import com.foxminded.university.model.Student;
import com.foxminded.university.model.Subject;
import com.foxminded.university.model.Teacher;

public class DataUpdater2 {

	public Student createStudent(String firstName, String lastName, String phone, String address, String email,
			Gender gender, String postalCode, String education, LocalDate birthDate, Group group) {
		Student student = new Student(firstName, lastName, phone, address, email, gender, postalCode, education,
				birthDate);
		student.setGroup(group);
		return student;
	}

	public void createTeacher(String firstName, String lastName, String phone, String address, String email,
			Gender gender, String postalCode, String education, LocalDate birthDate, Degree degree, Cathedra cathedra,
			List<Subject> subjects) {
		Teacher teacher = new Teacher(firstName, lastName, phone, address, email, gender, postalCode, education,
				birthDate, cathedra);
		teacher.setSubjects(subjects);
		teacher.setDegree(degree);
		for (Subject subject : subjects) {
			subject.getTeachers().add(teacher);
		}
		cathedra.getTeachers().add(teacher);
	}
}
