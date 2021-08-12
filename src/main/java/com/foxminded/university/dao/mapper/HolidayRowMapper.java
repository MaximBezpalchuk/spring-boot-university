package com.foxminded.university.dao.mapper;

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
		return Holiday.build(resultSet.getString("name"), resultSet.getObject("date", LocalDate.class),
				cathedraDao.findById(resultSet.getInt("cathedra_id"))).id(resultSet.getInt("id")).build();
	}
}