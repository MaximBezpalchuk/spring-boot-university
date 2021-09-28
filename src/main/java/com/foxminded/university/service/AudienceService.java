package com.foxminded.university.service;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.foxminded.university.dao.AudienceDao;
import com.foxminded.university.dao.jdbc.JdbcAudienceDao;
import com.foxminded.university.exception.EntityNotFoundException;
import com.foxminded.university.exception.EntityNotUniqueException;
import com.foxminded.university.exception.ServiceLayerException;
import com.foxminded.university.model.Audience;

@Service
public class AudienceService {

	private static final Logger logger = LoggerFactory.getLogger(AudienceService.class);

	private AudienceDao audienceDao;

	public AudienceService(JdbcAudienceDao audienceDao) {
		this.audienceDao = audienceDao;
	}

	public List<Audience> findAll() {
		logger.debug("Find all audiences");
		return audienceDao.findAll();
	}

	public Audience findById(int id) throws EntityNotFoundException {
		logger.debug("Find audience by id {}", id);
		return audienceDao.findById(id).orElseThrow(
				() -> new EntityNotFoundException("Can`t find any audience with specified id!", "Id is: " + id));
	}

	public void save(Audience audience) throws ServiceLayerException {
		logger.debug("Save audience");
		uniqueCheck(audience);
		audienceDao.save(audience);
	}

	public void deleteById(int id) {
		logger.debug("Delete audience by id: {}", id);
		audienceDao.deleteById(id);
	}

	private void uniqueCheck(Audience audience) throws EntityNotUniqueException {
		logger.debug("Check audience is unique");
		Optional<Audience> existingAudience = audienceDao.findByRoom(audience.getRoom());
		if (!existingAudience.isEmpty() && (existingAudience.get().getId() != audience.getId())) {
			throw new EntityNotUniqueException("Audience with this room number is already exists!",
					"Room number is: " + audience.getRoom());
		}
	}
}
