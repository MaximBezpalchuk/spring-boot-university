package com.foxminded.university.dao;

//TODO: update teacher_id from TeacherDao
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import com.foxminded.university.dao.mapper.VacationRowMapper;
import com.foxminded.university.model.Vacation;

@Component
public class VacationDao {

	private final static String SELECT_ALL = "SELECT * FROM vacations";
	private final static String SELECT_BY_ID = "SELECT * FROM vacations WHERE id = ?";
	private final static String INSERT_VACATION = "INSERT INTO vacations(start, finish, teacher_id) VALUES(?, ?, ?)";
	private final static String UPDATE_VACATION = "UPDATE vacations SET start=?, finish=?, teacher_id=? WHERE id=?";
	private final static String DELETE_VACATION = "DELETE FROM vacations WHERE id = ?";

	private final JdbcTemplate jdbcTemplate;
	private VacationRowMapper rowMapper;
	
	@Autowired
	public VacationDao(JdbcTemplate jdbcTemplate, VacationRowMapper rowMapper) {
		this.jdbcTemplate = jdbcTemplate;
		this.rowMapper = rowMapper;
	}

	public void create(Vacation vacation) {
		jdbcTemplate.update(INSERT_VACATION, vacation.getStart(), vacation.getEnd());
	}

	public List<Vacation> findAll() {
		return jdbcTemplate.query(SELECT_ALL, rowMapper);
	}

	@SuppressWarnings("deprecation")
	public Vacation findById(int id) {
		return jdbcTemplate.query(SELECT_BY_ID, new Object[] { id }, rowMapper).stream().findAny().orElse(null);
	}

	public void update(Vacation vacation) {
		if (vacation.getId() == 0) {
			KeyHolder keyHolder = new GeneratedKeyHolder();
			jdbcTemplate.update(connection -> {
				PreparedStatement statement = connection.prepareStatement(INSERT_VACATION, Statement.RETURN_GENERATED_KEYS);
				statement.setDate(1, java.sql.Date.valueOf(vacation.getStart()));
				statement.setDate(2, java.sql.Date.valueOf(vacation.getEnd()));
				statement.setInt(3, vacation.getTeacher().getId());
				return statement;
			}, keyHolder);
			vacation.setId((int) keyHolder.getKeyList().get(0).get("id"));
		} else {
			jdbcTemplate.update(UPDATE_VACATION, java.sql.Date.valueOf(vacation.getStart()),
					java.sql.Date.valueOf(vacation.getEnd()), vacation.getTeacher().getId(), vacation.getId());
		}

	}

	public void deleteById(int id) {
		jdbcTemplate.update(DELETE_VACATION, id);
	}
}
