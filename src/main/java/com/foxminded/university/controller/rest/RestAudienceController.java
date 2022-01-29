package com.foxminded.university.controller.rest;

import com.foxminded.university.controller.AudienceController;
import com.foxminded.university.model.Audience;
import com.foxminded.university.service.AudienceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/audiences")
public class RestAudienceController {

    private static final Logger logger = LoggerFactory.getLogger(AudienceController.class);

    private final AudienceService audienceService;

    public RestAudienceController(AudienceService audienceService) {
        this.audienceService = audienceService;
    }

    @GetMapping
    public List<Audience> getAllAudiences() {
        logger.debug("Show all audiences");

        return audienceService.findAll();
    }

    @GetMapping("/{id}")
    public Audience showAudience(@PathVariable int id) {
        logger.debug("Show audience with id {}", id);

        return audienceService.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void create(@RequestBody Audience audience) {
        logger.debug("Create new audience. Id {}", audience.getId());
        audienceService.save(audience);
    }


    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void update(@RequestBody Audience audience, @PathVariable int id) {
        logger.debug("Update audience with id {}", id);
        audienceService.save(audience);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void delete(@RequestBody Audience audience) {
        logger.debug("Delete audience with id {}", audience.getId());
        audienceService.delete(audience);
    }
}