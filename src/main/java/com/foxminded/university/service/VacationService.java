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
import com.foxminded.university.exception.ServiceLayerException;
import com.foxminded.university.exception.VacationDurationMoreThanMaxException;
import com.foxminded.university.exception.VacationLessOneDayException;
import com.foxminded.university.exception.VacationNotCorrectDateException;
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

	public Vacation findById(int id) throws EntityNotFoundException {
		logger.debug("Find vacation by id {}", id);
		return vacationDao.findById(id).orElseThrow(
				() -> new EntityNotFoundException("Can`t find any vacation with specified id!", "Id is: " + id));
	}

	public void save(Vacation vacation) throws ServiceLayerException {
		logger.debug("Save vacation");
		isUniqueCheck(vacation);
		isDateCorrectCheck(vacation);
		isDateMoreThenOneDayCheck(vacation);
		isVacationDurationLessOrEqualsThanMaxCheck(vacation);
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

	private void isUniqueCheck(Vacation vacation) throws EntityNotUniqueException {
		logger.debug("Check vacation is unique");
		Optional<Vacation> existingVacation = vacationDao.findByPeriodAndTeacher(vacation.getStart(), vacation.getEnd(),
				vacation.getTeacher());

		if (existingVacation.isEmpty() || (existingVacation.get().getId() == vacation.getId())) {
			return;
		} else {
			throw new EntityNotUniqueException("Vacation with same start, end and teacher id is already exists!",
					"Vacation duration is: from " + vacation.getStart() + "to " + vacation.getEnd(), "Teacher name is: "
							+ vacation.getTeacher().getFirstName() + " " + vacation.getTeacher().getLastName());
		}
	}

	private void isDateCorrectCheck(Vacation vacation) throws VacationNotCorrectDateException {
		logger.debug("Check vacation start is after end");
		if (vacation.getStart().isBefore(vacation.getEnd()) || vacation.getStart().equals(vacation.getEnd())) {
			return;
		} else {
			throw new VacationNotCorrectDateException("Vacation start date can`t be after vacation end date!",
					"Vacation start is: " + vacation.getStart(), "Vacation end is: " + vacation.getEnd());
		}
	}

	private void isDateMoreThenOneDayCheck(Vacation vacation) throws VacationLessOneDayException {
		logger.debug("Check vacation duration more or equals 1 day");
		if (getVacationDaysCount(vacation) < 1) {
			throw new VacationLessOneDayException("Vacation can`t be less than 1 day!",
					"Vacation start is: " + vacation.getStart(), "Vacation end is: " + vacation.getEnd());
		}
	}

	private int getVacationDaysCount(Vacation vacation) {
		return Math.abs(Period.between(vacation.getStart(), vacation.getEnd()).getDays());
	}

	private void isVacationDurationLessOrEqualsThanMaxCheck(Vacation vacation)
			throws VacationDurationMoreThanMaxException {
		logger.debug("Check vacation duration less or equals than max");
		int teacherVacationDays = vacationDao
				.findByTeacherIdAndYear(vacation.getTeacher().getId(), vacation.getStart().getYear()).stream()
				.map(vac -> getVacationDaysCount(vac)).mapToInt(Integer::intValue).sum();

		if ((teacherVacationDays + getVacationDaysCount(vacation)) >= maxVacation
				.get(vacation.getTeacher().getDegree())) {
			throw new VacationDurationMoreThanMaxException("Vacations duration can`t be more than max!",
					"Existing teachers vacation days count: " + teacherVacationDays,
					"Vacation duration is: " + getVacationDaysCount(vacation),
					"Max vacation for degree " + vacation.getTeacher().getDegree() + "is: " + maxVacation);
		}
	}
}
