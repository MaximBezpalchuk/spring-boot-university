package com.foxminded.university.dao.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import com.foxminded.university.model.Cathedra;

@Component
public class CathedraRowMapper implements RowMapper<Cathedra> {

	@Override
	public Cathedra mapRow(ResultSet resultSet, int rowNum) throws SQLException {
		return new Cathedra.Builder(resultSet.getString("name")).setId(resultSet.getInt("id")).build();
	}
}
