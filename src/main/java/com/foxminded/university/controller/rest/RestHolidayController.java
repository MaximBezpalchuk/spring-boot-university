package com.foxminded.university.controller.rest;

import com.foxminded.university.controller.HolidayController;
import com.foxminded.university.dao.mapper.HolidayDtoMapper;
import com.foxminded.university.model.dto.HolidayDto;
import com.foxminded.university.service.HolidayService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/holidays")
public class RestHolidayController {
    private static final Logger logger = LoggerFactory.getLogger(HolidayController.class);

    private final HolidayService holidayService;
    private final HolidayDtoMapper holidayDtoMapper;

    public RestHolidayController(HolidayService holidayService, HolidayDtoMapper holidayDtoMapper) {
        this.holidayService = holidayService;
        this.holidayDtoMapper = holidayDtoMapper;
    }

    @GetMapping
    public Page<HolidayDto> getAllHolidays(Pageable pageable) {
        logger.debug("Show all holidays");

        return holidayService.findAll(pageable).map(holidayDtoMapper::holidayToDto);
    }

    @GetMapping("/{id}")
    public HolidayDto showHoliday(@PathVariable int id) {
        logger.debug("Show holiday with id {}", id);

        return holidayDtoMapper.holidayToDto(holidayService.findById(id));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void create(@RequestBody HolidayDto holidayDto) {
        //logger.debug("Create new holiday. Id {}", holiday.getId());
        holidayService.save(holidayDtoMapper.dtoToHoliday(holidayDto));
    }

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void update(@RequestBody HolidayDto holidayDto, @PathVariable int id) {
        //logger.debug("Update holiday with id {}", id);
        holidayService.save(holidayDtoMapper.dtoToHoliday(holidayDto));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void delete(@RequestBody HolidayDto holidayDto) {
        //logger.debug("Delete holiday with id {}", holiday.getId());
        holidayService.delete(holidayDtoMapper.dtoToHoliday(holidayDto));
    }
}