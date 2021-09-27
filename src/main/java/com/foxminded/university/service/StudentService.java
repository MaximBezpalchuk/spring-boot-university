package com.foxminded.university.service;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.foxminded.university.dao.StudentDao;
import com.foxminded.university.dao.jdbc.JdbcStudentDao;
import com.foxminded.university.exception.EntityNotFoundException;
import com.foxminded.university.exception.EntityNotUniqueException;
import com.foxminded.university.exception.StudentGroupIsFullException;
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

	public Student findById(int id) throws EntityNotFoundException {
		logger.debug("Find student by id {}", id);
		return studentDao.findById(id).orElseThrow(() -> new EntityNotFoundException("Can`t find any student"));
	}

	public void save(Student student) throws Exception {
		logger.debug("Save student");
		if (isUnique(student) && !isGroupFilled(student)) {
			studentDao.save(student);
		}
	}

	public void deleteById(int id) {
		logger.debug("Delete student by id: {}", id);
		studentDao.deleteById(id);
	}

	private boolean isUnique(Student student) throws EntityNotUniqueException {
		logger.debug("Check student is unique");
		Optional<Student> existingStudent = studentDao.findByFullNameAndBirthDate(student.getFirstName(),
				student.getLastName(), student.getBirthDate());

		if (existingStudent.isEmpty() || (existingStudent.get().getId() == student.getId())) {
			return true;
		} else {
			throw new EntityNotUniqueException(
					"Student with same first name, last name and  birth date is already exists!");
		}
	}

	private boolean isGroupFilled(Student student) throws StudentGroupIsFullException {
		logger.debug("Check that group is filled");
		if (student.getGroup() != null) {
			if (studentDao.findByGroupId(student.getGroup().getId()).size() >= maxGroupSize) {
				throw new StudentGroupIsFullException("This group is already full!");
			} else {
				return false;
			}
		}

		return false;
	}
}
