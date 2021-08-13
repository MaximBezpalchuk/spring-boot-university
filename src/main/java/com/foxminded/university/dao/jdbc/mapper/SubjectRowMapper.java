package com.foxminded.university.dao.jdbc.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import com.foxminded.university.dao.jdbc.JdbcCathedraDao;
import com.foxminded.university.model.Subject;

@Component
public class SubjectRowMapper implements RowMapper<Subject> {

	private JdbcCathedraDao cathedraDao;

	public SubjectRowMapper(JdbcCathedraDao cathedraDao) {
		this.cathedraDao = cathedraDao;
	}

	@Override
	public Subject mapRow(ResultSet resultSet, int rowNum) throws SQLException {
		return Subject.builder().id(resultSet.getInt("id"))
				.cathedra(cathedraDao.findById(resultSet.getInt("cathedra_id"))).name(resultSet.getString("name"))
				.description(resultSet.getString("description")).build();
	}

}
