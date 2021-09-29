package com.foxminded.university.service;

import java.time.Period;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.foxminded.university.dao.VacationDao;
import com.foxminded.university.dao.jdbc.JdbcVacationDao;
import com.foxminded.university.exception.EntityNotFoundException;
import com.foxminded.university.exception.EntityNotUniqueException;
import com.foxminded.university.exception.VacationDurationMoreThanMaxException;
import com.foxminded.university.exception.VacationLessOneDayException;
import com.foxminded.university.exception.DateDurationException;
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
		return vacationDao.findById(id)
				.orElseThrow(() -> new EntityNotFoundException("Can`t find any vacation with id: " + id));
	}

	public void save(Vacation vacation) {
		logger.debug("Save vacation");
		uniqueCheck(vacation);
		dateCorrectCheck(vacation);
		dateMoreThenOneDayCheck(vacation);
		vacationDurationLessOrEqualsThanMaxCheck(vacation);
		vacationDao.save(vacation);
	}

	public void deleteById(int id) {
		logger.debug("Delete vacation by id: {}", id);
		vacationDao.deleteById(id);
	}

	public List<Vacation> findByTeacherId(int id) {
		logger.debug("Find vacation by teacher id: {}", id);
		return vacationDao.findByTeacherId(id);
	}

	private void uniqueCheck(Vacation vacation) {
		logger.debug("Check vacation is unique");
		Optional<Vacation> existingVacation = vacationDao.findByPeriodAndTeacher(vacation.getStart(), vacation.getEnd(),
				vacation.getTeacher());

		if (!existingVacation.isEmpty() && (existingVacation.get().getId() != vacation.getId())) {
			throw new EntityNotUniqueException("Vacation with start(" + vacation.getStart() + "), end("
					+ vacation.getEnd() + ") and teacher(" + vacation.getTeacher().getFirstName() + " "
					+ vacation.getTeacher().getLastName() + ") id is already exists!");
		}
	}

	private void dateCorrectCheck(Vacation vacation) {
		logger.debug("Check vacation start is after end");
		if (!vacation.getStart().isBefore(vacation.getEnd()) && !vacation.getStart().equals(vacation.getEnd())) {
			throw new DateDurationException(
					"Vacation start date can`t be after vacation end date! Vacation start is: " + vacation.getStart()
							+ ". Vacation end is: " + vacation.getEnd());
		}
	}

	private void dateMoreThenOneDayCheck(Vacation vacation) {
		logger.debug("Check vacation duration more or equals 1 day");
		if (getVacationDaysCount(vacation) < 1) {
			throw new VacationLessOneDayException("Vacation can`t be less than 1 day! Vacation start is: "
					+ vacation.getStart() + ". Vacation end is: " + vacation.getEnd());
		}
	}

	private int getVacationDaysCount(Vacation vacation) {
		return Math.abs(Period.between(vacation.getStart(), vacation.getEnd()).getDays());
	}

	private void vacationDurationLessOrEqualsThanMaxCheck(Vacation vacation) {
		logger.debug("Check vacation duration less or equals than max");
		int teacherVacationDays = vacationDao
				.findByTeacherIdAndYear(vacation.getTeacher().getId(), vacation.getStart().getYear()).stream()
				.map(vac -> getVacationDaysCount(vac)).mapToInt(Integer::intValue).sum();

		if ((teacherVacationDays + getVacationDaysCount(vacation)) >= maxVacation
				.get(vacation.getTeacher().getDegree())) {
			throw new VacationDurationMoreThanMaxException("Vacations duration(existing " + teacherVacationDays
					+ " plus appointed " + getVacationDaysCount(vacation) + ") can`t be more than max("
					+ maxVacation.get(vacation.getTeacher().getDegree()) + ") for degree "
					+ vacation.getTeacher().getDegree() + "!");
		}
	}
}
