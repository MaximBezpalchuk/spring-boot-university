package com.foxminded.university.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.foxminded.university.dao.CathedraDao;
import com.foxminded.university.dao.jdbc.JdbcCathedraDao;
import com.foxminded.university.exception.DaoException;
import com.foxminded.university.model.Cathedra;

@Service
public class CathedraService {

	private static final Logger logger = LoggerFactory.getLogger(CathedraService.class);

	private CathedraDao cathedraDao;

	public CathedraService(JdbcCathedraDao cathedraDao) {
		this.cathedraDao = cathedraDao;
	}

	public List<Cathedra> findAll() {
		logger.debug("Find all cathedras");
		return cathedraDao.findAll();
	}

	public Cathedra findById(int id) {
		logger.debug("Find cathedra by id {}", id);
		try {
			return cathedraDao.findById(id);
		} catch (DaoException e) {
			logger.error("Cannot find cathedra with id: {}", id, e);
			return null;
		}
	}

	public void save(Cathedra cathedra) {
		logger.debug("Save cathedra");
		if (isUnique(cathedra)) {
			cathedraDao.save(cathedra);
		}
	}

	public void deleteById(int id) {
		logger.debug("Delete cathedra by id: {}", id);
		cathedraDao.deleteById(id);
	}

	private boolean isUnique(Cathedra cathedra) {
		logger.debug("Check catheda is unique");
		try {
			Cathedra existingCathedra = cathedraDao.findByName(cathedra.getName());

			return (existingCathedra == null || existingCathedra.getId() == cathedra.getId());
		} catch (DaoException e) {
			logger.error("Cathedra with same name is already exists: {}", cathedra.getName());
			return false;
		}
	}
}
