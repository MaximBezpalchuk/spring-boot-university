package com.foxminded.university.controller.rest;

import com.foxminded.university.controller.StudentController;
import com.foxminded.university.model.Student;
import com.foxminded.university.service.StudentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/students")
public class RestStudentController {

    private static final Logger logger = LoggerFactory.getLogger(StudentController.class);

    private final StudentService studentService;

    public RestStudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping
    public Page<Student> getAllStudents(Pageable pageable) {
        logger.debug("Show all students");

        return studentService.findAll(pageable);
    }

    @GetMapping("/{id}")
    public Student showStudent(@PathVariable int id) {
        logger.debug("Show student page with id {}", id);

        return studentService.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void create(@RequestBody Student student) {
        logger.debug("Create new student. Id {}", student.getId());
        studentService.save(student);
    }

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void update(@RequestBody Student student, @PathVariable int id) {
        logger.debug("Update student with id {}", id);
        studentService.save(student);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void delete(@RequestBody Student student) {
        logger.debug("Delete student with id {}", student.getId());
        studentService.delete(student);
    }
}