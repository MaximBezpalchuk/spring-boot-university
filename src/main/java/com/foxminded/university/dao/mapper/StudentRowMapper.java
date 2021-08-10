package com.foxminded.university.dao.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

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
		Student student = new Student.Builder()
				.setFirstName(resultSet.getString("first_name"))
				.setLastName(resultSet.getString("last_name"))
				.setPhone(resultSet.getString("phone"))
				.setAddress(resultSet.getString("address"))
				.setEmail(resultSet.getString("email"))
				.setGender(Gender.valueOf(resultSet.getString("gender")))
				.setPostalCode(resultSet.getString("postal_code"))
				.setEducation(resultSet.getString("education"))
				.setBirthDate(resultSet.getObject("birth_date", LocalDate.class))
				.setGroup(groupDao.findById(resultSet.getInt("group_id")))
				.setId(resultSet.getInt("id"))
				.build();
		return student;
	}
}
