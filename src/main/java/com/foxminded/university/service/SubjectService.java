package com.foxminded.university.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.foxminded.university.dao.SubjectDao;
import com.foxminded.university.dao.jdbc.JdbcSubjectDao;
import com.foxminded.university.model.Subject;

@Service
public class SubjectService {

	private SubjectDao subjectDao;

	public SubjectService(JdbcSubjectDao subjectDao) {
		this.subjectDao = subjectDao;
	}

	public List<Subject> findAll() {
		return subjectDao.findAll();
	}

	public Subject findById(int id) {
		return subjectDao.findById(id);
	}

	public void save(Subject subject) {
		if (isUnique(subject)) {
			subjectDao.save(subject);
		}
	}

	public void deleteById(int id) {
		subjectDao.deleteById(id);
	}

	public List<Subject> findByTeacherId(int id) {
		return subjectDao.findByTeacherId(id);
	}

	private boolean isUnique(Subject subject) {
		Subject existingSubject = subjectDao.findByName(subject.getName());

		return existingSubject == null || (existingSubject.getId() == subject.getId());
	}
}
