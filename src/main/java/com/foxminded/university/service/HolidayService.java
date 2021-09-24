package com.foxminded.university.service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.foxminded.university.dao.HolidayDao;
import com.foxminded.university.dao.jdbc.JdbcHolidayDao;
import com.foxminded.university.exception.EntityNotFoundException;
import com.foxminded.university.exception.EntityNotUniqueException;
import com.foxminded.university.model.Holiday;

@Service
public class HolidayService {

	private static final Logger logger = LoggerFactory.getLogger(HolidayService.class);

	private HolidayDao holidayDao;

	public HolidayService(JdbcHolidayDao holidayDao) {
		this.holidayDao = holidayDao;
	}

	public List<Holiday> findAll() {
		logger.debug("Find all holidays");
		return holidayDao.findAll();
	}

	public Holiday findById(int id) throws EntityNotFoundException {
		logger.debug("Find holiday by id {}", id);
		try {
			return holidayDao.findById(id).orElseThrow();
		} catch (NoSuchElementException e) {
			throw new EntityNotFoundException("Can`t find any holiday", e);
		}
	}

	public void save(Holiday holiday) throws Exception {
		logger.debug("Save holiday");
		if (isUnique(holiday)) {
			holidayDao.save(holiday);
		}
	}

	public void deleteById(int id) {
		logger.debug("Delete holiday by id: {}", id);
		holidayDao.deleteById(id);
	}

	private boolean isUnique(Holiday holiday) throws EntityNotUniqueException {
		logger.debug("Check holiday is unique");
		Optional<Holiday> existingHoliday = holidayDao.findByNameAndDate(holiday.getName(), holiday.getDate());

		if (existingHoliday.isEmpty() || (existingHoliday.get().getId() == holiday.getId())) {
			return true;
		} else {
			throw new EntityNotUniqueException("Holiday with same name and date is already exists!");
		}
	}
}
