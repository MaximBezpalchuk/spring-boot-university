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

	public String save(Cathedra cathedra) {
		Cathedra existingCathedra = cathedraDao.findByName(cathedra.getName());
		if (existingCathedra == null) {
			cathedraDao.save(cathedra);
			return "Cathedra added!";
		} else if (existingCathedra.getId() == cathedra.getId()) {
			cathedraDao.save(cathedra);
			return "Cathedra updated!";
		}

		return "Unusual error";
	}

	public void deleteById(int id) {
		cathedraDao.deleteById(id);
	}
}
