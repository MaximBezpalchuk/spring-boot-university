package com.foxminded.university.controller.rest;

import com.foxminded.university.controller.VacationController;
import com.foxminded.university.dao.mapper.LectureDtoMapper;
import com.foxminded.university.dao.mapper.VacationDtoMapper;
import com.foxminded.university.dto.LectureDto;
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
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/teachers/{teacherId}/vacations")
public class RestVacationController {

    private static final Logger logger = LoggerFactory.getLogger(VacationController.class);

    private final TeacherService teacherService;
    private final VacationService vacationService;
    private final LectureService lectureService;
    private final VacationDtoMapper vacationDtoMapper;
    private final LectureDtoMapper lectureDtoMapper;

    public RestVacationController(TeacherService teacherService, VacationService vacationService,
                                  LectureService lectureService, VacationDtoMapper vacationDtoMapper,
                                  LectureDtoMapper lectureDtoMapper) {
        this.teacherService = teacherService;
        this.vacationService = vacationService;
        this.lectureService = lectureService;
        this.vacationDtoMapper = vacationDtoMapper;
        this.lectureDtoMapper = lectureDtoMapper;
    }

    @GetMapping
    public Page<VacationDto> getAllVacationsByTeacherId(@PathVariable int teacherId, Pageable pageable) {
        logger.debug("Show all vacations by teacher id {}", teacherId);

        return vacationService.findByTeacherId(pageable, teacherId).map(vacationDtoMapper::vacationToDto);
    }

    @GetMapping("/{id}")
    public VacationDto showVacation(@PathVariable int id) {
        logger.debug("Show vacation with id {}", id);

        return vacationDtoMapper.vacationToDto(vacationService.findById(id));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createVacation(@PathVariable int teacherId, @RequestBody VacationDto vacationDto) {
        Vacation vacation = vacationDtoMapper.dtoToVacation(vacationDto);
        List<Lecture> lectures = lectureService.findByTeacherIdAndPeriod(teacherId, vacation.getStart(),
            vacation.getEnd());
        if (lectures.isEmpty()) {
            //logger.debug("Create new vacation. Id {}", vacation.getId());
            vacationService.save(vacation);
        } else {
            logger.debug("Vacation wasn`t created! Need to change teacher in some lectures");
            throw new BusyTeacherException("Teacher is on another lecture this time! Teacher is: " +
                vacation.getTeacher().getFirstName() + " " + vacation.getTeacher().getLastName());
        }

    }

    @GetMapping("/lectures")
    public List<LectureDto> changeTeacherOnLectures(@PathVariable int teacherId,
                                                    @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
                                                    @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end) {
        logger.debug("Change teacher on lectures - teacher with id {}", teacherId);

        return lectureService.findByTeacherIdAndPeriod(teacherId, start, end).stream().map(lectureDtoMapper::lectureToDto).collect(Collectors.toList());
    }

    @PatchMapping("/lectures")
    @ResponseStatus(HttpStatus.OK)
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
    @ResponseStatus(HttpStatus.OK)
    public void update(@RequestBody VacationDto vacationDto, @PathVariable int teacherId, @PathVariable int id) {
        Vacation vacation = vacationDtoMapper.dtoToVacation(vacationDto);
        List<Lecture> lectures = lectureService.findByTeacherIdAndPeriod(teacherId, vacation.getStart(),
            vacation.getEnd());
        if (lectures.isEmpty()) {
            //logger.debug("Update vacation with id {}", id);
            vacationService.save(vacation);
        } else {
            logger.debug("Vacation wasn`t created! Need to change teacher in some lectures");
            throw new BusyTeacherException("Teacher is on another lecture this time! Teacher is: " +
                vacation.getTeacher().getFirstName() + " " + vacation.getTeacher().getLastName());
        }
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void delete(@RequestBody VacationDto vacationDto) {
        //logger.debug("Delete vacation with id {}", vacation.getId());
        vacationService.delete(vacationDtoMapper.dtoToVacation(vacationDto));
    }
}