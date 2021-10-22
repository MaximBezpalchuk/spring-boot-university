package com.foxminded.university.dao.jdbc.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import com.foxminded.university.dao.jdbc.JdbcGroupDao;
import com.foxminded.university.model.Gender;
import com.foxminded.university.model.Student;

@Component
public class StudentRowMapper implements RowMapper<Student> {

	private JdbcGroupDao groupDao;

	public StudentRowMapper(JdbcGroupDao groupDao) {
		this.groupDao = groupDao;
	}

	@Override
	public Student mapRow(ResultSet resultSet, int rowNum) throws SQLException {
		Student student = Student.builder()
				.firstName(resultSet.getString("first_name"))
				.lastName(resultSet.getString("last_name"))
				.address(resultSet.getString("address"))
				.gender(Gender.valueOf(resultSet.getString("gender")))
				.birthDate(resultSet.getObject("birth_date", LocalDate.class))
				.phone(resultSet.getString("phone"))
				.email(resultSet.getString("email"))
				.postalCode(resultSet.getString("postal_code"))
				.education(resultSet.getString("education"))
				.id(resultSet.getInt("id"))
				.build();
		int groupId = resultSet.getInt("group_id");
		groupDao.findById(groupId).ifPresent(student::setGroup);

		return student;
	}
}
