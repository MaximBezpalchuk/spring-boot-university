package com.foxminded.university.dao;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import com.foxminded.university.dao.mapper.LectureTimeRowMapper;
import com.foxminded.university.model.LectureTime;

@Component
public class LectureTimeDao {

	private final static String SELECT_ALL = "SELECT * FROM lecture_times";
	private final static String SELECT_BY_ID = "SELECT * FROM lecture_times WHERE id = ?";
	private final static String INSERT_LECTURE_TIME = "INSERT INTO lecture_times(start, finish) VALUES(?, ?)";
	private final static String UPDATE_LECTURE_TIME = "UPDATE lecture_times SET start=?, finish=? WHERE id=?";
	private final static String DELETE_LECTURE_TIME = "DELETE FROM lecture_times WHERE id = ?";

	private final JdbcTemplate jdbcTemplate;
	private LectureTimeRowMapper rowMapper;

	public LectureTimeDao(JdbcTemplate jdbcTemplate, LectureTimeRowMapper rowMapper) {
		this.jdbcTemplate = jdbcTemplate;
		this.rowMapper = rowMapper;
	}

	public List<LectureTime> findAll() {
		return jdbcTemplate.query(SELECT_ALL, rowMapper);
	}

	public LectureTime findById(int id) {
		return jdbcTemplate.queryForObject(SELECT_BY_ID, rowMapper, id);
	}

	public void save(LectureTime lectureTime) {
		if (lectureTime.getId() == 0) {
			KeyHolder keyHolder = new GeneratedKeyHolder();
			jdbcTemplate.update(connection -> {
				PreparedStatement statement = connection.prepareStatement(INSERT_LECTURE_TIME,
						Statement.RETURN_GENERATED_KEYS);
				statement.setObject(1, lectureTime.getStart());
				statement.setObject(2, lectureTime.getEnd());
				return statement;
			}, keyHolder);
			lectureTime.setId((int) keyHolder.getKeyList().get(0).get("id"));
		} else {
			jdbcTemplate.update(UPDATE_LECTURE_TIME, lectureTime.getStart(), lectureTime.getEnd(), lectureTime.getId());
		}

	}

	public void deleteById(int id) {
		jdbcTemplate.update(DELETE_LECTURE_TIME, id);
	}

}
