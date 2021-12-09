package com.foxminded.university.dao.jdbc;

import com.foxminded.university.dao.GroupDao;
import com.foxminded.university.dao.jdbc.mapper.GroupRowMapper;
import com.foxminded.university.model.Group;
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
public class JdbcGroupDao implements GroupDao {

    private static final Logger logger = LoggerFactory.getLogger(JdbcGroupDao.class);

    private static final String SELECT_ALL = "SELECT * FROM groups";
    private static final String SELECT_BY_ID = "SELECT * FROM groups WHERE id = ?";
    private static final String INSERT_GROUP = "INSERT INTO groups(name, cathedra_id) VALUES(?, ?)";
    private static final String UPDATE_GROUP = "UPDATE groups SET name=?, cathedra_id=? WHERE id=?";
    private static final String DELETE_GROUP = "DELETE FROM groups WHERE id = ?";
    private static final String SELECT_BY_LECTURE_ID = "SELECT * FROM groups WHERE id IN (SELECT group_id FROM lectures_groups WHERE lecture_id =?)";
    private static final String SELECT_BY_NAME = "SELECT * FROM groups WHERE name = ?";

    private final JdbcTemplate jdbcTemplate;
    private GroupRowMapper rowMapper;

    public JdbcGroupDao(JdbcTemplate jdbcTemplate, GroupRowMapper rowMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.rowMapper = rowMapper;
    }

    @Override
    public List<Group> findAll() {
        logger.debug("Find all groups");
        return jdbcTemplate.query(SELECT_ALL, rowMapper);
    }

    @Override
    public Optional<Group> findById(int id) {
        logger.debug("Find group by id: {}", id);
        try {
            return Optional.of(jdbcTemplate.queryForObject(SELECT_BY_ID, rowMapper, id));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public void save(Group group) {
        logger.debug("Save group {}", group);
        if (group.getId() == 0) {
            KeyHolder keyHolder = new GeneratedKeyHolder();
            jdbcTemplate.update(connection -> {
                PreparedStatement statement = connection.prepareStatement(INSERT_GROUP,
                        Statement.RETURN_GENERATED_KEYS);
                statement.setString(1, group.getName());
                statement.setInt(2, group.getCathedra().getId());
                return statement;
            }, keyHolder);
            group.setId((int) keyHolder.getKeyList().get(0).get("id"));
            logger.debug("New group created with id: {}", group.getId());
        } else {
            jdbcTemplate.update(UPDATE_GROUP, group.getName(), group.getCathedra().getId(), group.getId());
            logger.debug("Group with id {} was updated", group.getId());
        }

    }

    @Override
    public void deleteById(int id) {
        jdbcTemplate.update(DELETE_GROUP, id);
        logger.debug("Group with id {} was deleted", id);
    }

    @Override
    public List<Group> findByLectureId(int id) {
        logger.debug("Find groups with lecture id: {}", id);
        return jdbcTemplate.query(SELECT_BY_LECTURE_ID, rowMapper, id);
    }

    @Override
    public Optional<Group> findByName(String name) {
        logger.debug("Find group with name {}", name);
        try {
            return Optional.of(jdbcTemplate.queryForObject(SELECT_BY_NAME, rowMapper, name));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }
}
