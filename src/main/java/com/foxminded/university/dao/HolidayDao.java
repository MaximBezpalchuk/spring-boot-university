package com.foxminded.university.dao;

import java.sql.PreparedStatement;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import com.foxminded.university.dao.mapper.HolidayRowMapper;
import com.foxminded.university.model.Holiday;

@Component
public class HolidayDao {
	
	private final static String SELECT_ALL = "SELECT * FROM holidays";
	private final static String SELECT_BY_ID = "SELECT * FROM holidays WHERE id = ?";
	private final static String INSERT_HOLIDAY = "INSERT INTO holidays(name, date) VALUES(?, ?)";
	private final static String UPDATE_HOLIDAY = "UPDATE holidays SET name=?, date=? WHERE id=?";
	private final static String DELETE_HOLIDAY = "DELETE FROM holidays WHERE id = ?";
	
	private final JdbcTemplate jdbcTemplate;
	private HolidayRowMapper rowMapper;
	
	@Autowired
	public HolidayDao(JdbcTemplate jdbcTemplate, HolidayRowMapper rowMapper) {
		this.jdbcTemplate = jdbcTemplate;
		this.rowMapper = rowMapper;
	}
	
	public void create(Holiday holiday) {
		jdbcTemplate.update(INSERT_HOLIDAY, holiday.getName(), holiday.getDate());
	}

	public List<Holiday> findAll() {
		return jdbcTemplate.query(SELECT_ALL, rowMapper);
	}

	@SuppressWarnings("deprecation")
	public Holiday findById(int id) {
		return jdbcTemplate.query(SELECT_BY_ID, new Object[] { id }, rowMapper).stream().findAny().orElse(null);
	}

	public void update(Holiday holiday) {
		if (holiday.getId() == 0) {
			KeyHolder keyHolder = new GeneratedKeyHolder();
			jdbcTemplate.update(connection -> {
				PreparedStatement statement = connection.prepareStatement(INSERT_HOLIDAY);
				statement.setString(1, holiday.getName());
				statement.setDate(2, java.sql.Date.valueOf(holiday.getDate()));
				return statement;
			}, keyHolder);
			holiday.setId((int) keyHolder.getKey());
		} else {
			jdbcTemplate.update(UPDATE_HOLIDAY, holiday.getName(), holiday.getDate(), holiday.getId());
		}

	}

	public void deleteById(int id) {
		jdbcTemplate.update(DELETE_HOLIDAY, id);
	}

}
