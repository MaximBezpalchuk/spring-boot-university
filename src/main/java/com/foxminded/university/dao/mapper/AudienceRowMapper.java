package com.foxminded.university.dao.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import com.foxminded.university.model.Audience;

@Component
public class AudienceRowMapper implements RowMapper<Audience> {

	@Override
	public Audience mapRow(ResultSet resultSet, int rowNum) throws SQLException {
		Audience audience = new Audience(resultSet.getInt("room"), resultSet.getInt("capacity"));
		audience.setId(resultSet.getInt("id"));
		return audience;
	}

}
