package com.foxminded.university.dao;

import java.time.LocalDate;

import com.foxminded.university.model.Holiday;

public interface HolidayDao extends GenericDao<Holiday> {

	Holiday findByNameAndDate(String name, LocalDate date);
}
