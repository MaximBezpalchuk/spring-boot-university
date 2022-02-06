package com.foxminded.university.controller.rest;

import com.foxminded.university.controller.LectureTimeController;
import com.foxminded.university.dao.mapper.LectureTimeDtoMapper;
import com.foxminded.university.dto.LectureTimeDto;
import com.foxminded.university.dto.ObjectListDto;
import com.foxminded.university.model.LectureTime;
import com.foxminded.university.service.LectureTimeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/lecturetimes")
public class LectureTimeRestController {

    private static final Logger logger = LoggerFactory.getLogger(LectureTimeController.class);

    private final LectureTimeService lectureTimeService;
    private final LectureTimeDtoMapper lectureTimeDtoMapper;

    public LectureTimeRestController(LectureTimeService lectureTimeService, LectureTimeDtoMapper lectureTimeDtoMapper) {
        this.lectureTimeService = lectureTimeService;
        this.lectureTimeDtoMapper = lectureTimeDtoMapper;
    }

    @GetMapping
    public ObjectListDto getAllLectureTimes() {
        logger.debug("Show all lecture times");

        return new ObjectListDto(lectureTimeService.findAll().stream().map(lectureTimeDtoMapper::lectureTimeToDto).collect(Collectors.toList()));
    }

    @GetMapping("/{id}")
    public LectureTimeDto showLectureTime(@PathVariable int id) {
        logger.debug("Show lecture time with id {}", id);

        return lectureTimeDtoMapper.lectureTimeToDto(lectureTimeService.findById(id));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity create(@RequestBody LectureTimeDto lectureTimeDto) {
        LectureTime lectureTime = lectureTimeService.save(lectureTimeDtoMapper.dtoToLectureTime(lectureTimeDto));
        logger.debug("Create new lecture time. Id {}", lectureTime.getId());
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
            .buildAndExpand(lectureTime.getId()).toUri();

        return ResponseEntity.created(location).build();
    }

    @PatchMapping("/{id}")
    public void update(@RequestBody LectureTimeDto lectureTimeDto) {
        lectureTimeService.save(lectureTimeDtoMapper.dtoToLectureTime(lectureTimeDto));
    }

    @DeleteMapping("/{id}")
    public void delete(@RequestBody LectureTimeDto lectureTimeDto) {
        lectureTimeService.delete(lectureTimeDtoMapper.dtoToLectureTime(lectureTimeDto));
    }
}