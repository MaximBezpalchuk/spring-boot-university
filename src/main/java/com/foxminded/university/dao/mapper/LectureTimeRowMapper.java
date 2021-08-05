package com.foxminded.university.dao.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import com.foxminded.university.model.LectureTime;

@Component
public class LectureTimeRowMapper implements RowMapper<LectureTime> {

	@Override
	public LectureTime mapRow(ResultSet resultSet, int rowNum) throws SQLException {
		LectureTime lectureTime = new LectureTime(resultSet.getTime("start").toLocalTime(),
				resultSet.getTime("end").toLocalTime());
		lectureTime.setId(resultSet.getInt("id"));
		return lectureTime;
	}
}
