package com.foxminded.university.dao.jdbc;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import com.foxminded.university.dao.GenericStudentDao;
import com.foxminded.university.dao.jdbc.mapper.StudentRowMapper;
import com.foxminded.university.model.Group;
import com.foxminded.university.model.Student;

@Component
public class JdbcStudentDao implements GenericStudentDao {

	private final static String SELECT_ALL = "SELECT * FROM students";
	private final static String SELECT_BY_ID = "SELECT * FROM students WHERE id = ?";
	private final static String INSERT_STUDENT = "INSERT INTO students(first_name, last_name, phone, address, email, gender, postal_code, education, birth_date, group_id) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
	private final static String UPDATE_STUDENT = "UPDATE students SET first_name=?, last_name=?, phone=?, address=?, email=?, gender=?, postal_code=?, education=?, birth_date=?, group_id=? WHERE id=?";
	private final static String DELETE_STUDENT = "DELETE FROM students WHERE id = ?";

	private final JdbcTemplate jdbcTemplate;
	private StudentRowMapper rowMapper;

	public JdbcStudentDao(JdbcTemplate jdbcTemplate, StudentRowMapper rowMapper) {
		this.jdbcTemplate = jdbcTemplate;
		this.rowMapper = rowMapper;
	}

	@Override
	public List<Student> findAll() {
		return jdbcTemplate.query(SELECT_ALL, rowMapper);
	}

	@Override
	public Student findById(int id) {
		return jdbcTemplate.queryForObject(SELECT_BY_ID, rowMapper, id);
	}

	@Override
	public void save(Student student) {
		if (student.getId() == 0) {
			KeyHolder keyHolder = new GeneratedKeyHolder();
			Group group = student.getGroup();
			jdbcTemplate.update(connection -> {
				PreparedStatement statement = connection.prepareStatement(INSERT_STUDENT,
						Statement.RETURN_GENERATED_KEYS);
				statement.setString(1, student.getFirstName());
				statement.setString(2, student.getLastName());
				statement.setString(3, student.getPhone());
				statement.setString(4, student.getAddress());
				statement.setString(5, student.getEmail());
				statement.setString(6, student.getGender().toString());
				statement.setString(7, student.getPostalCode());
				statement.setString(8, student.getEducation());
				statement.setObject(9, student.getBirthDate());
				if (group != null) {
					statement.setInt(10, student.getGroup().getId());
				} else {
					statement.setObject(10, group);
				}
				return statement;
			}, keyHolder);
			student.setId((int) keyHolder.getKeyList().get(0).get("id"));
		} else {

			Group group = student.getGroup();
			jdbcTemplate.update(connection -> {
				PreparedStatement statement = connection.prepareStatement(UPDATE_STUDENT);
				statement.setString(1, student.getFirstName());
				statement.setString(2, student.getLastName());
				statement.setString(3, student.getPhone());
				statement.setString(4, student.getAddress());
				statement.setString(5, student.getEmail());
				statement.setString(6, student.getGender().toString());
				statement.setString(7, student.getPostalCode());
				statement.setString(8, student.getEducation());
				statement.setObject(9, student.getBirthDate());
				if (group != null) {
					statement.setInt(10, student.getGroup().getId());
				} else {
					statement.setObject(10, group);
				}
				statement.setInt(11, student.getId());
				return statement;
			});
		}

	}

	@Override
	public void deleteById(int id) {
		jdbcTemplate.update(DELETE_STUDENT, id);
	}

}
