package com.foxminded.university.controller.rest;

import com.foxminded.university.controller.LectureController;
import com.foxminded.university.dao.mapper.LectureMapper;
import com.foxminded.university.dto.HolidayDto;
import com.foxminded.university.dto.LectureDto;
import com.foxminded.university.model.Lecture;
import com.foxminded.university.service.LectureService;
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
@RequestMapping("api/lectures")
public class LectureRestController {

    private static final Logger logger = LoggerFactory.getLogger(LectureController.class);

    private final LectureService lectureService;
    private final LectureMapper lectureMapper;

    public LectureRestController(LectureService lectureService, LectureMapper lectureMapper) {
        this.lectureService = lectureService;
        this.lectureMapper = lectureMapper;
    }

    @GetMapping
    @Operation(summary = "Get all lectures")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Show all lectures"),
        @ApiResponse(responseCode = "404", description = "Lectures not found", content = @Content),
        @ApiResponse(responseCode = "500", description = "Internal error", content = @Content),
        @ApiResponse(responseCode = "500", description = "Internal error", content = @Content)})
    public Page<LectureDto> getAllLectures(Pageable pageable) {
        logger.debug("Show all lectures");

        return lectureService.findAll(pageable).map(lectureMapper::lectureToDto);
    }

    @GetMapping("{id}")
    @Operation(summary = "Get an lecture by its id")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Found the lecture",
            content = { @Content(mediaType = "application/json", schema = @Schema(implementation = LectureDto.class)) }),
        @ApiResponse(responseCode = "400", description = "Invalid id supplied", content = @Content),
        @ApiResponse(responseCode = "404", description = "Lecture not found", content = @Content),
        @ApiResponse(responseCode = "500", description = "Internal error", content = @Content)})
    public LectureDto showLecture(@Parameter(description = "Id of lecture to be searched") @PathVariable int id) {
        logger.debug("Show lecture with id {}", id);

        return lectureMapper.lectureToDto(lectureService.findById(id));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create a new lecture by its DTO")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Lecture successfully created",
            content = { @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseEntity.class)) }),
        @ApiResponse(responseCode = "500", description = "Internal error", content = @Content)})
    public ResponseEntity create(@RequestBody LectureDto lectureDto) {
        Lecture lecture = lectureService.save(lectureMapper.dtoToLecture(lectureDto));
        logger.debug("Create new lecture. Id {}", lecture.getId());
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
            .buildAndExpand(lecture.getId()).toUri();

        return ResponseEntity.created(location).build();
    }

    @PatchMapping("{id}")
    @Operation(summary = "Update an existing lecture by its DTO")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lecture successfully updated",content = @Content),
        @ApiResponse(responseCode = "400", description = "Invalid id supplied", content = @Content),
        @ApiResponse(responseCode = "404", description = "Lecture not found", content = @Content),
        @ApiResponse(responseCode = "500", description = "Internal error", content = @Content)})
    public void update(@RequestBody LectureDto lectureDto) {
        lectureService.save(lectureMapper.dtoToLecture(lectureDto));
    }

    @DeleteMapping("{id}")
    @Operation(summary = "Delete an existing lecture by its id")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lecture successfully deleted", content = @Content),
        @ApiResponse(responseCode = "400", description = "Invalid id supplied", content = @Content),
        @ApiResponse(responseCode = "404", description = "Lecture not found", content = @Content),
        @ApiResponse(responseCode = "500", description = "Internal error", content = @Content)})
    public void delete(@Parameter(description = "Id of lecture to be deleted") @PathVariable int id) {
        lectureService.delete(id);
    }

    @PatchMapping("{id}/edit/teacher")
    @Operation(summary = "Update an teacher holiday on existing lecture by lectures DTO")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lecture successfully updated",content = @Content),
        @ApiResponse(responseCode = "400", description = "Invalid id supplied", content = @Content),
        @ApiResponse(responseCode = "404", description = "Lecture not found", content = @Content),
        @ApiResponse(responseCode = "500", description = "Internal error", content = @Content)})
    public void updateTeacher(@RequestBody LectureDto lectureDto) {
        lectureService.save(lectureMapper.dtoToLecture(lectureDto));
    }
}