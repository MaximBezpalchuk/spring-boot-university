package com.foxminded.university.service;

import com.foxminded.university.dao.SubjectRepository;
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

    private final SubjectRepository subjectRepository;

    public SubjectService(SubjectRepository subjectRepository) {
        this.subjectRepository = subjectRepository;
    }

    public List<Subject> findAll() {
        logger.debug("Find all subjects");
        return subjectRepository.findAll();
    }

    public Page<Subject> findAll(final Pageable pageable) {
        logger.debug("Find all holidays paginated");
        return subjectRepository.findAll(pageable);
    }

    public Subject findById(int id) {
        logger.debug("Find subject by id {}", id);
        return subjectRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Can`t find any subject with id: " + id));
    }

    public Subject findByName(String name) {
        logger.debug("Find subject by name {}", name);
        return subjectRepository.findByName(name)
            .orElseThrow(() -> new EntityNotFoundException("Can`t find any subject with name: " + name));
    }

    public void save(Subject subject) {
        logger.debug("Save subject");
        uniqueCheck(subject);
        subjectRepository.save(subject);
    }

    public void delete(Subject subject) {
        logger.debug("Delete subject with id: {}", subject.getId());
        subjectRepository.delete(subject);
    }

    private void uniqueCheck(Subject subject) {
        logger.debug("Check subject is unique");
        Optional<Subject> existingSubject = subjectRepository.findByName(subject.getName());
        if (existingSubject.isPresent() && (existingSubject.get().getId() != subject.getId())) {
            throw new EntityNotUniqueException("Subject with name " + subject.getName() + " is already exists!");
        }
    }
}
