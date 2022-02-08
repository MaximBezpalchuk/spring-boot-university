package com.foxminded.university.controller.rest;

import com.foxminded.university.controller.TeacherController;
import com.foxminded.university.dao.mapper.TeacherMapper;
import com.foxminded.university.dto.TeacherDto;
import com.foxminded.university.model.Teacher;
import com.foxminded.university.service.TeacherService;
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
@RequestMapping("api/teachers")
public class TeacherRestController {

    private static final Logger logger = LoggerFactory.getLogger(TeacherController.class);
    private final TeacherService teacherService;
    private final TeacherMapper teacherMapper;

    public TeacherRestController(TeacherService teacherService, TeacherMapper teacherMapper) {
        this.teacherService = teacherService;
        this.teacherMapper = teacherMapper;
    }

    @GetMapping
    public Page<TeacherDto> all(Pageable pageable) {
        logger.debug("Show all teachers");

        return teacherService.findAll(pageable).map(teacherMapper::teacherToDto);
    }

    @GetMapping("/{id}")
    public TeacherDto showTeacher(@PathVariable int id) {
        logger.debug("Show teacher with id {}", id);

        return teacherMapper.teacherToDto(teacherService.findById(id));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity create(@RequestBody TeacherDto teacherDto) {
        Teacher teacher = teacherService.save(teacherMapper.dtoToTeacher(teacherDto));
        logger.debug("Create new teacher. Id {}", teacher.getId());
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
            .buildAndExpand(teacher.getId()).toUri();

        return ResponseEntity.created(location).build();
    }

    @PatchMapping("/{id}")
    public void update(@RequestBody TeacherDto teacherDto) {
        teacherService.save(teacherMapper.dtoToTeacher(teacherDto));
    }

    @DeleteMapping("/{id}")
    public void delete(@RequestBody TeacherDto teacherDto) {
        teacherService.delete(teacherMapper.dtoToTeacher(teacherDto));
    }
}