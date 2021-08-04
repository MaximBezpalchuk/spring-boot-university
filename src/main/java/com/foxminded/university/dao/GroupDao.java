package com.foxminded.university.dao;

import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import com.foxminded.university.dao.mapper.GroupRowMapper;
import com.foxminded.university.model.Group;

@Component
public class GroupDao {

	private final static String SELECT_ALL = "SELECT * FROM groups";
	private final static String SELECT_BY_ID = "SELECT * FROM groups WHERE id = ?";
	private final static String INSERT_GROUP = "INSERT INTO groups(name, cathedra_id) VALUES(?, ?)";
	private final static String UPDATE_GROUP = "UPDATE groups SET name=?, cathedra_id=? WHERE id=?";
	private final static String DELETE_GROUP = "DELETE FROM groups WHERE id = ?";

	private final JdbcTemplate jdbcTemplate;
	private GroupRowMapper rowMapper;

	@Autowired
	public GroupDao(JdbcTemplate jdbcTemplate, GroupRowMapper rowMapper) {
		this.jdbcTemplate = jdbcTemplate;
		this.rowMapper = rowMapper;
	}

	public void create(Group group) {
		jdbcTemplate.update(INSERT_GROUP, group.getName(), group.getCathedra());
	}

	public List<Group> findAll() {
		return jdbcTemplate.query(SELECT_ALL, rowMapper);
	}

	@SuppressWarnings("deprecation")
	public Group findById(int id) {
		return jdbcTemplate.query(SELECT_BY_ID, new Object[] { id }, rowMapper).stream().findAny().orElse(null);
	}

	public void update(Group group) {
		if (group.getId() == 0) {
			KeyHolder keyHolder = new GeneratedKeyHolder();
			jdbcTemplate.update(connection -> {
				PreparedStatement statement = connection.prepareStatement(INSERT_GROUP);
				statement.setString(1, group.getName());
				statement.setInt(2, group.getCathedra().getId());
				return statement;
			}, keyHolder);
			group.setId((int) keyHolder.getKey());
		} else {
			jdbcTemplate.update(UPDATE_GROUP, group.getName(), group.getCathedra().getId());
		}

	}

	public void deleteById(int id) {
		jdbcTemplate.update(DELETE_GROUP, id);
	}

	// TODO: realize
	public List<Group> findByLecture(int id) {
		return new ArrayList<Group>();
	}
}
