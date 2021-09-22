package com.foxminded.university.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.foxminded.university.dao.SubjectDao;
import com.foxminded.university.dao.jdbc.JdbcSubjectDao;
import com.foxminded.university.exception.DaoException;
import com.foxminded.university.model.Subject;

@Service
public class SubjectService {

	private static final Logger logger = LoggerFactory.getLogger(SubjectService.class);

	private SubjectDao subjectDao;

	public SubjectService(JdbcSubjectDao subjectDao) {
		this.subjectDao = subjectDao;
	}

	public List<Subject> findAll() {
		logger.debug("Find all subjects");
		return subjectDao.findAll();
	}

	public Subject findById(int id) {
		logger.debug("Find subject by id {}", id);
		try {
			return subjectDao.findById(id);
		} catch (DaoException e) {
			logger.error("Cannot find subject with id: {}", id, e);
			return null;
		}
	}

	public void save(Subject subject) {
		logger.debug("Save subject");
		if (isUnique(subject)) {
			subjectDao.save(subject);
		}
	}

	public void deleteById(int id) {
		logger.debug("Delete subject by id: {}", id);
		subjectDao.deleteById(id);
	}

	public List<Subject> findByTeacherId(int id) {
		logger.debug("Find subject by teacher id: {}", id);
		return subjectDao.findByTeacherId(id);
	}

	private boolean isUnique(Subject subject) {
		logger.debug("Check subject is unique");
		try {
			Subject existingSubject = subjectDao.findByName(subject.getName());

			return existingSubject == null || (existingSubject.getId() == subject.getId());
		} catch (DaoException e) {
			logger.error("Subject with same name: {} is already exists", subject.getName());
			return false;
		}
	}
}
