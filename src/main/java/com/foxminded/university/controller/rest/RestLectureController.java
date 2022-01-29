package com.foxminded.university.controller.rest;

import com.foxminded.university.controller.LectureController;
import com.foxminded.university.model.Lecture;
import com.foxminded.university.service.LectureService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/lectures")
public class RestLectureController {
    private static final Logger logger = LoggerFactory.getLogger(LectureController.class);

    private final LectureService lectureService;

    public RestLectureController(LectureService lectureService) {
        this.lectureService = lectureService;
    }

    @GetMapping
    public Page<Lecture> getAllLectures(Pageable pageable) {
        logger.debug("Show all lectures");

        return lectureService.findAll(pageable);
    }

    @GetMapping("{id}")
    public Lecture showLecture(@PathVariable int id) {
        logger.debug("Show lecture with id {}", id);

        return lectureService.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void create(@RequestBody Lecture lecture) {
        logger.debug("Create new lecture. Id {}", lecture.getId());
        lectureService.save(lecture);
    }

    @PatchMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    public void update(@RequestBody Lecture lecture, @PathVariable int id) {
        logger.debug("Update lecture with id {}", id);
        lectureService.save(lecture);
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    public void delete(@RequestBody Lecture lecture) {
        logger.debug("Delete lecture with id {}", lecture.getId());
        lectureService.delete(lecture);
    }

    @PatchMapping("{id}/edit/teacher")
    @ResponseStatus(HttpStatus.OK)
    public void updateTeacher(@RequestBody Lecture lecture, @PathVariable int id) {
        logger.debug("Update lecture with id {}", id);
        lectureService.save(lecture);
    }
}