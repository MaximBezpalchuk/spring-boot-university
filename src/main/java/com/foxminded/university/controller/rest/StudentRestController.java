package com.foxminded.university.controller.rest;

import com.foxminded.university.controller.StudentController;
import com.foxminded.university.dao.mapper.StudentMapper;
import com.foxminded.university.dto.HolidayDto;
import com.foxminded.university.dto.StudentDto;
import com.foxminded.university.model.Student;
import com.foxminded.university.service.StudentService;
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
@RequestMapping("api/students")
public class StudentRestController {

    private static final Logger logger = LoggerFactory.getLogger(StudentController.class);

    private final StudentService studentService;
    private final StudentMapper studentMapper;

    public StudentRestController(StudentService studentService, StudentMapper studentMapper) {
        this.studentService = studentService;
        this.studentMapper = studentMapper;
    }

    @GetMapping
    @Operation(summary = "Get all students")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Show all students"),
        @ApiResponse(responseCode = "404", description = "Students not found", content = @Content),
        @ApiResponse(responseCode = "500", description = "Internal error", content = @Content),
        @ApiResponse(responseCode = "500", description = "Internal error", content = @Content)})
    public Page<StudentDto> getAllStudents(Pageable pageable) {
        logger.debug("Show all students");

        return studentService.findAll(pageable).map(studentMapper::studentToDto);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get an student by its id")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Found the student",
            content = { @Content(mediaType = "application/json", schema = @Schema(implementation = StudentDto.class)) }),
        @ApiResponse(responseCode = "400", description = "Invalid id supplied", content = @Content),
        @ApiResponse(responseCode = "404", description = "Student not found", content = @Content),
        @ApiResponse(responseCode = "500", description = "Internal error", content = @Content)})
    public StudentDto showStudent(@Parameter(description = "Id of student to be searched") @PathVariable int id) {
        logger.debug("Show student page with id {}", id);

        return studentMapper.studentToDto(studentService.findById(id));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create a new student by its DTO")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Student successfully created",
            content = { @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseEntity.class)) }),
        @ApiResponse(responseCode = "500", description = "Internal error", content = @Content)})
    public ResponseEntity create(@RequestBody StudentDto studentDto) {
        Student student = studentService.save(studentMapper.dtoToStudent(studentDto));
        logger.debug("Create new student. Id {}", student.getId());
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
            .buildAndExpand(student.getId()).toUri();

        return ResponseEntity.created(location).build();
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Update an existing student by its DTO")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Student successfully updated",content = @Content),
        @ApiResponse(responseCode = "400", description = "Invalid id supplied", content = @Content),
        @ApiResponse(responseCode = "404", description = "Student not found", content = @Content),
        @ApiResponse(responseCode = "500", description = "Internal error", content = @Content)})
    public void update(@RequestBody StudentDto studentDto) {
        studentService.save(studentMapper.dtoToStudent(studentDto));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete an existing student by its id")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Student successfully deleted", content = @Content),
        @ApiResponse(responseCode = "400", description = "Invalid id supplied", content = @Content),
        @ApiResponse(responseCode = "404", description = "Student not found", content = @Content),
        @ApiResponse(responseCode = "500", description = "Internal error", content = @Content)})
    public void delete(@Parameter(description = "Id of student to be deleted") @PathVariable int id) {
        studentService.delete(id);
    }
}