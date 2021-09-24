package com.foxminded.university.dao.jdbc;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import com.foxminded.university.dao.VacationDao;
import com.foxminded.university.dao.jdbc.mapper.VacationRowMapper;
import com.foxminded.university.model.Teacher;
import com.foxminded.university.model.Vacation;

@Component
public class JdbcVacationDao implements VacationDao {

	private final static Logger logger = LoggerFactory.getLogger(JdbcVacationDao.class);

	private final static String SELECT_ALL = "SELECT * FROM vacations";
	private final static String SELECT_BY_ID = "SELECT * FROM vacations WHERE id = ?";
	private final static String INSERT_VACATION = "INSERT INTO vacations(start, finish, teacher_id) VALUES(?, ?, ?)";
	private final static String UPDATE_VACATION = "UPDATE vacations SET start=?, finish=?, teacher_id=? WHERE id=?";
	private final static String DELETE_VACATION = "DELETE FROM vacations WHERE id = ?";
	private final static String SELECT_BY_TEACHER_ID = "SELECT * FROM vacations WHERE teacher_id=?";
	private final static String SELECT_BY_TEACHER_ID_AND_YEAR = "SELECT * FROM vacations WHERE teacher_id = ? AND EXTRACT(YEAR FROM start) = ?";
	private final static String SELECT_BY_PERIOD_AND_TEACHER_ID = "SELECT * FROM vacations WHERE start = ? AND finish = ? AND teacher_id = ?";
	private final static String SELECT_BY_DATE_IN_PERIOD_AND_TEACHER_ID = "SELECT * FROM vacations WHERE start >= ? AND finish <= ? AND teacher_id = ?";

	private final JdbcTemplate jdbcTemplate;
	private VacationRowMapper rowMapper;

	public JdbcVacationDao(JdbcTemplate jdbcTemplate, VacationRowMapper rowMapper) {
		this.jdbcTemplate = jdbcTemplate;
		this.rowMapper = rowMapper;
	}

	@Override
	public List<Vacation> findAll() {
		logger.debug("Find all vacations");
		return jdbcTemplate.query(SELECT_ALL, rowMapper);
	}

	@Override
	public Optional<Vacation> findById(int id) {
		logger.debug("Find vacation by id: {}", id);
		try {
			return Optional.of(jdbcTemplate.queryForObject(SELECT_BY_ID, rowMapper, id));
		} catch (EmptyResultDataAccessException e) {
			return Optional.empty();
		}
	}

	@Override
	public void save(Vacation vacation) {
		logger.debug("Save vacation {}", vacation);
		if (vacation.getId() == 0) {
			KeyHolder keyHolder = new GeneratedKeyHolder();
			jdbcTemplate.update(connection -> {
				PreparedStatement statement = connection.prepareStatement(INSERT_VACATION,
						Statement.RETURN_GENERATED_KEYS);
				statement.setObject(1, vacation.getStart());
				statement.setObject(2, vacation.getEnd());
				statement.setInt(3, vacation.getTeacher().getId());
				return statement;
			}, keyHolder);
			vacation.setId((int) keyHolder.getKeyList().get(0).get("id"));
			logger.debug("New vacation created with id: {}", vacation.getId());
		} else {
			jdbcTemplate.update(UPDATE_VACATION, vacation.getStart(), vacation.getEnd(), vacation.getTeacher().getId(),
					vacation.getId());
			logger.debug("Vacation with id {} was updated", vacation.getId());
		}
	}

	@Override
	public void deleteById(int id) {
		jdbcTemplate.update(DELETE_VACATION, id);
		logger.debug("Vacation with id {} was deleted", id);
	}

	@Override
	public List<Vacation> findByTeacherId(int id) {
		logger.debug("Find vacations by teacher id: {}", id);
		return jdbcTemplate.query(SELECT_BY_TEACHER_ID, rowMapper, id);
	}

	@Override
	public Optional<Vacation> findByPeriodAndTeacher(LocalDate start, LocalDate end, Teacher teacher) {
		logger.debug("Find vacation by vacation start: {}, end: {}, teacher id: {}", start, end, teacher.getId());
		try {
			return Optional.of(jdbcTemplate.queryForObject(SELECT_BY_PERIOD_AND_TEACHER_ID, rowMapper, start, end,
					teacher.getId()));
		} catch (EmptyResultDataAccessException e) {
			return Optional.empty();
		}
	}

	@Override
	public List<Vacation> findByDateInPeriodAndTeacher(LocalDate date, Teacher teacher) {
		logger.debug("Find vacations by vacation date: {} and teacher id: {}", date, teacher.getId());
		return jdbcTemplate.query(SELECT_BY_DATE_IN_PERIOD_AND_TEACHER_ID, rowMapper, date, date, teacher.getId());
	}

	@Override
	public List<Vacation> findByTeacherIdAndYear(int id, int year) {
		logger.debug("Find vacations by teacher id: {} and year: {}", id, year);
		return jdbcTemplate.query(SELECT_BY_TEACHER_ID_AND_YEAR, rowMapper, id, year);
	}
}
