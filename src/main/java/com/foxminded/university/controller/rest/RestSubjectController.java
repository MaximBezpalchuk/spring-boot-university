package com.foxminded.university.controller.rest;

import com.foxminded.university.controller.SubjectController;
import com.foxminded.university.model.Subject;
import com.foxminded.university.service.SubjectService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/subjects")
public class RestSubjectController {

    private static final Logger logger = LoggerFactory.getLogger(SubjectController.class);

    private final SubjectService subjectService;

    public RestSubjectController(SubjectService subjectService) {
        this.subjectService = subjectService;
    }

    @GetMapping
    public Page<Subject> getAllSubjects(Pageable pageable) {
        logger.debug("Show all subjects");

        return subjectService.findAll(pageable);
    }

    @GetMapping("/{id}")
    public Subject showSubject(@PathVariable int id) {
        logger.debug("Show subject page with id {}", id);

        return subjectService.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void create(@RequestBody Subject subject) {
        logger.debug("Create new subject. Id {}", subject.getId());
        subjectService.save(subject);
    }

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void update(@RequestBody Subject subject, @PathVariable int id) {
        logger.debug("Update subject with id {}", id);
        subjectService.save(subject);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void delete(@RequestBody Subject subject) {
        logger.debug("Delete subject with id {}", subject.getId());
        subjectService.delete(subject);
    }
}