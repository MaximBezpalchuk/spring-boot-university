package com.foxminded.university.dao.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import com.foxminded.university.dao.GroupDao;
import com.foxminded.university.model.Gender;
import com.foxminded.university.model.Student;

@Component
public class StudentRowMapper implements RowMapper<Student> {

	private GroupDao groupDao;
	
	public StudentRowMapper(GroupDao groupDao) {
		this.groupDao = groupDao;
	}

	@Override
	public Student mapRow(ResultSet resultSet, int rowNum) throws SQLException {
		Student student = new Student(resultSet.getString("first_name"), resultSet.getString("last_name"),
				resultSet.getString("phone"), resultSet.getString("address"), resultSet.getString("email"),
				Gender.valueOf(resultSet.getString("gender")), resultSet.getString("postalcode"),
				resultSet.getString("education"),
				new java.sql.Date(resultSet.getDate("birthdate").getTime()).toLocalDate());
		student.setGroup(groupDao.findById(resultSet.getInt("group_id")));
		student.setId(resultSet.getInt("id"));
		return student;
	}
}
