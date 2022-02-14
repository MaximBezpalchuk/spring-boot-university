package com.foxminded.university.controller.rest;

import com.foxminded.university.controller.AudienceController;
import com.foxminded.university.dao.mapper.AudienceMapper;
import com.foxminded.university.dto.AudienceDto;
import com.foxminded.university.dto.GroupDto;
import com.foxminded.university.dto.Slice;
import com.foxminded.university.model.Audience;
import com.foxminded.university.service.AudienceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/audiences")
public class AudienceRestController {

    private static final Logger logger = LoggerFactory.getLogger(AudienceController.class);

    private final AudienceService audienceService;
    private final AudienceMapper audienceMapper;

    public AudienceRestController(AudienceService audienceService, AudienceMapper audienceMapper) {
        this.audienceService = audienceService;
        this.audienceMapper = audienceMapper;
    }

    @GetMapping
    @Operation(summary = "Get all audiences")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Show all audiences",
            content = { @Content(mediaType = "application/json", schema = @Schema(implementation = Slice.class)) }),
        @ApiResponse(responseCode = "404", description = "Audiences not found", content = @Content),
        @ApiResponse(responseCode = "500", description = "Internal error", content = @Content)})
    public Slice getAllAudiences() {
        logger.debug("Show all audiences");

        return new Slice(audienceService.findAll().stream().map(audienceMapper::audienceToDto).collect(Collectors.toList()));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get an audience by its id")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Found the audience",
            content = { @Content(mediaType = "application/json", schema = @Schema(implementation = AudienceDto.class)) }),
        @ApiResponse(responseCode = "400", description = "Invalid id supplied", content = @Content),
        @ApiResponse(responseCode = "404", description = "Audience not found", content = @Content),
        @ApiResponse(responseCode = "500", description = "Internal error", content = @Content)})
    public AudienceDto showAudience(@Parameter(description = "Id of audience to be searched") @PathVariable int id) {
        logger.debug("Show audience with id {}", id);

        return audienceMapper.audienceToDto(audienceService.findById(id));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create a new audience by its DTO")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Audience successfully created",
            content = { @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseEntity.class)) }),
        @ApiResponse(responseCode = "500", description = "Internal error", content = @Content)})
    public ResponseEntity create(@RequestBody AudienceDto audienceDto) {
        Audience audience = audienceService.save(audienceMapper.dtoToAudience(audienceDto));
        logger.debug("Create new audience. Id {}", audience.getId());
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
            .buildAndExpand(audience.getId()).toUri();

        return ResponseEntity.created(location).build();
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Update an existing audience by its DTO")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Audience successfully updated",content = @Content),
        @ApiResponse(responseCode = "400", description = "Invalid id supplied", content = @Content),
        @ApiResponse(responseCode = "404", description = "Audience not found", content = @Content),
        @ApiResponse(responseCode = "500", description = "Internal error", content = @Content)})
    public void update(@RequestBody AudienceDto audienceDto) {
        audienceService.save(audienceMapper.dtoToAudience(audienceDto));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete an existing audience by its id")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Audience successfully deleted", content = @Content),
        @ApiResponse(responseCode = "400", description = "Invalid id supplied", content = @Content),
        @ApiResponse(responseCode = "404", description = "Audience not found", content = @Content),
        @ApiResponse(responseCode = "500", description = "Internal error", content = @Content)})
    public void delete(@Parameter(description = "Id of audience to be deleted") @PathVariable int id) {
        audienceService.delete(id);
    }
}