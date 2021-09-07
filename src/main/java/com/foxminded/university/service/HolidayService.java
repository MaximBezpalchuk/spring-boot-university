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

	public String save(Holiday holiday) {
		Holiday existingGroup = holidayDao.findByName(holiday.getName());
		if (existingGroup == null) {
			holidayDao.save(holiday);
			return "Holiday added!";
		} else if (existingGroup.getId() == holiday.getId()) {
			holidayDao.save(holiday);
			return "Holiday updated!";
		}

		return "Holiday with such name already exists!";
	}

	public void deleteById(int id) {
		holidayDao.deleteById(id);
	}
}
