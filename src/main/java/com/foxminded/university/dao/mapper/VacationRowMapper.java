package com.foxminded.university.dao.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

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
		return new Vacation.Builder(resultSet.getObject("start", LocalDate.class),
				resultSet.getObject("finish", LocalDate.class), teacherDao.findById(resultSet.getInt("teacher_id")))
						.setId(resultSet.getInt("id")).build();
	}

}
