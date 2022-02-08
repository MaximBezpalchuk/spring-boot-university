package com.foxminded.university.controller.rest;

import com.foxminded.university.controller.VacationController;
import com.foxminded.university.dao.mapper.LectureMapper;
import com.foxminded.university.dao.mapper.VacationMapper;
import com.foxminded.university.dto.ObjectListDto;
import com.foxminded.university.dto.VacationDto;
import com.foxminded.university.exception.BusyTeacherException;
import com.foxminded.university.model.Lecture;
import com.foxminded.university.model.Teacher;
import com.foxminded.university.model.Vacation;
import com.foxminded.university.service.LectureService;
import com.foxminded.university.service.TeacherService;
import com.foxminded.university.service.VacationService;
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
    public Page<VacationDto> getAllVacationsByTeacherId(@PathVariable int teacherId, Pageable pageable) {
        logger.debug("Show all vacations by teacher id {}", teacherId);

        return vacationService.findByTeacherId(pageable, teacherId).map(vacationMapper::vacationToDto);
    }

    @GetMapping("/{id}")
    public VacationDto showVacation(@PathVariable int id) {
        logger.debug("Show vacation with id {}", id);

        return vacationMapper.vacationToDto(vacationService.findById(id));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity createVacation(@PathVariable int teacherId, @RequestBody VacationDto vacationDto) {
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
    public ObjectListDto changeTeacherOnLectures(@PathVariable int teacherId,
                                                 @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
                                                 @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end) {
        logger.debug("Change teacher on lectures - teacher with id {}", teacherId);
        List<Lecture> lectures = lectureService.findByTeacherIdAndPeriod(teacherId, start, end);

        return new ObjectListDto(lectures.stream().map(lectureMapper::lectureToDto).collect(Collectors.toList()));
    }

    @PatchMapping("/lectures")
    public void autofillTeachersOnLectures(@PathVariable int teacherId,
                                           @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
                                           @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end) {
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
    public void update(@RequestBody VacationDto vacationDto, @PathVariable int teacherId) {
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
    public void delete(@RequestBody VacationDto vacationDto) {
        vacationService.delete(vacationMapper.dtoToVacation(vacationDto));
    }
}