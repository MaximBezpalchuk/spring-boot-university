package com.foxminded.university.dao;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.foxminded.university.model.Holiday;

public interface HolidayDao extends GenericDao<Holiday> {

	Optional<Holiday> findByNameAndDate(String name, LocalDate date);

	List<Holiday> findByDate(LocalDate date);

	Page<Holiday> findPaginatedHolidays(Pageable pageable);
}
