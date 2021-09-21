package com.foxminded.university.dao.jdbc;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import com.foxminded.university.dao.AudienceDao;
import com.foxminded.university.dao.jdbc.mapper.AudienceRowMapper;
import com.foxminded.university.model.Audience;

@Component
public class JdbcAudienceDao implements AudienceDao {
	
	private final static Logger logger = LoggerFactory.getLogger(JdbcAudienceDao.class);

	private final static String SELECT_ALL = "SELECT * FROM audiences";
	private final static String SELECT_BY_ID = "SELECT * FROM audiences WHERE id = ?";
	private final static String INSERT_AUDIENCE = "INSERT INTO audiences(room, capacity, cathedra_id) VALUES(?, ?, ?)";
	private final static String UPDATE_AUDIENCE = "UPDATE audiences SET room=?, capacity=?, cathedra_id=? WHERE id=?";
	private final static String DELETE_AUDIENCE = "DELETE FROM audiences WHERE id = ?";
	private final static String SELECT_BY_ROOM = "SELECT * FROM audiences WHERE room = ?";

	private final JdbcTemplate jdbcTemplate;
	private AudienceRowMapper rowMapper;

	public JdbcAudienceDao(JdbcTemplate jdbcTemplate, AudienceRowMapper rowMapper) {
		this.jdbcTemplate = jdbcTemplate;
		this.rowMapper = rowMapper;
	}

	@Override
	public List<Audience> findAll() {
		logger.debug("Find all audiences");
		return jdbcTemplate.query(SELECT_ALL, rowMapper);
	}

	@Override
	public Audience findById(int id) {
		logger.debug("Find audience by id: {}", id);
		try {
			return jdbcTemplate.queryForObject(SELECT_BY_ID, rowMapper, id);
		} catch (EmptyResultDataAccessException e) {
			//TODO: throw custom exception with custom message
			return null;
		}
	}

	@Override
	public void save(Audience audience) {
		logger.debug("Save audience");
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
			logger.debug("New audience created with id: {}", audience.getId());
		} else {
			jdbcTemplate.update(UPDATE_AUDIENCE, audience.getRoom(), audience.getCapacity(),
					audience.getCathedra().getId(), audience.getId());
			logger.debug("Audience with id {} was updated", audience.getId());
		}

	}

	@Override
	public void deleteById(int id) {
		jdbcTemplate.update(DELETE_AUDIENCE, id);
		logger.debug("Audience with id {} was deleted", id);
	}

	@Override
	public Audience findByRoom(int room) {
		logger.debug("Find audience by room number: {}", room);
		try {
			return jdbcTemplate.queryForObject(SELECT_BY_ROOM, rowMapper, room);
		} catch (EmptyResultDataAccessException e) {
			//TODO: throw custom exception with custom message
			return null;
		}
	}
}
