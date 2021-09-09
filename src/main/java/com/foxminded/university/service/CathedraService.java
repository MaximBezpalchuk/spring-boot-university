package com.foxminded.university.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.foxminded.university.dao.CathedraDao;
import com.foxminded.university.dao.jdbc.JdbcCathedraDao;
import com.foxminded.university.model.Cathedra;

@Service
public class CathedraService {

	private CathedraDao cathedraDao;

	public CathedraService(JdbcCathedraDao cathedraDao) {
		this.cathedraDao = cathedraDao;
	}

	public List<Cathedra> findAll() {
		return cathedraDao.findAll();
	}

	public Cathedra findById(int id) {
		return cathedraDao.findById(id);
	}

	public void save(Cathedra cathedra) {
		if (isUnique(cathedra)) {
			cathedraDao.save(cathedra);
		}
	}

	public void deleteById(int id) {
		cathedraDao.deleteById(id);
	}

	private boolean isUnique(Cathedra cathedra) {
		Cathedra existingCathedra = cathedraDao.findByName(cathedra.getName());
		return existingCathedra == null || (existingCathedra.getId() == cathedra.getId());
	}
}
