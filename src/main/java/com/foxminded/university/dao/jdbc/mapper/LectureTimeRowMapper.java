package com.foxminded.university.dao.jdbc.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalTime;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import com.foxminded.university.model.LectureTime;

@Component
public class LectureTimeRowMapper implements RowMapper<LectureTime> {

	@Override
	public LectureTime mapRow(ResultSet resultSet, int rowNum) throws SQLException {
		return LectureTime
				.build(resultSet.getObject("start", LocalTime.class), resultSet.getObject("finish", LocalTime.class))
				.id(resultSet.getInt("id")).build();
	}
}