package com.foxminded.university.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.foxminded.university.dao.AudienceDao;
import com.foxminded.university.dao.jdbc.JdbcAudienceDao;
import com.foxminded.university.exception.DaoException;
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

	public Audience findById(int id) {
		logger.debug("Find audience by id {}", id);
		try {
			return audienceDao.findById(id);
		} catch (DaoException e) {
			logger.error("Cannot find audience with id: {}", id, e);
			return null;
		}

	}

	public void save(Audience audience) {
		logger.debug("Save audience");
		if (isUnique(audience)) {
			audienceDao.save(audience);
		}
	}

	public void deleteById(int id) {
		logger.debug("Delete audience by id: {}", id);
		audienceDao.deleteById(id);
	}

	private boolean isUnique(Audience audience) {
		logger.debug("Check audience is unique");
		try {
			Audience existingAudience = audienceDao.findByRoom(audience.getRoom());

			return existingAudience == null || (existingAudience.getId() == audience.getId());
		} catch (DaoException e) {
			logger.error("Audience with same room is already exists: {}", audience.getRoom());
			return false;
		}
	}
}
