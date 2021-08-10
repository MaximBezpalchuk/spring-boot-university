package com.foxminded.university.dao.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

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
		return new Holiday.Builder(resultSet.getString("name"), resultSet.getObject("date", LocalDate.class),
				cathedraDao.findById(resultSet.getInt("cathedra_id"))).setId(resultSet.getInt("id")).build();
	}
}