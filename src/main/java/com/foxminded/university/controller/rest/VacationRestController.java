package com.foxminded.university.controller.rest;

import com.foxminded.university.controller.VacationController;
import com.foxminded.university.dao.mapper.LectureMapper;
import com.foxminded.university.dao.mapper.VacationMapper;
import com.foxminded.university.dto.HolidayDto;
import com.foxminded.university.dto.Slice;
import com.foxminded.university.dto.VacationDto;
import com.foxminded.university.exception.BusyTeacherException;
import com.foxminded.university.model.Lecture;
import com.foxminded.university.model.Teacher;
import com.foxminded.university.model.Vacation;
import com.foxminded.university.service.LectureService;
import com.foxminded.university.service.TeacherService;
import com.foxminded.university.service.VacationService;
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
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/teachers/{teacherId}/vacations")
public class VacationRestController {

    private static final Logger logger = LoggerFactory.getLogger(VacationController.class);

    private final TeacherService teacherService;
    private final VacationService vacationService;
    private final LectureService lectureService;
    private final VacationMapper vacationMapper;
    private final LectureMapper lectureMapper;

    public VacationRestController(TeacherService teacherService, VacationService vacationService,
                                  LectureService lectureService, VacationMapper vacationMapper,
                                  LectureMapper lectureMapper) {
        this.teacherService = teacherService;
        this.vacationService = vacationService;
        this.lectureService = lectureService;
        this.vacationMapper = vacationMapper;
        this.lectureMapper = lectureMapper;
    }

