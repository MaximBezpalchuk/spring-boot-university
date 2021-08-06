package com.foxminded.university.dao;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import com.foxminded.university.dao.mapper.SubjectRowMapper;
import com.foxminded.university.model.Subject;

@Component
public class SubjectDao {

	private final static String SELECT_ALL = "SELECT * FROM subjects";
	private final static String SELECT_BY_ID = "SELECT * FROM subjects WHERE id = ?";
	private final static String INSERT_SUBJECT = "INSERT INTO subjects(name, description, cathedra_id) VALUES(?, ?, ?)";
	private final static String UPDATE_SUBJECT = "UPDATE subjects SET name=?, description=?, cathedra_id=? WHERE id=?";
	private final static String DELETE_SUBJECT = "DELETE FROM subjects WHERE id = ?";
	private final static String SELECT_BY_TEACHER_ID = "SELECT * FROM subjects WHERE id IN (SELECT subject_id FROM subjects_teachers WHERE teacher_id =?)";
	

	private final JdbcTemplate jdbcTemplate;
	private SubjectRowMapper rowMapper;

	@Autowired
	public SubjectDao(JdbcTemplate jdbcTemplate, SubjectRowMapper rowMapper) {
		this.jdbcTemplate = jdbcTemplate;
		this.rowMapper = rowMapper;
	}

	public List<Subject> findAll() {
		return jdbcTemplate.query(SELECT_ALL, rowMapper);
	}

	@SuppressWarnings("deprecation")
	public Subject findById(int id) {
		return jdbcTemplate.query(SELECT_BY_ID, new Object[] { id }, rowMapper).stream().findAny().orElse(null);
	}

	public void update(Subject subject) {
		if (subject.getId() == 0) {
			KeyHolder keyHolder = new GeneratedKeyHolder();
			jdbcTemplate.update(connection -> {
				PreparedStatement statement = connection.prepareStatement(INSERT_SUBJECT,
						Statement.RETURN_GENERATED_KEYS);
				statement.setString(1, subject.getName());
				statement.setString(2, subject.getDescription());
				statement.setInt(3, subject.getCathedra().getId());
				return statement;
			}, keyHolder);
			subject.setId((int) keyHolder.getKeyList().get(0).get("id"));
		} else {
			jdbcTemplate.update(UPDATE_SUBJECT, subject.getName(), subject.getDescription(),
					subject.getCathedra().getId(), subject.getId());
		}

	}

	public void deleteById(int id) {
		jdbcTemplate.update(DELETE_SUBJECT, id);
	}

	@SuppressWarnings("deprecation")
	public List<Subject> findByTeacherId(int id) {
		return jdbcTemplate.query(SELECT_BY_TEACHER_ID, new Object[] { id }, rowMapper);
	}

}
