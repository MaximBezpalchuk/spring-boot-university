package com.foxminded.university.service;

import java.time.Period;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.foxminded.university.dao.VacationDao;
import com.foxminded.university.dao.jdbc.JdbcVacationDao;
import com.foxminded.university.exception.DaoException;
import com.foxminded.university.model.Degree;
import com.foxminded.university.model.Vacation;

@Service
public class VacationService {

	private static final Logger logger = LoggerFactory.getLogger(VacationService.class);

	private VacationDao vacationDao;
	@Value("#{${maxVacation}}")
	private Map<Degree, Integer> maxVacation;

	public VacationService(JdbcVacationDao vacationDao) {
		this.vacationDao = vacationDao;
	}

	public List<Vacation> findAll() {
		logger.debug("Find all vacations");
		return vacationDao.findAll();
	}

	public Vacation findById(int id) {
		logger.debug("Find vacation by id {}", id);
		try {
			return vacationDao.findById(id);
		} catch (DaoException e) {
			logger.error("Cannot find vacation with id: {}", id, e);
			return null;
		}
	}

	public void save(Vacation vacation) {
		logger.debug("Save vacation");
		if (isDateCorrect(vacation) && isDateMoreThenOneDay(vacation) && isVacationDurationLessOrEqualsThanMax(vacation)
				&& isUnique(vacation)) {
			vacationDao.save(vacation);
		}
	}

	public void deleteById(int id) {
		logger.debug("Delete vacation by id: {}", id);
		vacationDao.deleteById(id);
	}

	public List<Vacation> findByTeacherId(int id) {
		logger.debug("Find vacation by teacher id: {}", id);
		return vacationDao.findByTeacherId(id);
	}

	private boolean isUnique(Vacation vacation) {
		logger.debug("Check vacation is unique");
		try {
			Vacation existingVacation = vacationDao.findByPeriodAndTeacher(vacation.getStart(), vacation.getEnd(),
					vacation.getTeacher());

			return existingVacation == null || (existingVacation.getId() == vacation.getId());
		} catch (DaoException e) {
			logger.error("Vacation with same start: {}, end: {} and teacher id: {} is already exists",
					vacation.getStart(), vacation.getEnd(), vacation.getTeacher().getId());
			return false;
		}
	}

	private boolean isDateCorrect(Vacation vacation) {
		logger.debug("Check vacation start is after end");
		return vacation.getStart().isBefore(vacation.getEnd()) || vacation.getStart().equals(vacation.getEnd());
	}

	private boolean isDateMoreThenOneDay(Vacation vacation) {
		logger.debug("Check vacation duration more or equals 1 day");
		return getVacationDaysCount(vacation) >= 1;
	}

	private int getVacationDaysCount(Vacation vacation) {
		return Math.abs(Period.between(vacation.getStart(), vacation.getEnd()).getDays());
	}

	private boolean isVacationDurationLessOrEqualsThanMax(Vacation vacation) {
		logger.debug("Check vacation duration less or equals than max");
		int teacherVacationDays = vacationDao
				.findByTeacherIdAndYear(vacation.getTeacher().getId(), vacation.getStart().getYear()).stream()
				.map(vac -> getVacationDaysCount(vac)).mapToInt(Integer::intValue).sum();

		return (teacherVacationDays + getVacationDaysCount(vacation)) <= maxVacation
				.get(vacation.getTeacher().getDegree());
	}
}
