package com.foxminded.university.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.foxminded.university.dao.AudienceDao;
import com.foxminded.university.dao.jdbc.JdbcAudienceDao;
import com.foxminded.university.model.Audience;

@Service
public class AudienceService {

	private AudienceDao audienceDao;

	public AudienceService(JdbcAudienceDao audienceDao) {
		this.audienceDao = audienceDao;
	}

	public List<Audience> findAll() {
		return audienceDao.findAll();
	}

	public Audience findById(int id) {
		return audienceDao.findById(id);
	}

	public String save(Audience audience) {
		Audience existingAudience = audienceDao.findByRoom(audience.getRoom());
		if (existingAudience == null) {
			audienceDao.save(audience);
			return "Audience added!";
		} else if (existingAudience.getId() == audience.getId()) {
			audienceDao.save(audience);
			return "Audience updated!";
		}

		return "Unusual error";
	}

	public void deleteById(int id) {
		audienceDao.deleteById(id);
	}
}
