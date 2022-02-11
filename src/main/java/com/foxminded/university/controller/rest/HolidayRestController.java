package com.foxminded.university.controller.rest;

import com.foxminded.university.controller.HolidayController;
import com.foxminded.university.dao.mapper.HolidayMapper;
import com.foxminded.university.dto.HolidayDto;
import com.foxminded.university.model.Holiday;
import com.foxminded.university.service.HolidayService;
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
@RequestMapping("api/holidays")
public class HolidayRestController {

    private static final Logger logger = LoggerFactory.getLogger(HolidayController.class);

    private final HolidayService holidayService;
    private final HolidayMapper holidayMapper;

    public HolidayRestController(HolidayService holidayService, HolidayMapper holidayMapper) {
        this.holidayService = holidayService;
        this.holidayMapper = holidayMapper;
    }

    @GetMapping
    public Page<HolidayDto> getAllHolidays(Pageable pageable) {
        logger.debug("Show all holidays");

        return holidayService.findAll(pageable).map(holidayMapper::holidayToDto);
    }

    @GetMapping("/{id}")
    public HolidayDto showHoliday(@PathVariable int id) {
        logger.debug("Show holiday with id {}", id);

        return holidayMapper.holidayToDto(holidayService.findById(id));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity create(@RequestBody HolidayDto holidayDto) {
        Holiday holiday = holidayService.save(holidayMapper.dtoToHoliday(holidayDto));
        logger.debug("Create new holiday. Id {}", holiday.getId());
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
            .buildAndExpand(holiday.getId()).toUri();

        return ResponseEntity.created(location).build();
    }

    @PatchMapping("/{id}")
    public void update(@RequestBody HolidayDto holidayDto) {
        holidayService.save(holidayMapper.dtoToHoliday(holidayDto));
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable int id) {
        holidayService.delete(id);
    }
}