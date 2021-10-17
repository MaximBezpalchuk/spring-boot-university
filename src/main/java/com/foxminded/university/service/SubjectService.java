package com.foxminded.university.service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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

	public Subject findById(int id) {
		logger.debug("Find subject by id {}", id);
		return subjectDao.findById(id)
				.orElseThrow(() -> new EntityNotFoundException("Can`t find any subject with id: " + id));
	}

	public void save(Subject subject) {
		logger.debug("Save subject");
		uniqueCheck(subject);
		subjectDao.save(subject);
	}

	public void deleteById(int id) {
		logger.debug("Delete subject by id: {}", id);
		subjectDao.deleteById(id);
	}

	public List<Subject> findByTeacherId(int id) {
		logger.debug("Find subject by teacher id: {}", id);
		return subjectDao.findByTeacherId(id);
	}

	private void uniqueCheck(Subject subject) {
		logger.debug("Check subject is unique");
		Optional<Subject> existingSubject = subjectDao.findByName(subject.getName());
		if (existingSubject.isPresent() && (existingSubject.get().getId() != subject.getId())) {
			throw new EntityNotUniqueException("Subject with name " + subject.getName() + " is already exists!");
		}
	}
	
	public Page<Subject> findPaginatedSubjects(final Pageable pageable){
		List<Subject> subjects = subjectDao.findAll();
		int pageSize = pageable.getPageSize();
        int currentPage = pageable.getPageNumber();
        int startItem = currentPage * pageSize;
        final List<Subject> list;
        if (subjects.size() < startItem) {
            list = Collections.emptyList();
        } else {
            int toIndex = Math.min(startItem + pageSize, subjects.size());
            list = subjects.subList(startItem, toIndex);
        }
        
        return new PageImpl<>(list, PageRequest.of(currentPage, pageSize), subjects.size());
	}
}
