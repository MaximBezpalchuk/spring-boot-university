package com.foxminded.university.controller.rest;

import com.foxminded.university.controller.AudienceController;
import com.foxminded.university.dao.mapper.AudienceDtoMapper;
import com.foxminded.university.dto.AudienceDto;
import com.foxminded.university.dto.ObjectListDto;
import com.foxminded.university.model.Audience;
import com.foxminded.university.service.AudienceService;
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
@RequestMapping("api/audiences")
public class AudienceRestController {

    private static final Logger logger = LoggerFactory.getLogger(AudienceController.class);

    private final AudienceService audienceService;
    private final AudienceDtoMapper audienceDtoMapper;

    public AudienceRestController(AudienceService audienceService, AudienceDtoMapper audienceDtoMapper) {
        this.audienceService = audienceService;
        this.audienceDtoMapper = audienceDtoMapper;
    }

    @GetMapping
    public ObjectListDto getAllAudiences() {
        logger.debug("Show all audiences");

        return new ObjectListDto(audienceService.findAll().stream().map(audienceDtoMapper::audienceToDto).collect(Collectors.toList()));
    }

    @GetMapping("/{id}")
    public AudienceDto showAudience(@PathVariable int id) {
        logger.debug("Show audience with id {}", id);

        return audienceDtoMapper.audienceToDto(audienceService.findById(id));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity create(@RequestBody AudienceDto audienceDto) {
        Audience audience = audienceService.save(audienceDtoMapper.dtoToAudience(audienceDto));
        logger.debug("Create new audience. Id {}", audience.getId());
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
            .buildAndExpand(audience.getId()).toUri();

        return ResponseEntity.created(location).build();
    }

    @PatchMapping("/{id}")
    public void update(@RequestBody AudienceDto audienceDto) {
        audienceService.save(audienceDtoMapper.dtoToAudience(audienceDto));
    }

    @DeleteMapping("/{id}")
    public void delete(@RequestBody AudienceDto audienceDto) {
        audienceService.delete(audienceDtoMapper.dtoToAudience(audienceDto));
    }
}