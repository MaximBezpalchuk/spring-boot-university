package com.foxminded.university.service;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.foxminded.university.dao.StudentDao;
import com.foxminded.university.dao.jdbc.JdbcStudentDao;
import com.foxminded.university.exception.EntityNotFoundException;
import com.foxminded.university.exception.EntityNotUniqueException;
import com.foxminded.university.exception.GroupOverflowException;
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

	public Page<Student> findAll(final Pageable pageable) {
		logger.debug("Find all holidays paginated");
		return studentDao.findPaginatedStudents(pageable);
	}

	public Student findById(int id) {
		logger.debug("Find student by id {}", id);
		return studentDao.findById(id)
				.orElseThrow(() -> new EntityNotFoundException("Can`t find any student with id: " + id));
	}

	public void save(Student student) {
		logger.debug("Save student");
		uniqueCheck(student);
		groupOverflowCheck(student);
		studentDao.save(student);
	}

	public void deleteById(int id) {
		logger.debug("Delete student by id: {}", id);
		studentDao.deleteById(id);
	}

	private void uniqueCheck(Student student) {
		logger.debug("Check student is unique");
		Optional<Student> existingStudent = studentDao.findByFullNameAndBirthDate(student.getFirstName(),
				student.getLastName(), student.getBirthDate());

		if (existingStudent.isPresent() && (existingStudent.get().getId() != student.getId())) {
			throw new EntityNotUniqueException("Student with full name " + student.getFirstName() + " "
					+ student.getLastName() + " and  birth date " + student.getBirthDate() + " is already exists!");
		}
	}

	private void groupOverflowCheck(Student student) {
		logger.debug("Check that group is filled");
		if (student.getGroup() != null) {
			int groupSize = studentDao.findByGroupId(student.getGroup().getId()).size();
			if (groupSize >= maxGroupSize) {
				throw new GroupOverflowException("This group is already full! Group size is: " + groupSize
						+ ". Max group size is: " + maxGroupSize);
			}
		}
	}
}
