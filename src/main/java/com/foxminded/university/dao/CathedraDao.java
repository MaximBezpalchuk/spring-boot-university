package com.foxminded.university.dao;

import java.sql.PreparedStatement;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import com.foxminded.university.dao.mapper.CathedraRowMapper;
import com.foxminded.university.model.Cathedra;

@Component
public class CathedraDao {

	private final static String SELECT_ALL = "SELECT * FROM cathedras";
	private final static String SELECT_BY_ID = "SELECT * FROM cathedras WHERE id = ?";
	private final static String INSERT_CATHEDRA = "INSERT INTO cathedra VALUES(?)";
	private final static String UPDATE_CATHEDRA = "UPDATE cathedra SET name=? WHERE id=?";
	private final static String DELETE_CATHEDRA = "DELETE FROM cathedra WHERE id = ?";

	private final JdbcTemplate jdbcTemplate;
	private CathedraRowMapper rowMapper;

	@Autowired
	public CathedraDao(JdbcTemplate jdbcTemplate, CathedraRowMapper rowMapper) {
		this.jdbcTemplate = jdbcTemplate;
		this.rowMapper = rowMapper;
	}

	public void create(Cathedra cathedra) {
		jdbcTemplate.update(INSERT_CATHEDRA, cathedra.getName());
	}

	public List<Cathedra> findAll() {
		return jdbcTemplate.query(SELECT_ALL, rowMapper);
	}

	@SuppressWarnings("deprecation")
	public Cathedra findById(int id) {
		return jdbcTemplate.query(SELECT_BY_ID, new Object[] { id }, rowMapper).stream().findAny().orElse(null);
	}

	public void update(Cathedra cathedra) {
		if (cathedra.getId() == 0) {
			KeyHolder keyHolder = new GeneratedKeyHolder();
			jdbcTemplate.update(connection -> {
				PreparedStatement statement = connection.prepareStatement(INSERT_CATHEDRA);
				statement.setString(1, cathedra.getName());
				return statement;
			}, keyHolder);
			cathedra.setId((int) keyHolder.getKey());
		} else {
			jdbcTemplate.update(UPDATE_CATHEDRA, cathedra.getName(), cathedra.getId());
		}

	}

	public void deleteById(int id) {
		jdbcTemplate.update(DELETE_CATHEDRA, id);
	}
}
