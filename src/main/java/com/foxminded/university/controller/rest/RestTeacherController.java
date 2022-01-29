package com.foxminded.university.controller.rest;

import com.foxminded.university.controller.TeacherController;
import com.foxminded.university.model.Teacher;
import com.foxminded.university.service.TeacherService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/teachers")
public class RestTeacherController {

    private static final Logger logger = LoggerFactory.getLogger(TeacherController.class);
    private final TeacherService teacherService;

    public RestTeacherController(TeacherService teacherService) {
        this.teacherService = teacherService;
    }

    @GetMapping
    public Page<Teacher> all(Pageable pageable) {
        logger.debug("Show all teachers");

        return teacherService.findAll(pageable);
    }

    @GetMapping("/{id}")
    public Teacher showTeacher(@PathVariable int id) {
        logger.debug("Show teacher with id {}", id);

        return teacherService.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void create(@RequestBody Teacher teacher) {
        logger.debug("Create new teacher. Id {}", teacher.getId());
        teacherService.save(teacher);
    }

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void update(@RequestBody Teacher teacher, @PathVariable int id) {
        logger.debug("Update teacher with id {}", id);
        teacherService.save(teacher);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void delete(@RequestBody Teacher teacher) {
        logger.debug("Delete teacher with id {}", teacher.getId());
        teacherService.delete(teacher);
    }
}