package com.foxminded.university.controller.rest;

import com.foxminded.university.controller.TeacherController;
import com.foxminded.university.dao.mapper.TeacherMapper;
import com.foxminded.university.dto.HolidayDto;
import com.foxminded.university.dto.TeacherDto;
import com.foxminded.university.model.Teacher;
import com.foxminded.university.service.TeacherService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
    @Operation(summary = "Get all teachers")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Show all teachers"),
        @ApiResponse(responseCode = "404", description = "Teachers not found", content = @Content),
        @ApiResponse(responseCode = "500", description = "Internal error", content = @Content),
        @ApiResponse(responseCode = "500", description = "Internal error", content = @Content)})
    public Page<TeacherDto> all(Pageable pageable) {
        logger.debug("Show all teachers");

        return teacherService.findAll(pageable).map(teacherMapper::teacherToDto);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get an teacher by its id")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Found the teacher",
            content = { @Content(mediaType = "application/json", schema = @Schema(implementation = TeacherDto.class)) }),
        @ApiResponse(responseCode = "400", description = "Invalid id supplied", content = @Content),
        @ApiResponse(responseCode = "404", description = "Teacher not found", content = @Content),
        @ApiResponse(responseCode = "500", description = "Internal error", content = @Content)})
    public TeacherDto showTeacher(@Parameter(description = "Id of teacher to be searched") @PathVariable int id) {
        logger.debug("Show teacher with id {}", id);

        return teacherMapper.teacherToDto(teacherService.findById(id));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create a new teacher by its DTO")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Teacher successfully created",
            content = { @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseEntity.class)) }),
        @ApiResponse(responseCode = "500", description = "Internal error", content = @Content)})
    public ResponseEntity create(@RequestBody TeacherDto teacherDto) {
        Teacher teacher = teacherService.save(teacherMapper.dtoToTeacher(teacherDto));
        logger.debug("Create new teacher. Id {}", teacher.getId());
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
            .buildAndExpand(teacher.getId()).toUri();

        return ResponseEntity.created(location).build();
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Update an existing teacher by its DTO")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Teacher successfully updated",content = @Content),
        @ApiResponse(responseCode = "400", description = "Invalid id supplied", content = @Content),
        @ApiResponse(responseCode = "404", description = "Teacher not found", content = @Content),
        @ApiResponse(responseCode = "500", description = "Internal error", content = @Content)})
    public void update(@RequestBody TeacherDto teacherDto) {
        teacherService.save(teacherMapper.dtoToTeacher(teacherDto));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete an existing teacher by its id")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Teacher successfully deleted", content = @Content),
        @ApiResponse(responseCode = "400", description = "Invalid id supplied", content = @Content),
        @ApiResponse(responseCode = "404", description = "Teacher not found", content = @Content),
        @ApiResponse(responseCode = "500", description = "Internal error", content = @Content)})
    public void delete(@Parameter(description = "Id of teacher to be deleted") @PathVariable int id) {
        teacherService.delete(id);
    }
}