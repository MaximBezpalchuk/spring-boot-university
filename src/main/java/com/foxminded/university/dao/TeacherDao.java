package com.foxminded.university.dao;

import java.sql.PreparedStatement;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import com.foxminded.university.dao.mapper.TeacherRowMapper;
import com.foxminded.university.model.Teacher;

@Component
public class TeacherDao {

	private final static String SELECT_ALL = "SELECT * FROM teachers";
	private final static String SELECT_BY_ID = "SELECT * FROM teachers WHERE id = ?";
	private final static String INSERT_STUDENT = "INSERT INTO teachers(first_name, last_name, phone, address, email, gender, postalcode, education, birthdate, cathedra_id,  degree) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
	private final static String UPDATE_STUDENT = "UPDATE teachers SET first_name=?, last_name=?, phone=?, address=?, email=?, gender=?, postalcode=?, education=?, birthdate=?, cathedra_id=?, degree=? WHERE id=?";
	private final static String DELETE_STUDENT = "DELETE FROM teachers WHERE id = ?";

	private final JdbcTemplate jdbcTemplate;
	private TeacherRowMapper rowMapper;

	public TeacherDao(JdbcTemplate jdbcTemplate, TeacherRowMapper rowMapper) {
		this.jdbcTemplate = jdbcTemplate;
		this.rowMapper = rowMapper;
	}

	public void create(Teacher teacher) {
		jdbcTemplate.update(INSERT_STUDENT, teacher.getFirstName(), teacher.getLastName(), teacher.getPhone(),
				teacher.getAddress(), teacher.getEmail(), teacher.getGender().toString(), teacher.getPostalCode(),
				teacher.getEducation(), teacher.getBirthDate(), teacher.getCathedra().getId(),
				teacher.getDegree().toString());
	}

	public List<Teacher> findAll() {
		return jdbcTemplate.query(SELECT_ALL, rowMapper);
	}

	@SuppressWarnings("deprecation")
	public Teacher findById(int id) {
		return jdbcTemplate.query(SELECT_BY_ID, new Object[] { id }, rowMapper).stream().findAny().orElse(null);
	}

	public void update(Teacher teacher) {
		if (teacher.getId() == 0) {
			KeyHolder keyHolder = new GeneratedKeyHolder();
			jdbcTemplate.update(connection -> {
				PreparedStatement statement = connection.prepareStatement(INSERT_STUDENT);
				statement.setString(1, teacher.getFirstName());
				statement.setString(2, teacher.getLastName());
				statement.setString(3, teacher.getPhone());
				statement.setString(4, teacher.getAddress());
				statement.setString(5, teacher.getEmail());
				statement.setString(6, teacher.getGender().toString());
				statement.setString(7, teacher.getPostalCode());
				statement.setString(8, teacher.getEducation());
				statement.setDate(9, java.sql.Date.valueOf(teacher.getBirthDate()));
				statement.setInt(10, teacher.getCathedra().getId());
				statement.setString(11, teacher.getDegree().toString());
				return statement;
			}, keyHolder);
			teacher.setId((int) keyHolder.getKey());
		} else {
			jdbcTemplate.update(UPDATE_STUDENT, teacher.getFirstName(), teacher.getLastName(), teacher.getPhone(),
					teacher.getAddress(), teacher.getEmail(), teacher.getGender().toString(), teacher.getPostalCode(),
					teacher.getEducation(), teacher.getBirthDate(), teacher.getCathedra().getId(),
					teacher.getDegree().toString(), teacher.getId());
		}

	}

	public void deleteById(int id) {
		jdbcTemplate.update(DELETE_STUDENT, id);
	}
}
