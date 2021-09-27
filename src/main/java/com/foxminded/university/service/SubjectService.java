package com.foxminded.university.service;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.foxminded.university.dao.SubjectDao;
import com.foxminded.university.dao.jdbc.JdbcSubjectDao;
import com.foxminded.university.exception.EntityNotFoundException;
import com.foxminded.university.exception.EntityNotUniqueException;
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

	public Subject findById(int id) throws EntityNotFoundException {
		logger.debug("Find subject by id {}", id);
		return subjectDao.findById(id).orElseThrow(() -> new EntityNotFoundException("Can`t find any subject"));
	}

	public void save(Subject subject) throws Exception {
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

	private boolean isUnique(Subject subject) throws EntityNotUniqueException {
		logger.debug("Check subject is unique");
		Optional<Subject> existingSubject = subjectDao.findByName(subject.getName());
		if (existingSubject.isEmpty() || (existingSubject.get().getId() == subject.getId())) {
			return true;
		} else {
			throw new EntityNotUniqueException("Subject with same name is already exists!");
		}
	}
}
