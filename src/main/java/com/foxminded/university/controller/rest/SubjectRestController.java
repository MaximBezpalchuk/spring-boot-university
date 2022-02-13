package com.foxminded.university.controller.rest;

import com.foxminded.university.controller.SubjectController;
import com.foxminded.university.dao.mapper.SubjectMapper;
import com.foxminded.university.dto.HolidayDto;
import com.foxminded.university.dto.SubjectDto;
import com.foxminded.university.model.Subject;
import com.foxminded.university.service.SubjectService;
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
@RequestMapping("api/subjects")
public class SubjectRestController {

    private static final Logger logger = LoggerFactory.getLogger(SubjectController.class);

    private final SubjectService subjectService;
    private final SubjectMapper subjectMapper;

    public SubjectRestController(SubjectService subjectService, SubjectMapper subjectMapper) {
        this.subjectService = subjectService;
        this.subjectMapper = subjectMapper;
    }

    @GetMapping
    @Operation(summary = "Get all subjects")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Show all subjects"),
        @ApiResponse(responseCode = "404", description = "Subjects not found", content = @Content),
        @ApiResponse(responseCode = "500", description = "Internal error", content = @Content),
        @ApiResponse(responseCode = "500", description = "Internal error", content = @Content)})
    public Page<SubjectDto> getAllSubjects(Pageable pageable) {
        logger.debug("Show all subjects");

        return subjectService.findAll(pageable).map(subjectMapper::subjectToDto);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get an subject by its id")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Found the subject",
            content = { @Content(mediaType = "application/json", schema = @Schema(implementation = SubjectDto.class)) }),
        @ApiResponse(responseCode = "400", description = "Invalid id supplied", content = @Content),
        @ApiResponse(responseCode = "404", description = "Subject not found", content = @Content),
        @ApiResponse(responseCode = "500", description = "Internal error", content = @Content)})
    public SubjectDto showSubject(@Parameter(description = "Id of subject to be searched") @PathVariable int id) {
        logger.debug("Show subject page with id {}", id);

        return subjectMapper.subjectToDto(subjectService.findById(id));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create a new subject by its DTO")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Subject successfully created",
            content = { @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseEntity.class)) }),
        @ApiResponse(responseCode = "500", description = "Internal error", content = @Content)})
    public ResponseEntity create(@RequestBody SubjectDto subjectDto) {
        Subject subject = subjectService.save(subjectMapper.dtoToSubject(subjectDto));
        logger.debug("Create new subject. Id {}", subject.getId());
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
            .buildAndExpand(subject.getId()).toUri();

        return ResponseEntity.created(location).build();
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Update an existing subject by its DTO")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Subject successfully updated",content = @Content),
        @ApiResponse(responseCode = "400", description = "Invalid id supplied", content = @Content),
        @ApiResponse(responseCode = "404", description = "Subject not found", content = @Content),
        @ApiResponse(responseCode = "500", description = "Internal error", content = @Content)})
    public void update(@RequestBody SubjectDto subjectDto) {
        subjectService.save(subjectMapper.dtoToSubject(subjectDto));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete an existing subject by its id")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Subject successfully deleted", content = @Content),
        @ApiResponse(responseCode = "400", description = "Invalid id supplied", content = @Content),
        @ApiResponse(responseCode = "404", description = "Subject not found", content = @Content),
        @ApiResponse(responseCode = "500", description = "Internal error", content = @Content)})
    public void delete(@Parameter(description = "Id of subject to be deleted") @PathVariable int id) {
        subjectService.delete(id);
    }
}