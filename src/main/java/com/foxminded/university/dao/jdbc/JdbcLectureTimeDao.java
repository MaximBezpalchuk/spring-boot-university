package com.foxminded.university.dao.jdbc;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import com.foxminded.university.dao.LectureTimeDao;
import com.foxminded.university.dao.jdbc.mapper.LectureTimeRowMapper;
import com.foxminded.university.model.LectureTime;

@Component
public class JdbcLectureTimeDao implements LectureTimeDao {

	private static final Logger logger = LoggerFactory.getLogger(JdbcLectureTimeDao.class);

	private static final String SELECT_ALL = "SELECT * FROM lecture_times";
	private static final String SELECT_BY_ID = "SELECT * FROM lecture_times WHERE id = ?";
	private static final String INSERT_LECTURE_TIME = "INSERT INTO lecture_times(start, finish) VALUES(?, ?)";
	private static final String UPDATE_LECTURE_TIME = "UPDATE lecture_times SET start=?, finish=? WHERE id=?";
	private static final String DELETE_LECTURE_TIME = "DELETE FROM lecture_times WHERE id = ?";
	private static final String SELECT_BY_PERIOD = "SELECT * FROM lecture_times WHERE start = ? AND finish = ?";

	private final JdbcTemplate jdbcTemplate;
	private LectureTimeRowMapper rowMapper;

	public JdbcLectureTimeDao(JdbcTemplate jdbcTemplate, LectureTimeRowMapper rowMapper) {
		this.jdbcTemplate = jdbcTemplate;
		this.rowMapper = rowMapper;
	}

	@Override
	public List<LectureTime> findAll() {
		logger.debug("Find all lecture times");
		return jdbcTemplate.query(SELECT_ALL, rowMapper);
	}

	@Override
	public Optional<LectureTime> findById(int id) {
		logger.debug("Find lecture time by id: {}", id);
		try {
			return Optional.of(jdbcTemplate.queryForObject(SELECT_BY_ID, rowMapper, id));
		} catch (EmptyResultDataAccessException e) {
			return Optional.empty();
		}
	}

	@Override
	public void save(LectureTime lectureTime) {
		logger.debug("Save lecture time {}", lectureTime);
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
			logger.debug("New lecture time created with id: {}", lectureTime.getId());
		} else {
			jdbcTemplate.update(UPDATE_LECTURE_TIME, lectureTime.getStart(), lectureTime.getEnd(), lectureTime.getId());
			logger.debug("Lecture time with id {} was updated", lectureTime.getId());
		}

	}

	@Override
	public void deleteById(int id) {
		jdbcTemplate.update(DELETE_LECTURE_TIME, id);
		logger.debug("Lecture time with id {} was deleted", id);
	}

	@Override
	public Optional<LectureTime> findByPeriod(LocalTime start, LocalTime end) {
		logger.debug("Find lecture time which starts at {} and end at {}", start, end);
		try {
			return Optional.of(jdbcTemplate.queryForObject(SELECT_BY_PERIOD, rowMapper, start, end));
		} catch (EmptyResultDataAccessException e) {
			return Optional.empty();
		}
	}
}