    @GetMapping
    @Operation(summary = "Get all vacations by teacher id")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Show all vacations"),
        @ApiResponse(responseCode = "404", description = "Vacations not found", content = @Content),
        @ApiResponse(responseCode = "500", description = "Internal error", content = @Content),
        @ApiResponse(responseCode = "500", description = "Internal error", content = @Content)})
    public Page<VacationDto> getAllVacationsByTeacherId(@Parameter(description = "Id of teacher to search its vacations") @PathVariable int teacherId, Pageable pageable) {
        logger.debug("Show all vacations by teacher id {}", teacherId);

        return vacationService.findByTeacherId(pageable, teacherId).map(vacationMapper::vacationToDto);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get an vacation by its id")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Found the vacation",
            content = { @Content(mediaType = "application/json", schema = @Schema(implementation = VacationDto.class)) }),
        @ApiResponse(responseCode = "400", description = "Invalid id supplied", content = @Content),
        @ApiResponse(responseCode = "404", description = "Vacation not found", content = @Content),
        @ApiResponse(responseCode = "500", description = "Internal error", content = @Content)})
    public VacationDto showVacation(@Parameter(description = "Id of teacher that vacation need to search") @PathVariable int teacherId, @Parameter(description = "Id of vacation to be searched") @PathVariable int id) {
        logger.debug("Show vacation with id {}", id);

        return vacationMapper.vacationToDto(vacationService.findByTeacherIdAndId(teacherId, id));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create a new vacation by its DTO")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Vacation successfully created",
            content = { @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseEntity.class)) }),
        @ApiResponse(responseCode = "500", description = "Internal error", content = @Content)})
    public ResponseEntity createVacation(@Parameter(description = "Id of teacher to create its vacations") @PathVariable int teacherId, @RequestBody VacationDto vacationDto) {
        Vacation vacation;
        Vacation vacationFromDto = vacationMapper.dtoToVacation(vacationDto);
        List<Lecture> lectures = lectureService.findByTeacherIdAndPeriod(teacherId, vacationFromDto.getStart(),
            vacationFromDto.getEnd());
        if (lectures.isEmpty()) {
            vacation = vacationService.save(vacationFromDto);
            logger.debug("Create new vacation. Id {}", vacation.getId());
        } else {
            logger.debug("Vacation wasn`t created! Need to change teacher in some lectures");
            throw new BusyTeacherException("Teacher is on another lecture this time! Teacher is: " +
                vacationFromDto.getTeacher().getFirstName() + " " + vacationFromDto.getTeacher().getLastName());
        }
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
            .buildAndExpand(vacation.getId()).toUri();

        return ResponseEntity.created(location).build();
    }

    @GetMapping("/lectures")
    @Operation(summary = "Get all lectures by period")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Show all lectures by period",
            content = { @Content(mediaType = "application/json", schema = @Schema(implementation = Slice.class)) }),
        @ApiResponse(responseCode = "404", description = "Lectures not found", content = @Content),
        @ApiResponse(responseCode = "500", description = "Internal error", content = @Content),
        @ApiResponse(responseCode = "500", description = "Internal error", content = @Content)})
    public Slice changeTeacherOnLectures(@Parameter(description = "Id of teacher to search lectures with this teacher") @PathVariable int teacherId,
                                         @Parameter(description = "Start period") @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
                                         @Parameter(description = "End period") @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end) {
        logger.debug("Change teacher on lectures - teacher with id {}", teacherId);
        List<Lecture> lectures = lectureService.findByTeacherIdAndPeriod(teacherId, start, end);

        return new Slice(lectures.stream().map(lectureMapper::lectureToDto).collect(Collectors.toList()));
    }

    @PatchMapping("/lectures")
    @Operation(summary = "Update an existing lectures with new teacher by random")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lectures successfully updated",content = @Content),
        @ApiResponse(responseCode = "400", description = "Invalid id supplied", content = @Content),
        @ApiResponse(responseCode = "404", description = "Lectures not found", content = @Content),
        @ApiResponse(responseCode = "500", description = "Internal error", content = @Content)})
    public void autofillTeachersOnLectures(@Parameter(description = "Id of teacher to search lectures with this teacher") @PathVariable int teacherId,
                                           @Parameter(description = "Start period") @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
                                           @Parameter(description = "End period") @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end) {
        logger.debug("Autofill teacher on lectures - teacher with id {}", teacherId);
        List<Lecture> lectures = lectureService.findByTeacherIdAndPeriod(teacherId, start, end);
        for (Lecture lecture : lectures) {
            List<Teacher> teachers = teacherService.findTeachersForChange(lecture);
            Random rand = new Random();
            lecture.setTeacher(teachers.get(rand.nextInt(teachers.size()))); // set random free teacher
            lectureService.save(lecture);
        }
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Update an existing vacation by its DTO")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Vacation successfully updated",content = @Content),
        @ApiResponse(responseCode = "400", description = "Invalid id supplied", content = @Content),
        @ApiResponse(responseCode = "404", description = "Vacation not found", content = @Content),
        @ApiResponse(responseCode = "500", description = "Internal error", content = @Content)})
    public void update(@RequestBody VacationDto vacationDto, @Parameter(description = "Id of teacher to update its vacations") @PathVariable int teacherId) {
        Vacation vacation = vacationMapper.dtoToVacation(vacationDto);
        List<Lecture> lectures = lectureService.findByTeacherIdAndPeriod(teacherId, vacation.getStart(),
            vacation.getEnd());
        if (lectures.isEmpty()) {
            vacationService.save(vacation);
        } else {
            logger.debug("Vacation wasn`t created! Need to change teacher in some lectures");
            throw new BusyTeacherException("Teacher is on another lecture this time! Teacher is: " +
                vacation.getTeacher().getFirstName() + " " + vacation.getTeacher().getLastName());
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete an existing vacation by its id")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Holiday successfully deleted", content = @Content),
        @ApiResponse(responseCode = "400", description = "Invalid id supplied", content = @Content),
        @ApiResponse(responseCode = "404", description = "Holiday not found", content = @Content),
        @ApiResponse(responseCode = "500", description = "Internal error", content = @Content)})
    public void delete(@Parameter(description = "Id of vacation to be deleted") @PathVariable int id) {
        vacationService.delete(id);
    }
}