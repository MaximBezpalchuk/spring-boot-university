package com.foxminded.university.dao.jdbc.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import com.foxminded.university.dao.jdbc.JdbcCathedraDao;
import com.foxminded.university.model.Group;

@Component
public class GroupRowMapper implements RowMapper<Group> {

	private JdbcCathedraDao cathedraDao;

	public GroupRowMapper(JdbcCathedraDao cathedraDao) {
		this.cathedraDao = cathedraDao;
	}

	@Override
	public Group mapRow(ResultSet resultSet, int rowNum) throws SQLException {
		Group group = Group.builder()
				.id(resultSet.getInt("id"))
				.name(resultSet.getString("name"))
				.build();
		cathedraDao.findById(resultSet.getInt("cathedra_id")).ifPresent(group::setCathedra);

		return group;
	}
}
