package com.foxminded.university.dao;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import com.foxminded.university.dao.mapper.HolidayRowMapper;
import com.foxminded.university.model.Cathedra;
import com.foxminded.university.model.Holiday;

@Component
public class HolidayDao {

	private final static String SELECT_ALL = "SELECT * FROM holidays";
	private final static String SELECT_BY_ID = "SELECT * FROM holidays WHERE id = ?";
	private final static String INSERT_HOLIDAY = "INSERT INTO holidays(name, date, cathedra_id) VALUES(?, ?, ?)";
	private final static String UPDATE_HOLIDAY = "UPDATE holidays SET name=?, date=?, cathedra_id=? WHERE id=?";
	private final static String DELETE_HOLIDAY = "DELETE FROM holidays WHERE id = ?";

	private final JdbcTemplate jdbcTemplate;
	private HolidayRowMapper rowMapper;

	public HolidayDao(JdbcTemplate jdbcTemplate, HolidayRowMapper rowMapper) {
		this.jdbcTemplate = jdbcTemplate;
		this.rowMapper = rowMapper;
	}

	public List<Holiday> findAll() {
		return jdbcTemplate.query(SELECT_ALL, rowMapper);
	}

	public Holiday findById(int id) {
		return jdbcTemplate.queryForObject(SELECT_BY_ID, rowMapper, id);
	}

	public void save(Holiday holiday, Cathedra cathedra) {
		if (holiday.getId() == 0) {
			KeyHolder keyHolder = new GeneratedKeyHolder();
			jdbcTemplate.update(connection -> {
				PreparedStatement statement = connection.prepareStatement(INSERT_HOLIDAY,
						Statement.RETURN_GENERATED_KEYS);
				statement.setString(1, holiday.getName());
				statement.setDate(2, java.sql.Date.valueOf(holiday.getDate()));
				statement.setInt(3, cathedra.getId());
				return statement;
			}, keyHolder);
			holiday.setId((int) keyHolder.getKeyList().get(0).get("id"));
		} else {
			jdbcTemplate.update(UPDATE_HOLIDAY, holiday.getName(), holiday.getDate(), cathedra.getId(),
					holiday.getId());
		}
	}

	public void deleteById(int id) {
		jdbcTemplate.update(DELETE_HOLIDAY, id);
	}

}
