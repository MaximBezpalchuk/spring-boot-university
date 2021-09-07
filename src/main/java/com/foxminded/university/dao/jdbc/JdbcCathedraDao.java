package com.foxminded.university.dao.jdbc;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import com.foxminded.university.dao.CathedraDao;
import com.foxminded.university.dao.jdbc.mapper.CathedraRowMapper;
import com.foxminded.university.model.Cathedra;

@Component
public class JdbcCathedraDao implements CathedraDao {

	private final static String SELECT_ALL = "SELECT * FROM cathedras";
	private final static String SELECT_BY_ID = "SELECT * FROM cathedras WHERE id = ?";
	private final static String INSERT_CATHEDRA = "INSERT INTO cathedras(name) VALUES(?)";
	private final static String UPDATE_CATHEDRA = "UPDATE cathedras SET name=? WHERE id=?";
	private final static String DELETE_CATHEDRA = "DELETE FROM cathedras WHERE id = ?";
	private final static String SELECT_BY_NAME = "SELECT * FROM cathedras WHERE name = ?";

	private final JdbcTemplate jdbcTemplate;
	private CathedraRowMapper rowMapper;

	public JdbcCathedraDao(JdbcTemplate jdbcTemplate, CathedraRowMapper rowMapper) {
		this.jdbcTemplate = jdbcTemplate;
		this.rowMapper = rowMapper;
	}

	@Override
	public List<Cathedra> findAll() {
		return jdbcTemplate.query(SELECT_ALL, rowMapper);
	}

	@Override
	public Cathedra findById(int id) {
		return jdbcTemplate.queryForObject(SELECT_BY_ID, rowMapper, id);
	}

	@Override
	public void save(Cathedra cathedra) {
		if (cathedra.getId() == 0) {
			KeyHolder keyHolder = new GeneratedKeyHolder();
			jdbcTemplate.update(connection -> {
				PreparedStatement statement = connection.prepareStatement(INSERT_CATHEDRA,
						Statement.RETURN_GENERATED_KEYS);
				statement.setString(1, cathedra.getName());
				return statement;
			}, keyHolder);
			cathedra.setId((int) keyHolder.getKeyList().get(0).get("id"));
		} else {
			jdbcTemplate.update(UPDATE_CATHEDRA, cathedra.getName(), cathedra.getId());
		}

	}

	@Override
	public void deleteById(int id) {
		jdbcTemplate.update(DELETE_CATHEDRA, id);
	}
	
	@Override
	public Cathedra findByName(String name) {
		try {
			return jdbcTemplate.queryForObject(SELECT_BY_NAME, rowMapper, name);
		} catch (DataAccessException e) {
			return null;
		}
	}
}
