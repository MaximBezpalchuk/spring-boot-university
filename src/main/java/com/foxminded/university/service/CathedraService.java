package com.foxminded.university.service;

import java.util.List;
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
		return cathedraDao.findById(id).orElseThrow(() -> new EntityNotFoundException("Can`t find any cathedra"));
	}

	public void save(Cathedra cathedra) throws Exception {
		logger.debug("Save cathedra");
		isUniqueCheck(cathedra);
		cathedraDao.save(cathedra);
	}

	public void deleteById(int id) {
		logger.debug("Delete cathedra by id: {}", id);
		cathedraDao.deleteById(id);
	}

	private void isUniqueCheck(Cathedra cathedra) throws EntityNotUniqueException {
		logger.debug("Check catheda is unique");
		Optional<Cathedra> existingCathedra = cathedraDao.findByName(cathedra.getName());
		if (existingCathedra.isEmpty() || existingCathedra.get().getId() == cathedra.getId()) {
			return;
		} else {
			throw new EntityNotUniqueException("Cathedra with same name is already exists!");
		}
	}
}
