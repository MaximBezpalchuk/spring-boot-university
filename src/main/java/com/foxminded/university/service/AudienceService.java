package com.foxminded.university.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.foxminded.university.dao.AudienceDao;
import com.foxminded.university.model.Audience;

@Service
public class AudienceService {

	private AudienceDao audienceDao;

	public List<Audience> findAll() {
		return audienceDao.findAll();
	}

	public Audience findById(int id) {
		return audienceDao.findById(id);
	}

	public void save(Audience audience) {
		audienceDao.save(audience);
	}

	public void deleteById(int id) {
		audienceDao.deleteById(id);
	}
}
