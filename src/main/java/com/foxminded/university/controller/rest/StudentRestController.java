package com.foxminded.university.controller.rest;

import com.foxminded.university.controller.StudentController;
import com.foxminded.university.dao.mapper.StudentDtoMapper;
import com.foxminded.university.dto.StudentDto;
import com.foxminded.university.model.Student;
import com.foxminded.university.service.StudentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("api/students")
public class StudentRestController {

    private static final Logger logger = LoggerFactory.getLogger(StudentController.class);

    private final StudentService studentService;
    private final StudentDtoMapper studentDtoMapper;

    public StudentRestController(StudentService studentService, StudentDtoMapper studentDtoMapper) {
        this.studentService = studentService;
        this.studentDtoMapper = studentDtoMapper;
    }

    @GetMapping
    public Page<StudentDto> getAllStudents(Pageable pageable) {
        logger.debug("Show all students");

        return studentService.findAll(pageable).map(studentDtoMapper::studentToDto);
    }

    @GetMapping("/{id}")
    public StudentDto showStudent(@PathVariable int id) {
        logger.debug("Show student page with id {}", id);

        return studentDtoMapper.studentToDto(studentService.findById(id));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity create(@RequestBody StudentDto studentDto) {
        Student student = studentService.save(studentDtoMapper.dtoToStudent(studentDto));
        logger.debug("Create new student. Id {}", student.getId());
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
            .buildAndExpand(student.getId()).toUri();

        return ResponseEntity.created(location).build();
    }

    @PatchMapping("/{id}")
    public void update(@RequestBody StudentDto studentDto) {
        studentService.save(studentDtoMapper.dtoToStudent(studentDto));
    }

    @DeleteMapping("/{id}")
    public void delete(@RequestBody StudentDto studentDto) {
        studentService.delete(studentDtoMapper.dtoToStudent(studentDto));
    }
}