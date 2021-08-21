package com.foxminded.university.dao.jdbc.mapper;

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
		Audience audience = Audience.builder()
				.id(resultSet.getInt("id"))
				.room(resultSet.getInt("room"))
				.capacity(resultSet.getInt("capacity"))
				.cathedra(cathedraDao.findById(resultSet.getInt("cathedra_id")))
				.build();
		return audience;
	}

}
