package com.foxminded.university.service;

import com.foxminded.university.dao.AudienceRepository;
import com.foxminded.university.exception.EntityNotFoundException;
import com.foxminded.university.exception.EntityNotUniqueException;
import com.foxminded.university.model.Audience;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AudienceService {

    private static final Logger logger = LoggerFactory.getLogger(AudienceService.class);

    private final AudienceRepository audienceRepository;

    public AudienceService(AudienceRepository audienceRepository) {
        this.audienceRepository = audienceRepository;
    }

    public List<Audience> findAll() {
        logger.debug("Find all audiences");
        return audienceRepository.findAll();
    }

    public Audience findById(int id) {
        logger.debug("Find audience by id {}", id);
        return audienceRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Can`t find any audience with id: " + id));
    }

    public Audience findByRoom(int room) {
        logger.debug("Find audience by room {}", room);
        return audienceRepository.findByRoom(room)
            .orElseThrow(() -> new EntityNotFoundException("Can`t find any audience with room: " + room));
    }

    public Audience save(Audience audience) {
        logger.debug("Save audience");
        uniqueCheck(audience);

        return audienceRepository.save(audience);
    }

    public void delete(int id) {
        logger.debug("Delete audience with id: {}", id);
        audienceRepository.deleteById(id);
    }

    private void uniqueCheck(Audience audience) {
        logger.debug("Check audience is unique");
        Optional<Audience> existingAudience = audienceRepository.findByRoom(audience.getRoom());
        if (existingAudience.isPresent() && (existingAudience.get().getId() != audience.getId())) {
            throw new EntityNotUniqueException(
                "Audience with room number " + audience.getRoom() + " is already exists!");
        }
    }
}
