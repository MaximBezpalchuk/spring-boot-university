package com.foxminded.university.controller.rest;

import com.foxminded.university.controller.HolidayController;
import com.foxminded.university.model.Holiday;
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

    public RestHolidayController(HolidayService holidayService) {
        this.holidayService = holidayService;
    }

    @GetMapping
    public Page<Holiday> getAllHolidays(Pageable pageable) {
        logger.debug("Show all holidays");

        return holidayService.findAll(pageable);
    }

    @GetMapping("/{id}")
    public Holiday showHoliday(@PathVariable int id) {
        logger.debug("Show holiday with id {}", id);

        return holidayService.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void create(@RequestBody Holiday holiday) {
        logger.debug("Create new holiday. Id {}", holiday.getId());
        holidayService.save(holiday);
    }

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void update(@RequestBody Holiday holiday, @PathVariable int id) {
        logger.debug("Update holiday with id {}", id);
        holidayService.save(holiday);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void delete(@RequestBody Holiday holiday) {
        logger.debug("Delete holiday with id {}", holiday.getId());
        holidayService.delete(holiday);
    }
}