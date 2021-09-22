package com.foxminded.university.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.foxminded.university.dao.HolidayDao;
import com.foxminded.university.dao.jdbc.JdbcHolidayDao;
import com.foxminded.university.exception.DaoException;
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

	public Holiday findById(int id) {
		logger.debug("Find holiday by id {}", id);
		try {
			return holidayDao.findById(id);
		} catch (DaoException e) {
			logger.error("Cannot find holiday with id: {}", id, e);
			return null;
		}
	}

	public void save(Holiday holiday) {
		logger.debug("Save holiday");
		if (isUnique(holiday)) {
			holidayDao.save(holiday);
		}
	}

	public void deleteById(int id) {
		logger.debug("Delete holiday by id: {}", id);
		holidayDao.deleteById(id);
	}

	private boolean isUnique(Holiday holiday) {
		logger.debug("Check holiday is unique");
		try {
			Holiday existingHoliday = holidayDao.findByNameAndDate(holiday.getName(), holiday.getDate());

			return existingHoliday == null || (existingHoliday.getId() == holiday.getId());
		} catch (DaoException e) {
			logger.error("Holiday with same name: {} and date: {} is already exists", holiday.getName(),
					holiday.getDate());
			return false;
		}
	}
}
