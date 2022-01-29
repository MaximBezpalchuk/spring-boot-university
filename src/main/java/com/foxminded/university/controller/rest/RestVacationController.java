package com.foxminded.university.controller.rest;

import com.foxminded.university.controller.VacationController;
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

@RestController
@RequestMapping("api/teachers/{teacherId}/vacations")
public class RestVacationController {

    private static final Logger logger = LoggerFactory.getLogger(VacationController.class);

    private final TeacherService teacherService;
    private final VacationService vacationService;
    private final LectureService lectureService;

    public RestVacationController(TeacherService teacherService, VacationService vacationService,
                                  LectureService lectureService) {
        this.teacherService = teacherService;
        this.vacationService = vacationService;
        this.lectureService = lectureService;
    }

    @GetMapping
    public Page<Vacation> getAllVacationsByTeacherId(@PathVariable int teacherId, Pageable pageable) {
        logger.debug("Show all vacations by teacher id {}", teacherId);

        return vacationService.findByTeacherId(pageable, teacherId);
    }

    @GetMapping("/{id}")
    public Vacation showVacation(@PathVariable int id) {
        logger.debug("Show vacation with id {}", id);

        return vacationService.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createVacation(@PathVariable int teacherId, @RequestBody Vacation vacation) {
        List<Lecture> lectures = lectureService.findByTeacherIdAndPeriod(teacherId, vacation.getStart(),
            vacation.getEnd());
        if (lectures.isEmpty()) {
            logger.debug("Create new vacation. Id {}", vacation.getId());
            vacationService.save(vacation);
        } else {
            logger.debug("Vacation wasn`t created! Need to change teacher in some lectures");
            throw new BusyTeacherException("Teacher is on another lecture this time! Teacher is: " +
                vacation.getTeacher().getFirstName() + " " + vacation.getTeacher().getLastName());
        }

    }

    @GetMapping("/lectures")
    public List<Lecture> changeTeacherOnLectures(@PathVariable int teacherId,
                                                 @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
                                                 @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end) {
        logger.debug("Change teacher on lectures - teacher with id {}", teacherId);

        return lectureService.findByTeacherIdAndPeriod(teacherId, start, end);
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
    public void update(@RequestBody Vacation vacation, @PathVariable int teacherId, @PathVariable int id) {
        List<Lecture> lectures = lectureService.findByTeacherIdAndPeriod(teacherId, vacation.getStart(),
            vacation.getEnd());
        if (lectures.isEmpty()) {
            logger.debug("Update vacation with id {}", id);
            vacationService.save(vacation);
        } else {
            logger.debug("Vacation wasn`t created! Need to change teacher in some lectures");
            throw new BusyTeacherException("Teacher is on another lecture this time! Teacher is: " +
                vacation.getTeacher().getFirstName() + " " + vacation.getTeacher().getLastName());
        }
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void delete(@RequestBody Vacation vacation) {
        logger.debug("Delete vacation with id {}", vacation.getId());
        vacationService.delete(vacation);
    }
}