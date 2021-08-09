package com.foxminded.university.dao.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import com.foxminded.university.dao.TeacherDao;
import com.foxminded.university.model.Vacation;

@Component
public class VacationRowMapper implements RowMapper<Vacation> {

	private TeacherDao teacherDao;
	
	public VacationRowMapper(TeacherDao teacherDao) {
		this.teacherDao = teacherDao;
	}

	@Override
	public Vacation mapRow(ResultSet resultSet, int rowNum) throws SQLException {
		Vacation vacation = new Vacation(resultSet.getTimestamp("start").toLocalDateTime().toLocalDate(),
				resultSet.getTimestamp("finish").toLocalDateTime().toLocalDate(),
				teacherDao.findById(resultSet.getInt("teacher_id")));
		vacation.setId(resultSet.getInt("id"));
		return vacation;
	}

}
