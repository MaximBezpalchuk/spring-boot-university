package com.foxminded.university.dao.jdbc;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.List;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import com.foxminded.university.dao.HolidayDao;
import com.foxminded.university.dao.jdbc.mapper.HolidayRowMapper;
import com.foxminded.university.model.Holiday;

@Component
public class JdbcHolidayDao implements HolidayDao {

	private final static String SELECT_ALL = "SELECT * FROM holidays";
	private final static String SELECT_BY_ID = "SELECT * FROM holidays WHERE id = ?";
	private final static String INSERT_HOLIDAY = "INSERT INTO holidays(name, date, cathedra_id) VALUES(?, ?, ?)";
	private final static String UPDATE_HOLIDAY = "UPDATE holidays SET name=?, date=?, cathedra_id=? WHERE id=?";
	private final static String DELETE_HOLIDAY = "DELETE FROM holidays WHERE id = ?";
	private final static String SELECT_BY_NAME_AND_DATE = "SELECT * FROM holidays WHERE name = ? AND date = ?";
	private final static String SELECT_BY_DATE = "SELECT * FROM holidays WHERE date = ?";

	private final JdbcTemplate jdbcTemplate;
	private HolidayRowMapper rowMapper;

	public JdbcHolidayDao(JdbcTemplate jdbcTemplate, HolidayRowMapper rowMapper) {
		this.jdbcTemplate = jdbcTemplate;
		this.rowMapper = rowMapper;
	}

	@Override
	public List<Holiday> findAll() {
		return jdbcTemplate.query(SELECT_ALL, rowMapper);
	}

	@Override
	public Holiday findById(int id) {
		try {
			return jdbcTemplate.queryForObject(SELECT_BY_ID, rowMapper, id);
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	@Override
	public void save(Holiday holiday) {
		if (holiday.getId() == 0) {
			KeyHolder keyHolder = new GeneratedKeyHolder();
			jdbcTemplate.update(connection -> {
				PreparedStatement statement = connection.prepareStatement(INSERT_HOLIDAY,
						Statement.RETURN_GENERATED_KEYS);
				statement.setString(1, holiday.getName());
				statement.setObject(2, holiday.getDate());
				statement.setInt(3, holiday.getCathedra().getId());
				return statement;
			}, keyHolder);
			holiday.setId((int) keyHolder.getKeyList().get(0).get("id"));
		} else {
			jdbcTemplate.update(UPDATE_HOLIDAY, holiday.getName(), holiday.getDate(), holiday.getCathedra().getId(),
					holiday.getId());
		}
	}

	@Override
	public void deleteById(int id) {
		jdbcTemplate.update(DELETE_HOLIDAY, id);
	}

	@Override
	public Holiday findByNameAndDate(String name, LocalDate date) {
		try {
			return jdbcTemplate.queryForObject(SELECT_BY_NAME_AND_DATE, rowMapper, name, date);
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}
	
	@Override
	public List<Holiday> findByDate(LocalDate date) {
		try {
			return jdbcTemplate.query(SELECT_BY_DATE, rowMapper,  date);
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}
}
