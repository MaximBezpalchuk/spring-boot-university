package com.foxminded.university.dao.jdbc.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import com.foxminded.university.dao.jdbc.JdbcTeacherDao;
import com.foxminded.university.model.Vacation;

@Component
public class VacationRowMapper implements RowMapper<Vacation> {

	private JdbcTeacherDao teacherDao;

	public VacationRowMapper(JdbcTeacherDao teacherDao) {
		this.teacherDao = teacherDao;
	}

	@Override
	public Vacation mapRow(ResultSet resultSet, int rowNum) throws SQLException {
		Vacation vacation = Vacation.builder()
				.start(resultSet.getObject("start", LocalDate.class))
				.end(resultSet.getObject("finish", LocalDate.class))
				.id(resultSet.getInt("id"))
				.build();
		teacherDao.findById(resultSet.getInt("teacher_id")).ifPresent(vacation::setTeacher);

		return vacation;
	}
}
