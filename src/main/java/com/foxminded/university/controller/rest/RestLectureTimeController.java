package com.foxminded.university.controller.rest;

import com.foxminded.university.controller.LectureTimeController;
import com.foxminded.university.dao.mapper.LectureTimeDtoMapper;
import com.foxminded.university.dto.LectureTimeDto;
import com.foxminded.university.model.LectureTime;
import com.foxminded.university.service.LectureTimeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/lecturetimes")
public class RestLectureTimeController {

    private static final Logger logger = LoggerFactory.getLogger(LectureTimeController.class);

    private final LectureTimeService lectureTimeService;
    private final LectureTimeDtoMapper lectureTimeDtoMapper;

    public RestLectureTimeController(LectureTimeService lectureTimeService, LectureTimeDtoMapper lectureTimeDtoMapper) {
        this.lectureTimeService = lectureTimeService;
        this.lectureTimeDtoMapper = lectureTimeDtoMapper;
    }

    @GetMapping
    public List<LectureTimeDto> getAllLectureTimes() {
        logger.debug("Show all lecture times");

        return lectureTimeService.findAll().stream().map(lectureTimeDtoMapper::lectureTimeToDto).collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public LectureTimeDto showLectureTime(@PathVariable int id) {
        logger.debug("Show lecture time with id {}", id);

        return lectureTimeDtoMapper.lectureTimeToDto(lectureTimeService.findById(id));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void create(@RequestBody LectureTimeDto lectureTimeDto) {
        //logger.debug("Create new lecture time. Id {}", lectureTime.getId());
        lectureTimeService.save(lectureTimeDtoMapper.dtoToLectureTime(lectureTimeDto));
    }

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void update(@RequestBody LectureTimeDto lectureTimeDto, @PathVariable int id) {
        //logger.debug("Update lecture time with id {}", id);
        lectureTimeService.save(lectureTimeDtoMapper.dtoToLectureTime(lectureTimeDto));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void delete(@RequestBody LectureTimeDto lectureTimeDto) {
        //logger.debug("Delete lecture time with id {}", lectureTime.getId());
        lectureTimeService.delete(lectureTimeDtoMapper.dtoToLectureTime(lectureTimeDto));
    }
}