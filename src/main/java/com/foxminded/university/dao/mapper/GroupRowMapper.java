//TODO: update with lists when add all dao classes
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
		Group group = new Group(resultSet.getString("name"), cathedraDao.findById(resultSet.getInt("cathedra")));
		group.setId(resultSet.getInt("id"));
		return group;
	}
}
