package com.foxminded.university.dao.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import com.foxminded.university.dao.jdbc.JdbcCathedraDao;
import com.foxminded.university.model.Audience;

@Component
public class AudienceRowMapper implements RowMapper<Audience> {

	private JdbcCathedraDao cathedraDao;

	public AudienceRowMapper(JdbcCathedraDao cathedraDao) {
		this.cathedraDao = cathedraDao;
	}

	@Override
	public Audience mapRow(ResultSet resultSet, int rowNum) throws SQLException {
		Audience audience = Audience.build(resultSet.getInt("room"), resultSet.getInt("capacity"),
				cathedraDao.findById(resultSet.getInt("cathedra_id"))).setId(resultSet.getInt("id")).build();
		return audience;
	}

}
