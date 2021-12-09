package com.foxminded.university.dao.jdbc.mapper;

import com.foxminded.university.dao.jdbc.JdbcCathedraDao;
import com.foxminded.university.model.Group;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class GroupRowMapper implements RowMapper<Group> {

    private final JdbcCathedraDao cathedraDao;

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
