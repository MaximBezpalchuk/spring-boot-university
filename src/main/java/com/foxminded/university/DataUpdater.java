package com.foxminded.university;

import java.time.LocalDate;
import java.util.List;

import com.foxminded.university.model.Cathedra;
import com.foxminded.university.model.Group;
import com.foxminded.university.model.Student;
import com.foxminded.university.model.Subject;
import com.foxminded.university.model.Teacher;

public class DataUpdater {

	public void createStudent(String firstName, String lastName, String phone, String address, String email,
			String gender, String postalCode, String education, LocalDate birthDate, Group group) {
		Student student = new Student(firstName, lastName, phone, address, email, gender, postalCode, education,
				birthDate);
		student.setGroup(group);
		group.getStudents().add(student);
	}

	public void createTeacher(String firstName, String lastName, String phone, String address, String email,
			String gender, String postalCode, String education, LocalDate birthDate, Cathedra cathedra,
			List<Subject> subjects) {
		Teacher teacher = new Teacher(firstName, lastName, phone, address, email, gender, postalCode, education,
				birthDate, cathedra);
		teacher.setSubjects(subjects);
		for(Subject subject : subjects) {
			subject.getTeachers().add(teacher);
		}
		cathedra.getTeachers().add(teacher);		
	}
}
