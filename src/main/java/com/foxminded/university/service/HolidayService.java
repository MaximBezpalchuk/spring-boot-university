package com.foxminded.university.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.foxminded.university.dao.HolidayDao;
import com.foxminded.university.dao.jdbc.JdbcHolidayDao;
import com.foxminded.university.model.Holiday;

@Service
public class HolidayService {

	private HolidayDao holidayDao;

	public HolidayService(JdbcHolidayDao holidayDao) {
		this.holidayDao = holidayDao;
	}

	public List<Holiday> findAll() {
		return holidayDao.findAll();
	}

	public Holiday findById(int id) {
		return holidayDao.findById(id);
	}

	public void save(Holiday holiday) {
		if (isUnique(holiday)) {
			holidayDao.save(holiday);
		}
	}

	public void deleteById(int id) {
		holidayDao.deleteById(id);
	}

	private boolean isUnique(Holiday holiday) {
		Holiday existingHoliday = holidayDao.findByName(holiday.getName());
		return existingHoliday == null || (existingHoliday.getId() == holiday.getId());
	}
}
