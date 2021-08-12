package com.foxminded.university.dao.jdbc;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import com.foxminded.university.dao.GenericAudienceDao;
import com.foxminded.university.dao.jdbc.mapper.AudienceRowMapper;
import com.foxminded.university.model.Audience;

@Component
public class JdbcAudienceDao implements GenericAudienceDao {

	private final static String SELECT_ALL = "SELECT * FROM audiences";
	private final static String SELECT_BY_ID = "SELECT * FROM audiences WHERE id = ?";
	private final static String INSERT_AUDIENCE = "INSERT INTO audiences(room, capacity, cathedra_id) VALUES(?, ?, ?)";
	private final static String UPDATE_AUDIENCE = "UPDATE audiences SET room=?, capacity=?, cathedra_id=? WHERE id=?";
	private final static String DELETE_AUDIENCE = "DELETE FROM audiences WHERE id = ?";

	private final JdbcTemplate jdbcTemplate;
	private AudienceRowMapper rowMapper;

	public JdbcAudienceDao(JdbcTemplate jdbcTemplate, AudienceRowMapper rowMapper) {
		this.jdbcTemplate = jdbcTemplate;
		this.rowMapper = rowMapper;
	}

	@Override
	public List<Audience> findAll() {
		return jdbcTemplate.query(SELECT_ALL, rowMapper);
	}

	@Override
	public Audience findById(int id) {
		return jdbcTemplate.queryForObject(SELECT_BY_ID, rowMapper, id);
	}

	@Override
	public void save(Audience audience) {
		if (audience.getId() == 0) {
			KeyHolder keyHolder = new GeneratedKeyHolder();
			jdbcTemplate.update(connection -> {
				PreparedStatement statement = connection.prepareStatement(INSERT_AUDIENCE,
						Statement.RETURN_GENERATED_KEYS);
				statement.setInt(1, audience.getRoom());
				statement.setInt(2, audience.getCapacity());
				statement.setInt(3, audience.getCathedra().getId());
				return statement;
			}, keyHolder);
			audience.setId((int) keyHolder.getKeyList().get(0).get("id"));
		} else {
			jdbcTemplate.update(UPDATE_AUDIENCE, audience.getRoom(), audience.getCapacity(),
					audience.getCathedra().getId(), audience.getId());
		}

	}

	@Override
	public void deleteById(int id) {
		jdbcTemplate.update(DELETE_AUDIENCE, id);
	}

}
