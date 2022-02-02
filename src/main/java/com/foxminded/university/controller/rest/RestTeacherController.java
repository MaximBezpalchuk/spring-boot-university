package com.foxminded.university.controller.rest;

import com.foxminded.university.controller.TeacherController;
import com.foxminded.university.dao.mapper.TeacherDtoMapper;
import com.foxminded.university.dto.TeacherDto;
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
    private final TeacherDtoMapper teacherDtoMapper;

    public RestTeacherController(TeacherService teacherService, TeacherDtoMapper teacherDtoMapper) {
        this.teacherService = teacherService;
        this.teacherDtoMapper = teacherDtoMapper;
    }

    @GetMapping
    public Page<TeacherDto> all(Pageable pageable) {
        logger.debug("Show all teachers");

        return teacherService.findAll(pageable).map(teacherDtoMapper::teacherToDto);
    }

    @GetMapping("/{id}")
    public TeacherDto showTeacher(@PathVariable int id) {
        logger.debug("Show teacher with id {}", id);

        return teacherDtoMapper.teacherToDto(teacherService.findById(id));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void create(@RequestBody TeacherDto teacherDto) {
        //logger.debug("Create new teacher. Id {}", teacher.getId());
        teacherService.save(teacherDtoMapper.dtoToTeacher(teacherDto));
    }

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void update(@RequestBody TeacherDto teacherDto, @PathVariable int id) {
        //logger.debug("Update teacher with id {}", id);
        teacherService.save(teacherDtoMapper.dtoToTeacher(teacherDto));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void delete(@RequestBody TeacherDto teacherDto) {
        //logger.debug("Delete teacher with id {}", teacher.getId());
        teacherService.delete(teacherDtoMapper.dtoToTeacher(teacherDto));
    }
}