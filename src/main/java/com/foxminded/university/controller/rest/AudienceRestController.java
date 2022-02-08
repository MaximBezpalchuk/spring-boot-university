package com.foxminded.university.controller.rest;

import com.foxminded.university.controller.AudienceController;
import com.foxminded.university.dao.mapper.AudienceMapper;
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
    public ObjectListDto getAllAudiences() {
        logger.debug("Show all audiences");

        return new ObjectListDto(audienceService.findAll().stream().map(audienceMapper::audienceToDto).collect(Collectors.toList()));
    }

    @GetMapping("/{id}")
    public AudienceDto showAudience(@PathVariable int id) {
        logger.debug("Show audience with id {}", id);

        return audienceMapper.audienceToDto(audienceService.findById(id));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity create(@RequestBody AudienceDto audienceDto) {
        Audience audience = audienceService.save(audienceMapper.dtoToAudience(audienceDto));
        logger.debug("Create new audience. Id {}", audience.getId());
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
            .buildAndExpand(audience.getId()).toUri();

        return ResponseEntity.created(location).build();
    }

    @PatchMapping("/{id}")
    public void update(@RequestBody AudienceDto audienceDto) {
        audienceService.save(audienceMapper.dtoToAudience(audienceDto));
    }

    @DeleteMapping("/{id}")
    public void delete(@RequestBody AudienceDto audienceDto) {
        audienceService.delete(audienceMapper.dtoToAudience(audienceDto));
    }
}