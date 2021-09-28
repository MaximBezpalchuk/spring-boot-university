package com.foxminded.university.service;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.foxminded.university.dao.TeacherDao;
import com.foxminded.university.dao.jdbc.JdbcTeacherDao;
import com.foxminded.university.exception.EntityNotFoundException;
import com.foxminded.university.exception.EntityNotUniqueException;
import com.foxminded.university.exception.ServiceLayerException;
import com.foxminded.university.model.Teacher;

@Service
public class TeacherService {

	private static final Logger logger = LoggerFactory.getLogger(TeacherService.class);

	private TeacherDao teacherDao;

	public TeacherService(JdbcTeacherDao teacherDao) {
		this.teacherDao = teacherDao;
	}

	public List<Teacher> findAll() {
		logger.debug("Find all teachers");
		return teacherDao.findAll();
	}

	public Teacher findById(int id) throws EntityNotFoundException {
		logger.debug("Find teacher by id {}", id);
		return teacherDao.findById(id).orElseThrow(
				() -> new EntityNotFoundException("Can`t find any teacher with specified id!", "Id is: " + id));
	}

	public void save(Teacher teacher) throws ServiceLayerException {
		logger.debug("Save teacher");
		uniqueCheck(teacher);
		teacherDao.save(teacher);
	}

	public void deleteById(int id) {
		logger.debug("Delete teacher by id: {}", id);
		teacherDao.deleteById(id);
	}

	private void uniqueCheck(Teacher teacher) throws EntityNotUniqueException {
		logger.debug("Check teacher is unique");

		Optional<Teacher> existingTeacher = teacherDao.findByFullNameAndBirthDate(teacher.getFirstName(),
				teacher.getLastName(), teacher.getBirthDate());
		if (existingTeacher.isEmpty() || (existingTeacher.get().getId() == teacher.getId())) {
			return;
		} else {
			throw new EntityNotUniqueException(
					"Teacher with same first name, last name and birth date is already exists!",
					"Teacher name is: " + teacher.getFirstName() + " " + teacher.getLastName(),
					"Teacher birth date is: " + teacher.getBirthDate());
		}
	}
}
