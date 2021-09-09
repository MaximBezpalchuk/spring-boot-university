package com.foxminded.university.service;

import java.time.LocalDate;
import java.time.Period;
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

	public void save(Vacation vacation) {
		LocalDate start = vacation.getStart();
		LocalDate end = vacation.getEnd();
		Vacation existingVacation = vacationDao.findByPeriodAndTeacher(start, end, vacation.getTeacher());
		if (isDateCorrect(vacation) && isDateMoreThenOneDay(vacation) && isUnique(vacation, existingVacation)) {
			vacationDao.save(vacation);
		}
	}

	public void deleteById(int id) {
		vacationDao.deleteById(id);
	}

	public List<Vacation> findByTeacherId(int id) {
		return vacationDao.findByTeacherId(id);
	}

	private boolean isUnique(Vacation vacation, Vacation existingVacation) {
		return existingVacation == null || (existingVacation.getId() == vacation.getId());
	}

	private boolean isDateCorrect(Vacation vacation) {
		return vacation.getStart().isBefore(vacation.getEnd());
	}

	private boolean isDateMoreThenOneDay(Vacation vacation) {
		return Math.abs(Period.between(vacation.getStart(), vacation.getEnd()).getDays()) > 1;
	}
}
