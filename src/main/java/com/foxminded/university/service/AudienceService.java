package com.foxminded.university.service;

import com.foxminded.university.dao.AudienceDao;
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

    private AudienceDao audienceDao;

    public AudienceService(AudienceDao audienceDao) {
        this.audienceDao = audienceDao;
    }

    public List<Audience> findAll() {
        logger.debug("Find all audiences");
        return audienceDao.findAll();
    }

    public Audience findById(int id) {
        logger.debug("Find audience by id {}", id);
        return audienceDao.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Can`t find any audience with id: " + id));
    }

    public void save(Audience audience) {
        logger.debug("Save audience");
        uniqueCheck(audience);
        audienceDao.save(audience);
    }

    public void delete(Audience audience) {
        logger.debug("Delete audience with id: {}", audience.getId());
        audienceDao.delete(audience);
    }

    private void uniqueCheck(Audience audience) {
        logger.debug("Check audience is unique");
        Optional<Audience> existingAudience = audienceDao.findByRoom(audience.getRoom());
        if (existingAudience.isPresent() && (existingAudience.get().getId() != audience.getId())) {
            throw new EntityNotUniqueException(
                    "Audience with room number " + audience.getRoom() + " is already exists!");
        }
    }
}
