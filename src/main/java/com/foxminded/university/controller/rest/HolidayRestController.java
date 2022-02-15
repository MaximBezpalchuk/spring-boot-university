package com.foxminded.university.controller.rest;

import com.foxminded.university.controller.HolidayController;
import com.foxminded.university.dao.mapper.HolidayMapper;
import com.foxminded.university.dto.AudienceDto;
import com.foxminded.university.dto.HolidayDto;
import com.foxminded.university.dto.Slice;
import com.foxminded.university.model.Holiday;
import com.foxminded.university.service.HolidayService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
    @Operation(summary = "Get all holidays")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Show all holidays"),
        @ApiResponse(responseCode = "404", description = "Holidays not found", content = @Content),
        @ApiResponse(responseCode = "500", description = "Internal error", content = @Content)})
    public Page<HolidayDto> getAllHolidays(Pageable pageable) {
        logger.debug("Show all holidays");

        return holidayService.findAll(pageable).map(holidayMapper::holidayToDto);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get an holiday by its id")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Found the holiday",
            content = { @Content(mediaType = "application/json", schema = @Schema(implementation = HolidayDto.class)) }),
        @ApiResponse(responseCode = "400", description = "Invalid id supplied", content = @Content),
        @ApiResponse(responseCode = "404", description = "Holiday not found", content = @Content),
        @ApiResponse(responseCode = "500", description = "Internal error", content = @Content)})
    public HolidayDto showHoliday(@Parameter(description = "Id of holiday to be searched") @PathVariable int id) {
        logger.debug("Show holiday with id {}", id);

        return holidayMapper.holidayToDto(holidayService.findById(id));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create a new holiday by its DTO")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Holiday successfully created",
            content = { @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseEntity.class)) }),
        @ApiResponse(responseCode = "500", description = "Internal error", content = @Content)})
    public ResponseEntity create(@RequestBody HolidayDto holidayDto) {
        Holiday holiday = holidayService.save(holidayMapper.dtoToHoliday(holidayDto));
        logger.debug("Create new holiday. Id {}", holiday.getId());
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
            .buildAndExpand(holiday.getId()).toUri();

        return ResponseEntity.created(location).build();
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Update an existing holiday by its DTO")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Holiday successfully updated",content = @Content),
        @ApiResponse(responseCode = "400", description = "Invalid id supplied", content = @Content),
        @ApiResponse(responseCode = "404", description = "Holiday not found", content = @Content),
        @ApiResponse(responseCode = "500", description = "Internal error", content = @Content)})
    public void update(@RequestBody HolidayDto holidayDto) {
        holidayService.save(holidayMapper.dtoToHoliday(holidayDto));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete an existing holiday by its id")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Holiday successfully deleted", content = @Content),
        @ApiResponse(responseCode = "400", description = "Invalid id supplied", content = @Content),
        @ApiResponse(responseCode = "404", description = "Holiday not found", content = @Content),
        @ApiResponse(responseCode = "500", description = "Internal error", content = @Content)})
    public void delete(@Parameter(description = "Id of holiday to be deleted") @PathVariable int id) {
        holidayService.delete(id);
    }
}