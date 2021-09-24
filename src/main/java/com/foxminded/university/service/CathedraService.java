package com.foxminded.university.service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.foxminded.university.dao.CathedraDao;
import com.foxminded.university.dao.jdbc.JdbcCathedraDao;
import com.foxminded.university.exception.EntityNotFoundException;
import com.foxminded.university.exception.EntityNotUniqueException;
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

	public Cathedra findById(int id) throws EntityNotFoundException {
		logger.debug("Find cathedra by id {}", id);
		try {
			return cathedraDao.findById(id).orElseThrow();
		} catch (NoSuchElementException e) {
			throw new EntityNotFoundException("Can`t find any cathedra", e);
		}
	}

	public void save(Cathedra cathedra) throws Exception {
		logger.debug("Save cathedra");
		if (isUnique(cathedra)) {
			cathedraDao.save(cathedra);
		}
	}

	public void deleteById(int id) {
		logger.debug("Delete cathedra by id: {}", id);
		cathedraDao.deleteById(id);
	}

	private boolean isUnique(Cathedra cathedra) throws EntityNotUniqueException {
		logger.debug("Check catheda is unique");
		Optional<Cathedra> existingCathedra = cathedraDao.findByName(cathedra.getName());
		if (existingCathedra.isEmpty() || existingCathedra.get().getId() == cathedra.getId()) {
			return true;
		} else {
			throw new EntityNotUniqueException("Cathedra with same name is already exists!");
		}
	}
}
