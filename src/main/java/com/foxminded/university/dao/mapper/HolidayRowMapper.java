package com.foxminded.university.dao.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import com.foxminded.university.dao.CathedraDao;
import com.foxminded.university.model.Holiday;

@Component
public class HolidayRowMapper implements RowMapper<Holiday> {

	private CathedraDao cathedraDao;

	public HolidayRowMapper(CathedraDao cathedraDao) {
		this.cathedraDao = cathedraDao;
	}

	@Override
	public Holiday mapRow(ResultSet resultSet, int rowNum) throws SQLException {
		Holiday holiday = new Holiday(resultSet.getString("name"),
				new java.sql.Date(resultSet.getDate("date").getTime()).toLocalDate(),
				cathedraDao.findById(resultSet.getInt("cathedra_id")));
		holiday.setId(resultSet.getInt("id"));
		return holiday;
	}
}