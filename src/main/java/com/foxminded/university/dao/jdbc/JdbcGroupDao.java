package com.foxminded.university.dao.jdbc;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import com.foxminded.university.dao.GroupDao;
import com.foxminded.university.dao.jdbc.mapper.GroupRowMapper;
import com.foxminded.university.model.Group;

@Component
public class JdbcGroupDao implements GroupDao {

	private final static String SELECT_ALL = "SELECT * FROM groups";
	private final static String SELECT_BY_ID = "SELECT * FROM groups WHERE id = ?";
	private final static String INSERT_GROUP = "INSERT INTO groups(name, cathedra_id) VALUES(?, ?)";
	private final static String UPDATE_GROUP = "UPDATE groups SET name=?, cathedra_id=? WHERE id=?";
	private final static String DELETE_GROUP = "DELETE FROM groups WHERE id = ?";
	private final static String SELECT_BY_LECTURE_ID = "SELECT * FROM groups WHERE id IN (SELECT group_id FROM lectures_groups WHERE lecture_id =?)";
	private final static String SELECT_BY_NAME = "SELECT * FROM groups WHERE name = ?";

	private final JdbcTemplate jdbcTemplate;
	private GroupRowMapper rowMapper;

	public JdbcGroupDao(JdbcTemplate jdbcTemplate, GroupRowMapper rowMapper) {
		this.jdbcTemplate = jdbcTemplate;
		this.rowMapper = rowMapper;
	}

	@Override
	public List<Group> findAll() {
		return jdbcTemplate.query(SELECT_ALL, rowMapper);
	}

	@Override
	public Group findById(int id) {
		try {
			return jdbcTemplate.queryForObject(SELECT_BY_ID, rowMapper, id);
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	@Override
	public void save(Group group) {
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
		} else {
			jdbcTemplate.update(UPDATE_GROUP, group.getName(), group.getCathedra().getId(), group.getId());
		}

	}

	@Override
	public void deleteById(int id) {
		jdbcTemplate.update(DELETE_GROUP, id);
	}

	@Override
	public List<Group> findByLectureId(int id) {
		return jdbcTemplate.query(SELECT_BY_LECTURE_ID, rowMapper, id);
	}

	@Override
	public Group findByName(String name) {
		try {
			return jdbcTemplate.queryForObject(SELECT_BY_NAME, rowMapper, name);
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}
}
