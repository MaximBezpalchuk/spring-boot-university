package com.foxminded.university.service;

import com.foxminded.university.dao.SubjectDao;
import com.foxminded.university.exception.EntityNotFoundException;
import com.foxminded.university.exception.EntityNotUniqueException;
import com.foxminded.university.model.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SubjectService {

    private static final Logger logger = LoggerFactory.getLogger(SubjectService.class);

    private final SubjectDao subjectDao;

    public SubjectService(SubjectDao subjectDao) {
        this.subjectDao = subjectDao;
    }

    public List<Subject> findAll() {
        logger.debug("Find all subjects");
        return subjectDao.findAll();
    }

    public Page<Subject> findAll(final Pageable pageable) {
        logger.debug("Find all holidays paginated");
        return subjectDao.findAll(pageable);
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

    public void delete(Subject subject) {
        logger.debug("Delete subject with id: {}", subject.getId());
        subjectDao.delete(subject);
    }

    private void uniqueCheck(Subject subject) {
        logger.debug("Check subject is unique");
        Optional<Subject> existingSubject = subjectDao.findByName(subject.getName());
        if (existingSubject.isPresent() && (existingSubject.get().getId() != subject.getId())) {
            throw new EntityNotUniqueException("Subject with name " + subject.getName() + " is already exists!");
        }
    }
}
