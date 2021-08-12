package com.foxminded.university.dao.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import com.foxminded.university.dao.jdbc.JdbcGroupDao;
import com.foxminded.university.model.Gender;
import com.foxminded.university.model.Group;
import com.foxminded.university.model.Student;

@Component
public class StudentRowMapper implements RowMapper<Student> {

	private JdbcGroupDao groupDao;

	public StudentRowMapper(JdbcGroupDao groupDao) {
		this.groupDao = groupDao;
	}

	@Override
	public Student mapRow(ResultSet resultSet, int rowNum) throws SQLException {
		Student student = Student
				.build(resultSet.getString("first_name"), resultSet.getString("last_name"),
						resultSet.getString("address"), Gender.valueOf(resultSet.getString("gender")),
						resultSet.getObject("birth_date", LocalDate.class))
				.phone(resultSet.getString("phone")).email(resultSet.getString("email"))
				.postalCode(resultSet.getString("postal_code")).education(resultSet.getString("education"))
				.id(resultSet.getInt("id")).build();
		Object group_id = resultSet.getObject("group_id");
		if (group_id != null) {
			Group group = groupDao.findById((int) group_id);
			student.setGroup(group);
		}
		return student;
	}
}
