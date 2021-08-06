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
		Cathedra cathedra = new Cathedra(resultSet.getString("name"));
		cathedra.setId(resultSet.getInt("id"));
		return cathedra;
	}

}
