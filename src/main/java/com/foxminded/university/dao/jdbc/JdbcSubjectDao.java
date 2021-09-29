package com.foxminded.university.dao.jdbc;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import com.foxminded.university.dao.SubjectDao;
import com.foxminded.university.dao.jdbc.mapper.SubjectRowMapper;
import com.foxminded.university.model.Subject;

@Component
public class JdbcSubjectDao implements SubjectDao {
	
	private final static Logger logger = LoggerFactory.getLogger(JdbcSubjectDao.class);

	private final static String SELECT_ALL = "SELECT * FROM subjects";
	private final static String SELECT_BY_ID = "SELECT * FROM subjects WHERE id = ?";
	private final static String INSERT_SUBJECT = "INSERT INTO subjects(name, description, cathedra_id) VALUES(?, ?, ?)";
	private final static String UPDATE_SUBJECT = "UPDATE subjects SET name=?, description=?, cathedra_id=? WHERE id=?";
	private final static String DELETE_SUBJECT = "DELETE FROM subjects WHERE id = ?";
	private final static String SELECT_BY_TEACHER_ID = "SELECT * FROM subjects WHERE id IN (SELECT subject_id FROM subjects_teachers WHERE teacher_id =?)";
	private final static String SELECT_BY_NAME = "SELECT * FROM subjects WHERE name = ?";

	private final JdbcTemplate jdbcTemplate;
	private SubjectRowMapper rowMapper;

	public JdbcSubjectDao(JdbcTemplate jdbcTemplate, SubjectRowMapper rowMapper) {
		this.jdbcTemplate = jdbcTemplate;
		this.rowMapper = rowMapper;
	}

	@Override
	public List<Subject> findAll() {
		logger.debug("Find all subjects");
		return jdbcTemplate.query(SELECT_ALL, rowMapper);
	}

	@Override
	public Optional<Subject> findById(int id) {
		logger.debug("Find subject by id: {}", id);
		try {
			return Optional.of(jdbcTemplate.queryForObject(SELECT_BY_ID, rowMapper, id));
		} catch (EmptyResultDataAccessException e) {
			return Optional.empty();
		}
	}

	@Override
	public void save(Subject subject) {
		logger.debug("Save subject {}", subject);
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
			logger.debug("New subject created with id: {}", subject.getId());
		} else {
			jdbcTemplate.update(UPDATE_SUBJECT, subject.getName(), subject.getDescription(),
					subject.getCathedra().getId(), subject.getId());
			logger.debug("Subject with id {} was updated", subject.getId());
		}

	}

	@Override
	public void deleteById(int id) {
		jdbcTemplate.update(DELETE_SUBJECT, id);
		logger.debug("Subject with id {} was deleted", id);
	}

	@Override
	public List<Subject> findByTeacherId(int id) {
		logger.debug("Find subject by teacher id: {}", id);
		return jdbcTemplate.query(SELECT_BY_TEACHER_ID, rowMapper, id);
	}

	@Override
	public Optional<Subject> findByName(String name) {
		logger.debug("Find subject by name: {}", name);
		try {
			return Optional.of(jdbcTemplate.queryForObject(SELECT_BY_NAME, rowMapper, name));
		} catch (EmptyResultDataAccessException e) {
			return Optional.empty();
		}
	}
}
