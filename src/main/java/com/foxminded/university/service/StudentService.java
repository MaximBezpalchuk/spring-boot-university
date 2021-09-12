package com.foxminded.university.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.foxminded.university.dao.StudentDao;
import com.foxminded.university.dao.jdbc.JdbcStudentDao;
import com.foxminded.university.model.Student;

@Service
public class StudentService {

	private StudentDao studentDao;
	@Value("${maxGroupSize}")
	private int maxGroupSize;

	public StudentService(JdbcStudentDao studentDao) {
		this.studentDao = studentDao;
	}

	public List<Student> findAll() {
		return studentDao.findAll();
	}

	public Student findById(int id) {
		return studentDao.findById(id);
	}

	public void save(Student student) {
		if (isUnique(student) && !isGroupFilled(student)) {
			studentDao.save(student);
		}
	}

	public void deleteById(int id) {
		studentDao.deleteById(id);
	}

	private boolean isUnique(Student student) {
		Student existingStudent = studentDao.findByFullNameAndBirthDate(student.getFirstName(), student.getLastName(),
				student.getBirthDate());

		return existingStudent == null || (existingStudent.getId() == student.getId());
	}

	private boolean isGroupFilled(Student student) {
		List<Student> students = studentDao.findByGroupName(student.getGroup().getName());
		if (students.size() >= maxGroupSize) {
			return true;
		}

		return false;
	}
}
