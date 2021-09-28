package com.foxminded.university.service;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.foxminded.university.dao.HolidayDao;
import com.foxminded.university.dao.jdbc.JdbcHolidayDao;
import com.foxminded.university.exception.EntityNotFoundException;
import com.foxminded.university.exception.EntityNotUniqueException;
import com.foxminded.university.exception.ServiceLayerException;
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
		return holidayDao.findById(id).orElseThrow(
				() -> new EntityNotFoundException("Can`t find any holiday with specified id!", "Id is: " + id));
	}

	public void save(Holiday holiday) throws ServiceLayerException {
		logger.debug("Save holiday");
		uniqueCheck(holiday);
		holidayDao.save(holiday);
	}

	public void deleteById(int id) {
		logger.debug("Delete holiday by id: {}", id);
		holidayDao.deleteById(id);
	}

	private void uniqueCheck(Holiday holiday) throws EntityNotUniqueException {
		logger.debug("Check holiday is unique");
		Optional<Holiday> existingHoliday = holidayDao.findByNameAndDate(holiday.getName(), holiday.getDate());

		if (!existingHoliday.isEmpty() && (existingHoliday.get().getId() != holiday.getId())) {
			throw new EntityNotUniqueException("Holiday with same name and date is already exists!",
					"Holiday name is: " + holiday.getName(), "Holiday date is: " + holiday.getDate());
		}
	}
}
