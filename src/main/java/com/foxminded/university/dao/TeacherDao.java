package com.foxminded.university.dao;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import com.foxminded.university.dao.mapper.TeacherRowMapper;
import com.foxminded.university.model.Subject;
import com.foxminded.university.model.Teacher;

@Component
public class TeacherDao {

	private final static String SELECT_ALL = "SELECT * FROM teachers";
	private final static String SELECT_BY_ID = "SELECT * FROM teachers WHERE id = ?";
	private final static String INSERT_TEACHER = "INSERT INTO teachers(id, first_name, last_name, phone, address, email, gender, postalcode, education, birthdate, cathedra_id,  degree) VALUES(DEFAULT, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
	private final static String UPDATE_TEACHER = "UPDATE teachers SET first_name=?, last_name=?, phone=?, address=?, email=?, gender=?, postalcode=?, education=?, birthdate=?, cathedra_id=?, degree=? WHERE id=?";
	private final static String DELETE_TEACHER = "DELETE FROM teachers WHERE id = ?";
	private final static String INSERT_SUBJECT = "INSERT INTO subjects_teachers(teacher_id, subject_id) VALUES(?, ?)";

	private final JdbcTemplate jdbcTemplate;
	private TeacherRowMapper rowMapper;

	public TeacherDao(JdbcTemplate jdbcTemplate, TeacherRowMapper rowMapper) {
		this.jdbcTemplate = jdbcTemplate;
		this.rowMapper = rowMapper;
	}

	public List<Teacher> findAll() {
		return jdbcTemplate.query(SELECT_ALL, rowMapper);
	}

	public Teacher findById(int id) {
		return jdbcTemplate.queryForObject(SELECT_BY_ID, rowMapper, id);
	}

	public void save(Teacher teacher) {
		if (teacher.getId() == 0) {
			KeyHolder keyHolder = new GeneratedKeyHolder();
			jdbcTemplate.update(connection -> {
				PreparedStatement statement = connection.prepareStatement(INSERT_TEACHER,
						Statement.RETURN_GENERATED_KEYS);
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
			teacher.setId((int) keyHolder.getKeyList().get(0).get("id"));
		} else {
			jdbcTemplate.update(UPDATE_TEACHER, teacher.getFirstName(), teacher.getLastName(), teacher.getPhone(),
					teacher.getAddress(), teacher.getEmail(), teacher.getGender().toString(), teacher.getPostalCode(),
					teacher.getEducation(), teacher.getBirthDate(), teacher.getCathedra().getId(),
					teacher.getDegree().toString(), teacher.getId());
		}

	}

	public void deleteById(int id) {
		jdbcTemplate.update(DELETE_TEACHER, id);
	}

	public void updateSubject(Teacher teacher, Subject subject) {
		jdbcTemplate.update(INSERT_SUBJECT, teacher.getId(), subject.getId());
	}

}
