package com.foxminded.university.dao.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.foxminded.university.model.Holiday;

public class HolidayRowMapper implements RowMapper<Holiday> {

	@Override
	public Holiday mapRow(ResultSet resultSet, int rowNum) throws SQLException {
		Holiday holiday = new Holiday(resultSet.getString("name"),
				new java.sql.Date(resultSet.getDate("date").getTime()).toLocalDate());
		holiday.setId(resultSet.getInt("id"));
		return holiday;
	}
}