package com.foxminded.university.dao.jdbc;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import com.foxminded.university.dao.VacationDao;
import com.foxminded.university.dao.jdbc.mapper.VacationRowMapper;
import com.foxminded.university.model.Vacation;

@Component
public class JdbcVacationDao implements VacationDao {

	private final static String SELECT_ALL = "SELECT * FROM vacations";
	private final static String SELECT_BY_ID = "SELECT * FROM vacations WHERE id = ?";
	private final static String INSERT_VACATION = "INSERT INTO vacations(start, finish, teacher_id) VALUES(?, ?, ?)";
	private final static String UPDATE_VACATION = "UPDATE vacations SET start=?, finish=?, teacher_id=? WHERE id=?";
	private final static String DELETE_VACATION = "DELETE FROM vacations WHERE id = ?";
	private final static String SELECT_BY_TEACHER_ID = "SELECT * FROM vacations WHERE teacher_id=?";

	private final JdbcTemplate jdbcTemplate;
	private VacationRowMapper rowMapper;

	public JdbcVacationDao(JdbcTemplate jdbcTemplate, VacationRowMapper rowMapper) {
		this.jdbcTemplate = jdbcTemplate;
		this.rowMapper = rowMapper;
	}

	@Override
	public List<Vacation> findAll() {
		return jdbcTemplate.query(SELECT_ALL, rowMapper);
	}

	@Override
	public Vacation findById(int id) {
		return jdbcTemplate.queryForObject(SELECT_BY_ID, rowMapper, id);
	}

	@Override
	public void save(Vacation vacation) {
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
		} else {
			jdbcTemplate.update(UPDATE_VACATION, vacation.getStart(), vacation.getEnd(), vacation.getTeacher().getId(),
					vacation.getId());
		}

	}

	@Override
	public void deleteById(int id) {
		jdbcTemplate.update(DELETE_VACATION, id);
	}

	@Override
	public List<Vacation> findByTeacherId(int id) {
		return jdbcTemplate.query(SELECT_BY_TEACHER_ID, rowMapper, id);
	}
}
