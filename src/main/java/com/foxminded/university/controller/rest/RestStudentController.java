package com.foxminded.university.controller.rest;

import com.foxminded.university.controller.StudentController;
import com.foxminded.university.dao.mapper.StudentDtoMapper;
import com.foxminded.university.dto.StudentDto;
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
    private final StudentDtoMapper studentDtoMapper;

    public RestStudentController(StudentService studentService, StudentDtoMapper studentDtoMapper) {
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
    public void create(@RequestBody StudentDto studentDto) {
        //logger.debug("Create new student. Id {}", student.getId());
        studentService.save(studentDtoMapper.dtoToStudent(studentDto));
    }

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void update(@RequestBody StudentDto studentDto, @PathVariable int id) {
        //logger.debug("Update student with id {}", id);
        studentService.save(studentDtoMapper.dtoToStudent(studentDto));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void delete(@RequestBody StudentDto studentDto) {
        //logger.debug("Delete student with id {}", student.getId());
        studentService.delete(studentDtoMapper.dtoToStudent(studentDto));
    }
}