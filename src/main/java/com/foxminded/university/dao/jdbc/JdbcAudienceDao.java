package com.foxminded.university.dao.jdbc;

import com.foxminded.university.dao.AudienceDao;
import com.foxminded.university.dao.jdbc.mapper.AudienceRowMapper;
import com.foxminded.university.model.Audience;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

@Component
public class JdbcAudienceDao implements AudienceDao {

    private static final Logger logger = LoggerFactory.getLogger(JdbcAudienceDao.class);

    private static final String SELECT_ALL = "SELECT * FROM audiences";
    private static final String SELECT_BY_ID = "SELECT * FROM audiences WHERE id = ?";
    private static final String INSERT_AUDIENCE = "INSERT INTO audiences(room, capacity, cathedra_id) VALUES(?, ?, ?)";
    private static final String UPDATE_AUDIENCE = "UPDATE audiences SET room=?, capacity=?, cathedra_id=? WHERE id=?";
    private static final String DELETE_AUDIENCE = "DELETE FROM audiences WHERE id = ?";
    private static final String SELECT_BY_ROOM = "SELECT * FROM audiences WHERE room = ?";

    private final JdbcTemplate jdbcTemplate;
    private final AudienceRowMapper rowMapper;

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
    public Optional<Audience> findById(int id) {
        logger.debug("Find audience by id: {}", id);
        try {
            return Optional.of(jdbcTemplate.queryForObject(SELECT_BY_ID, rowMapper, id));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public void save(Audience audience) {
        logger.debug("Save audience {}", audience);
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
    public Optional<Audience> findByRoom(int room) {
        logger.debug("Find audience by room number: {}", room);
        try {
            return Optional.of(jdbcTemplate.queryForObject(SELECT_BY_ROOM, rowMapper, room));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }
}
