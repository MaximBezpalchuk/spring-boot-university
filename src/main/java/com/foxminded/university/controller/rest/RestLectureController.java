package com.foxminded.university.controller.rest;

import com.foxminded.university.controller.LectureController;
import com.foxminded.university.dao.mapper.LectureDtoMapper;
import com.foxminded.university.dto.LectureDto;
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
    private final LectureDtoMapper lectureDtoMapper;

    public RestLectureController(LectureService lectureService, LectureDtoMapper lectureDtoMapper) {
        this.lectureService = lectureService;
        this.lectureDtoMapper = lectureDtoMapper;
    }

    @GetMapping
    public Page<LectureDto> getAllLectures(Pageable pageable) {
        logger.debug("Show all lectures");

        return lectureService.findAll(pageable).map(lectureDtoMapper::lectureToDto);
    }

    @GetMapping("{id}")
    public LectureDto showLecture(@PathVariable int id) {
        logger.debug("Show lecture with id {}", id);

        return lectureDtoMapper.lectureToDto(lectureService.findById(id));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void create(@RequestBody LectureDto lectureDto) {
        //logger.debug("Create new lecture. Id {}", lecture.getId());
        Lecture lecture= lectureDtoMapper.dtoToLecture(lectureDto);
        lectureService.save(lecture);
    }

    @PatchMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    public void update(@RequestBody LectureDto lectureDto, @PathVariable int id) {
        //logger.debug("Update lecture with id {}", id);
        lectureService.save(lectureDtoMapper.dtoToLecture(lectureDto));
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    public void delete(@RequestBody LectureDto lectureDto) {
        //logger.debug("Delete lecture with id {}", lecture.getId());
        lectureService.delete(lectureDtoMapper.dtoToLecture(lectureDto));
    }

    @PatchMapping("{id}/edit/teacher")
    @ResponseStatus(HttpStatus.OK)
    public void updateTeacher(@RequestBody LectureDto lectureDto, @PathVariable int id) {
        //logger.debug("Update lecture with id {}", id);
        lectureService.save(lectureDtoMapper.dtoToLecture(lectureDto));
    }
}