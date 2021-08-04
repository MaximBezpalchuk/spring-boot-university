package com.foxminded.university.dao.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.foxminded.university.dao.CathedraDao;
import com.foxminded.university.model.Subject;

public class SubjectRowMapper implements RowMapper<Subject> {

	private CathedraDao cathedraDao;

	public SubjectRowMapper(CathedraDao cathedraDao) {
		this.cathedraDao = cathedraDao;
	}

	@Override
	public Subject mapRow(ResultSet resultSet, int rowNum) throws SQLException {
		Subject subject = new Subject(cathedraDao.findById(resultSet.getInt("cathedra_id")),
				resultSet.getString("name"), resultSet.getString("description"));
		subject.setTeachers(teacherDao.findByGroup(resultSet.getInt("id")));
		subject.setId(resultSet.getInt("id"));
		return subject;
	}

}
