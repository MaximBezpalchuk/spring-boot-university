package com.foxminded.university.dao;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import com.foxminded.university.dao.mapper.AudienceRowMapper;
import com.foxminded.university.model.Audience;

@Component
public class AudienceDao {

	private final static String SELECT_ALL = "SELECT * FROM audiences";
	private final static String SELECT_BY_ID = "SELECT * FROM audiences WHERE id = ?";
	private final static String INSERT_AUDIENCE = "INSERT INTO audiences(room, capacity, cathedra_id) VALUES(?, ?, ?)";
	private final static String UPDATE_AUDIENCE = "UPDATE audiences SET room=?, capacity=?, cathedra_id=? WHERE id=?";
	private final static String DELETE_AUDIENCE = "DELETE FROM audiences WHERE id = ?";

	private final JdbcTemplate jdbcTemplate;
	private AudienceRowMapper rowMapper;

	@Autowired
	public AudienceDao(JdbcTemplate jdbcTemplate, AudienceRowMapper rowMapper) {
		this.jdbcTemplate = jdbcTemplate;
		this.rowMapper = rowMapper;
	}

	public List<Audience> findAll() {
		return jdbcTemplate.query(SELECT_ALL, rowMapper);
	}

	@SuppressWarnings("deprecation")
	public Audience findById(int id) {
		return jdbcTemplate.query(SELECT_BY_ID, new Object[] { id }, rowMapper).stream().findAny().orElse(null);
	}

	public void update(Audience audience) {
		if (audience.getId() == 0) {
			KeyHolder keyHolder = new GeneratedKeyHolder();
			jdbcTemplate.update(connection -> {
				PreparedStatement statement = connection.prepareStatement(INSERT_AUDIENCE, Statement.RETURN_GENERATED_KEYS);
				statement.setInt(1, audience.getRoom());
				statement.setInt(2, audience.getCapacity());
				statement.setInt(3, audience.getCathedra().getId());
				return statement;
			}, keyHolder);
			audience.setId((int) keyHolder.getKeyList().get(0).get("id"));
		} else {
			jdbcTemplate.update(UPDATE_AUDIENCE, audience.getRoom(), audience.getCapacity(), audience.getCathedra().getId(), audience.getId());
		}

	}

	public void deleteById(int id) {
		jdbcTemplate.update(DELETE_AUDIENCE, id);
	}

}
