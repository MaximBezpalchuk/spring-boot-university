package com.foxminded.university.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.foxminded.university.dao.jdbc.JdbcVacationDao;
import com.foxminded.university.model.Vacation;

@Service
public class VacationService {

	private JdbcVacationDao vacationDao;

	public VacationService(JdbcVacationDao vacationDao) {
		this.vacationDao = vacationDao;
	}

	public List<Vacation> findAll() {
		return vacationDao.findAll();
	}

	public Vacation findById(int id) {
		return vacationDao.findById(id);
	}

	public void save(Vacation vacation) {
		vacationDao.save(vacation);
	}

	public void deleteById(int id) {
		vacationDao.deleteById(id);
	}

	public List<Vacation> findByTeacherId(int id) {
		return vacationDao.findByTeacherId(id);
	}
}
