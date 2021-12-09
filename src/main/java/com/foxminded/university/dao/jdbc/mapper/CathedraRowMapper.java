package com.foxminded.university.dao.jdbc.mapper;

import com.foxminded.university.model.Cathedra;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class CathedraRowMapper implements RowMapper<Cathedra> {

    @Override
    public Cathedra mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        return Cathedra.builder()
                .id(resultSet.getInt("id"))
                .name(resultSet.getString("name"))
                .build();
    }
}
