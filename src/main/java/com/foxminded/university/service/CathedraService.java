package com.foxminded.university.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.foxminded.university.dao.jdbc.JdbcCathedraDao;
import com.foxminded.university.model.Cathedra;

@Service
public class CathedraService {

	private JdbcCathedraDao cathedraDao;

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
		cathedraDao.save(cathedra);
	}

	public void deleteById(int id) {
		cathedraDao.deleteById(id);
		;
	}
}
