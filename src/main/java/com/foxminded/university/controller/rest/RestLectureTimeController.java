package com.foxminded.university.controller.rest;

import com.foxminded.university.controller.LectureTimeController;
import com.foxminded.university.model.LectureTime;
import com.foxminded.university.service.LectureTimeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/lecturetimes")
public class RestLectureTimeController {

    private static final Logger logger = LoggerFactory.getLogger(LectureTimeController.class);

    private final LectureTimeService lectureTimeService;

    public RestLectureTimeController(LectureTimeService lectureTimeService) {
        this.lectureTimeService = lectureTimeService;
    }

    @GetMapping
    public List<LectureTime> getAllLectureTimes() {
        logger.debug("Show all lecture times");

        return lectureTimeService.findAll();
    }

    @GetMapping("/{id}")
    public LectureTime showLectureTime(@PathVariable int id) {
        logger.debug("Show lecture time with id {}", id);

        return lectureTimeService.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void create(@RequestBody LectureTime lectureTime) {
        logger.debug("Create new lecture time. Id {}", lectureTime.getId());
        lectureTimeService.save(lectureTime);
    }

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void update(@RequestBody LectureTime lectureTime, @PathVariable int id) {
        logger.debug("Update lecture time with id {}", id);
        lectureTimeService.save(lectureTime);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void delete(@RequestBody LectureTime lectureTime) {
        logger.debug("Delete lecture time with id {}", lectureTime.getId());
        lectureTimeService.delete(lectureTime);
    }
}