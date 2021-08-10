package com.foxminded.university.dao.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import com.foxminded.university.dao.CathedraDao;
import com.foxminded.university.model.Group;

@Component
public class GroupRowMapper implements RowMapper<Group> {

	private CathedraDao cathedraDao;

	public GroupRowMapper(CathedraDao cathedraDao) {
		this.cathedraDao = cathedraDao;
	}

	@Override
	public Group mapRow(ResultSet resultSet, int rowNum) throws SQLException {
		return new Group.Builder(resultSet.getString("name"), cathedraDao.findById(resultSet.getInt("cathedra_id")))
				.setId(resultSet.getInt("id")).build();
	}
}
