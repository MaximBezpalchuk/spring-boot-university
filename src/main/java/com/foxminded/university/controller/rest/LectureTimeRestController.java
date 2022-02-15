package com.foxminded.university.controller.rest;

import com.foxminded.university.controller.LectureTimeController;
import com.foxminded.university.dao.mapper.LectureTimeMapper;
import com.foxminded.university.dto.AudienceDto;
import com.foxminded.university.dto.LectureTimeDto;
import com.foxminded.university.dto.Slice;
import com.foxminded.university.model.LectureTime;
import com.foxminded.university.service.LectureTimeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/lecturetimes")
public class LectureTimeRestController {

    private static final Logger logger = LoggerFactory.getLogger(LectureTimeController.class);

    private final LectureTimeService lectureTimeService;
    private final LectureTimeMapper lectureTimeMapper;

    public LectureTimeRestController(LectureTimeService lectureTimeService, LectureTimeMapper lectureTimeMapper) {
        this.lectureTimeService = lectureTimeService;
        this.lectureTimeMapper = lectureTimeMapper;
    }

    @GetMapping
    @Operation(summary = "Get all lecture times")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Show all lecture times",
            content = { @Content(mediaType = "application/json", schema = @Schema(implementation = Slice.class)) }),
        @ApiResponse(responseCode = "404", description = "Lecture times not found", content = @Content),
        @ApiResponse(responseCode = "500", description = "Internal error", content = @Content)})
    public Slice getAllLectureTimes() {
        logger.debug("Show all lecture times");

        return new Slice(lectureTimeService.findAll().stream().map(lectureTimeMapper::lectureTimeToDto).collect(Collectors.toList()));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get an lecture time by its id")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Found the lecture time",
            content = { @Content(mediaType = "application/json", schema = @Schema(implementation = LectureTimeDto.class)) }),
        @ApiResponse(responseCode = "400", description = "Invalid id supplied", content = @Content),
        @ApiResponse(responseCode = "404", description = "Lecture time not found", content = @Content),
        @ApiResponse(responseCode = "500", description = "Internal error", content = @Content)})
    public LectureTimeDto showLectureTime(@Parameter(description = "Id of lecture time to be searched") @PathVariable int id) {
        logger.debug("Show lecture time with id {}", id);

        return lectureTimeMapper.lectureTimeToDto(lectureTimeService.findById(id));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create a new lecture time by its DTO")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Lecture time successfully created",
            content = { @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseEntity.class)) }),
        @ApiResponse(responseCode = "500", description = "Internal error", content = @Content)})
    public ResponseEntity create(@RequestBody LectureTimeDto lectureTimeDto) {
        LectureTime lectureTime = lectureTimeService.save(lectureTimeMapper.dtoToLectureTime(lectureTimeDto));
        logger.debug("Create new lecture time. Id {}", lectureTime.getId());
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
            .buildAndExpand(lectureTime.getId()).toUri();

        return ResponseEntity.created(location).build();
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Update an existing lecture time by its DTO")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lecture time successfully updated",content = @Content),
        @ApiResponse(responseCode = "400", description = "Invalid id supplied", content = @Content),
        @ApiResponse(responseCode = "404", description = "Lecture time not found", content = @Content),
        @ApiResponse(responseCode = "500", description = "Internal error", content = @Content)})
    public void update(@RequestBody LectureTimeDto lectureTimeDto) {
        lectureTimeService.save(lectureTimeMapper.dtoToLectureTime(lectureTimeDto));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete an existing lecture time by its id")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lecture time successfully deleted", content = @Content),
        @ApiResponse(responseCode = "400", description = "Invalid id supplied", content = @Content),
        @ApiResponse(responseCode = "404", description = "Lecture time not found", content = @Content),
        @ApiResponse(responseCode = "500", description = "Internal error", content = @Content)})
    public void delete(@Parameter(description = "Id of lecture time to be deleted") @PathVariable int id) {
        lectureTimeService.delete(id);
    }
}