package com.foxminded.university.dao;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import com.foxminded.university.dao.mapper.StudentRowMapper;
import com.foxminded.university.model.Student;

@Component
public class StudentDao {

	private final static String SELECT_ALL = "SELECT * FROM students";
	private final static String SELECT_BY_ID = "SELECT * FROM students WHERE id = ?";
	private final static String INSERT_STUDENT = "INSERT INTO students(first_name, last_name, phone, address, email, gender, postalcode, education, birthdate, group_id) VALUES(?, ?, ?, ?, ?, ?::\"gender\", ?, ?, ?, ?)";
	private final static String UPDATE_STUDENT = "UPDATE students SET first_name=?, last_name=?, phone=?, address=?, email=?, gender=?::\"gender\", postalcode=?, education=?, birthdate=?, group_id=? WHERE id=?";
	private final static String DELETE_STUDENT = "DELETE FROM students WHERE id = ?";

	private final JdbcTemplate jdbcTemplate;
	private StudentRowMapper rowMapper;
	
	@Autowired
	public StudentDao(JdbcTemplate jdbcTemplate, StudentRowMapper rowMapper) {
		this.jdbcTemplate = jdbcTemplate;
		this.rowMapper = rowMapper;
	}

	public void create(Student student) {
		jdbcTemplate.update(INSERT_STUDENT, student.getFirstName(), student.getLastName(), student.getPhone(),
				student.getAddress(), student.getEmail(), student.getGender(), student.getPostalCode(),
				student.getEducation(), student.getBirthDate(), student.getGroup().getId());
	}

	public List<Student> findAll() {
		return jdbcTemplate.query(SELECT_ALL, rowMapper);
	}

	@SuppressWarnings("deprecation")
	public Student findById(int id) {
		return jdbcTemplate.query(SELECT_BY_ID, new Object[] { id }, rowMapper).stream().findAny().orElse(null);
	}

	public void update(Student student) {
		if (student.getId() == 0) {
			KeyHolder keyHolder = new GeneratedKeyHolder();
			jdbcTemplate.update(connection -> {
				PreparedStatement statement = connection.prepareStatement(INSERT_STUDENT, Statement.RETURN_GENERATED_KEYS);
				statement.setString(1, student.getFirstName());
				statement.setString(2, student.getLastName());
				statement.setString(3, student.getPhone());
				statement.setString(4, student.getAddress());
				statement.setString(5, student.getEmail());
				statement.setString(6, student.getGender().toString());
				statement.setString(7, student.getPostalCode());
				statement.setString(8, student.getEducation());
				statement.setDate(9, java.sql.Date.valueOf(student.getBirthDate()));
				statement.setInt(10, student.getGroup().getId());
				return statement;
			}, keyHolder);
			student.setId((int) keyHolder.getKeyList().get(0).get("id"));
		} else {
			jdbcTemplate.update(UPDATE_STUDENT, student.getFirstName(), student.getLastName(), student.getPhone(),
					student.getAddress(), student.getEmail(), student.getGender().toString(), student.getPostalCode(),
					student.getEducation(), student.getBirthDate(), student.getGroup().getId(), student.getId());
		}

	}

	public void deleteById(int id) {
		jdbcTemplate.update(DELETE_STUDENT, id);
	}

}
