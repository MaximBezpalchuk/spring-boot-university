package com.foxminded.university.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.foxminded.university.dao.StudentDao;
import com.foxminded.university.dao.jdbc.JdbcStudentDao;
import com.foxminded.university.exception.DaoException;
import com.foxminded.university.model.Student;

@Service
public class StudentService {

	private static final Logger logger = LoggerFactory.getLogger(StudentService.class);

	private StudentDao studentDao;
	@Value("${maxGroupSize}")
	private int maxGroupSize;

	public StudentService(JdbcStudentDao studentDao) {
		this.studentDao = studentDao;
	}

	public List<Student> findAll() {
		logger.debug("Find all students");
		return studentDao.findAll();
	}

	public Student findById(int id) {
		logger.debug("Find student by id {}", id);
		try {
			return studentDao.findById(id);
		} catch (DaoException e) {
			logger.error("Cannot find student with id: {}", id, e);
			return null;
		}
	}

	public void save(Student student) {
		logger.debug("Save student");
		if (isUnique(student) && !isGroupFilled(student)) {
			studentDao.save(student);
		}
	}

	public void deleteById(int id) {
		logger.debug("Delete student by id: {}", id);
		studentDao.deleteById(id);
	}

	private boolean isUnique(Student student) {
		logger.debug("Check student is unique");
		try {
			Student existingStudent = studentDao.findByFullNameAndBirthDate(student.getFirstName(),
					student.getLastName(), student.getBirthDate());

			return existingStudent == null || (existingStudent.getId() == student.getId());
		} catch (DaoException e) {
			logger.error("Student with same first name: {}, last name: {} and  birth date: {} is already exists",
					student.getFirstName(), student.getLastName(), student.getBirthDate());
			return false;
		}
	}

	private boolean isGroupFilled(Student student) {
		logger.debug("Check that group is filled");
		if (student.getGroup() != null) {
			return studentDao.findByGroupId(student.getGroup().getId()).size() >= maxGroupSize;
		}

		return false;
	}
}
