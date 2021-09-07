package com.foxminded.university.service;

import java.time.Duration;
import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

import com.foxminded.university.dao.VacationDao;
import com.foxminded.university.dao.jdbc.JdbcVacationDao;
import com.foxminded.university.model.Vacation;

@Service
public class VacationService {

	private VacationDao vacationDao;

	public VacationService(JdbcVacationDao vacationDao) {
		this.vacationDao = vacationDao;
	}

	public List<Vacation> findAll() {
		return vacationDao.findAll();
	}

	public Vacation findById(int id) {
		return vacationDao.findById(id);
	}

	public String save(Vacation vacation) {
		LocalDate start = vacation.getStart();
		LocalDate end = vacation.getEnd();
		Vacation existingVacation = vacationDao.findByPeriodAndTeacher(start, end, vacation.getTeacher());
		if (start.isAfter(end)) {
			return "Vacation can`t start after end date";
		} else if (Duration.between(start, end).toDays() < 1) {
			return "Vacation can`t be less than 1 day";
		} else if (existingVacation == null) {
			vacationDao.save(vacation);
			return "Vacation added!";
		} else if (existingVacation.getId() == vacation.getId()) {
			vacationDao.save(vacation);
			return "Vacation updated!";
		}

		return "Vacation with such teacher, start and end date already exists!";
	}

	public void deleteById(int id) {
		vacationDao.deleteById(id);
	}

	public List<Vacation> findByTeacherId(int id) {
		return vacationDao.findByTeacherId(id);
	}
}
