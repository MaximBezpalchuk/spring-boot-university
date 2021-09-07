package com.foxminded.university.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.foxminded.university.dao.StudentDao;
import com.foxminded.university.dao.jdbc.JdbcStudentDao;
import com.foxminded.university.model.Student;

@Service
public class StudentService {

	private StudentDao studentDao;

	public StudentService(JdbcStudentDao studentDao) {
		this.studentDao = studentDao;
	}

	public List<Student> findAll() {
		return studentDao.findAll();
	}

	public Student findById(int id) {
		return studentDao.findById(id);
	}

	public String save(Student student) {
		Student existingStudent = studentDao.findByFullNameAndBirthDate(student.getFirstName(), student.getLastName(),
				student.getBirthDate());
		if (existingStudent == null) {
			studentDao.save(student);
			return "Student added!";
		} else if (existingStudent.getId() == student.getId()) {
			studentDao.save(student);
			return "Student updated!";
		}

		return "Student with such full name and birthday already exists!";
	}

	public void deleteById(int id) {
		studentDao.deleteById(id);
	}
}
