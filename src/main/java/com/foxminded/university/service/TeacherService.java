package com.foxminded.university.service;

import java.util.List;
import java.util.NoSuchElementException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.foxminded.university.dao.TeacherDao;
import com.foxminded.university.dao.jdbc.JdbcTeacherDao;
import com.foxminded.university.exception.DaoException;
import com.foxminded.university.exception.EntityNotFoundException;
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
		try {
			return teacherDao.findById(id).orElseThrow();
		} catch (NoSuchElementException e) {
			throw new EntityNotFoundException("Can`t find any teacher", e);
		}
	}

	public void save(Teacher teacher) {
		logger.debug("Save teacher");
		if (isUnique(teacher)) {
			teacherDao.save(teacher);
		}
	}

	public void deleteById(int id) {
		logger.debug("Delete teacher by id: {}", id);
		teacherDao.deleteById(id);
	}

	private boolean isUnique(Teacher teacher) {
		logger.debug("Check teacher is unique");
		try {
			Teacher existingTeacher = teacherDao.findByFullNameAndBirthDate(teacher.getFirstName(),
					teacher.getLastName(), teacher.getBirthDate());

			return existingTeacher == null || (existingTeacher.getId() == teacher.getId());
		} catch (DaoException e) {
			logger.error("Teacher with same first name: {}, last name: {} and birth date: {} is already exists",
					teacher.getFirstName(), teacher.getLastName(), teacher.getBirthDate());
			return false;
		}
	}
}
