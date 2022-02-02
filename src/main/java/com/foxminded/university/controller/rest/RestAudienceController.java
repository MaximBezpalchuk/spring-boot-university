package com.foxminded.university.controller.rest;

import com.foxminded.university.controller.AudienceController;
import com.foxminded.university.dao.mapper.AudienceDtoMapper;
import com.foxminded.university.model.dto.AudienceDto;
import com.foxminded.university.service.AudienceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/audiences")
public class RestAudienceController {

    private static final Logger logger = LoggerFactory.getLogger(AudienceController.class);

    private final AudienceService audienceService;
    private final AudienceDtoMapper audienceDtoMapper;

    public RestAudienceController(AudienceService audienceService, AudienceDtoMapper audienceDtoMapper) {
        this.audienceService = audienceService;
        this.audienceDtoMapper = audienceDtoMapper;
    }

    @GetMapping
    public List<AudienceDto> getAllAudiences() {
        logger.debug("Show all audiences");

        return audienceService.findAll().stream().map(audienceDtoMapper::audienceToDto).collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public AudienceDto showAudience(@PathVariable int id) {
        logger.debug("Show audience with id {}", id);

        return audienceDtoMapper.audienceToDto(audienceService.findById(id));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void create(@RequestBody AudienceDto audienceDto) {
        //logger.debug("Create new audience. Id {}", audience.getId());
        audienceService.save(audienceDtoMapper.dtoToAudience(audienceDto));
    }


    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void update(@RequestBody AudienceDto audienceDto, @PathVariable int id) {
        //logger.debug("Update audience with id {}", id);
        audienceService.save(audienceDtoMapper.dtoToAudience(audienceDto));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void delete(@RequestBody AudienceDto audienceDto) {
        //logger.debug("Delete audience with id {}", audience.getId());
        audienceService.delete(audienceDtoMapper.dtoToAudience(audienceDto));
    }
}