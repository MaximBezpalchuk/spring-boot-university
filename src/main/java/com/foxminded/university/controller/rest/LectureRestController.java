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
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("api/lectures")
public class LectureRestController {
    private static final Logger logger = LoggerFactory.getLogger(LectureController.class);

    private final LectureService lectureService;
    private final LectureDtoMapper lectureDtoMapper;

    public LectureRestController(LectureService lectureService, LectureDtoMapper lectureDtoMapper) {
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
    public ResponseEntity create(@RequestBody LectureDto lectureDto) {
        Lecture lecture = lectureService.save(lectureDtoMapper.dtoToLecture(lectureDto));
        logger.debug("Create new lecture. Id {}", lecture.getId());
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
            .buildAndExpand(lecture.getId()).toUri();

        return ResponseEntity.created(location).build();
    }

    @PatchMapping("{id}")
    public void update(@RequestBody LectureDto lectureDto) {
        lectureService.save(lectureDtoMapper.dtoToLecture(lectureDto));
    }

    @DeleteMapping("{id}")
    public void delete(@RequestBody LectureDto lectureDto) {
        lectureService.delete(lectureDtoMapper.dtoToLecture(lectureDto));
    }

    @PatchMapping("{id}/edit/teacher")
    public void updateTeacher(@RequestBody LectureDto lectureDto) {
        lectureService.save(lectureDtoMapper.dtoToLecture(lectureDto));
    }
}