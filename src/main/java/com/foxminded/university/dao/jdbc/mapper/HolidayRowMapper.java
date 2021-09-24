package com.foxminded.university.dao.jdbc.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import com.foxminded.university.dao.jdbc.JdbcCathedraDao;
import com.foxminded.university.model.Holiday;

@Component
public class HolidayRowMapper implements RowMapper<Holiday> {

	private JdbcCathedraDao cathedraDao;

	public HolidayRowMapper(JdbcCathedraDao cathedraDao) {
		this.cathedraDao = cathedraDao;
	}

	@Override
	public Holiday mapRow(ResultSet resultSet, int rowNum) throws SQLException {
		return Holiday.builder()
				.id(resultSet.getInt("id"))
				.name(resultSet.getString("name"))
				.date(resultSet.getObject("date", LocalDate.class))
				.cathedra(cathedraDao.findById(resultSet.getInt("cathedra_id")).get())
				.build();
	}
}