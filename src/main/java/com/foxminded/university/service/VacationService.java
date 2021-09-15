package com.foxminded.university.service;

import java.time.Period;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.foxminded.university.dao.VacationDao;
import com.foxminded.university.dao.jdbc.JdbcVacationDao;
import com.foxminded.university.model.Degree;
import com.foxminded.university.model.Vacation;

@Service
public class VacationService {

	private VacationDao vacationDao;
	@Value("#{${maxVacation}}")
	private Map<Degree, Integer> maxVacation;

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
		if (isDateCorrect(vacation) && isDateMoreThenOneDay(vacation) && isVacationDurationLessOrEqualsThanMax(vacation)
				&& isUnique(vacation)) {
			vacationDao.save(vacation);
		}
	}

	public void deleteById(int id) {
		vacationDao.deleteById(id);
	}

	public List<Vacation> findByTeacherId(int id) {
		return vacationDao.findByTeacherId(id);
	}

	private boolean isUnique(Vacation vacation) {
		Vacation existingVacation = vacationDao.findByPeriodAndTeacher(vacation.getStart(), vacation.getEnd(),
				vacation.getTeacher());

		return existingVacation == null || (existingVacation.getId() == vacation.getId());
	}

	private boolean isDateCorrect(Vacation vacation) {
		return vacation.getStart().isBefore(vacation.getEnd()) || vacation.getStart().equals(vacation.getEnd());
	}

	private boolean isDateMoreThenOneDay(Vacation vacation) {
		return getVacationDaysCount(vacation) >= 1;
	}

	private int getVacationDaysCount(Vacation vacation) {
		return Math.abs(Period.between(vacation.getStart(), vacation.getEnd()).getDays());
	}

	private boolean isVacationDurationLessOrEqualsThanMax(Vacation vacation) {
		int teacherVacationDays = vacationDao.findByTeacherId(vacation.getTeacher().getId()).stream()
				.map(vac -> getVacationDaysCount(vac)).mapToInt(Integer::intValue).sum();

		return (teacherVacationDays + getVacationDaysCount(vacation)) <= maxVacation
				.get(vacation.getTeacher().getDegree());
	}
}
