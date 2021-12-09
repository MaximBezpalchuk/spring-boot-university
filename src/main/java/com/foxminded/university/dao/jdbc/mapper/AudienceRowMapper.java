package com.foxminded.university.dao.jdbc.mapper;

import com.foxminded.university.dao.jdbc.JdbcCathedraDao;
import com.foxminded.university.model.Audience;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class AudienceRowMapper implements RowMapper<Audience> {

    private JdbcCathedraDao cathedraDao;

    public AudienceRowMapper(JdbcCathedraDao cathedraDao) {
        this.cathedraDao = cathedraDao;
    }

    @Override
    public Audience mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        Audience audience = Audience.builder()
                .id(resultSet.getInt("id"))
                .room(resultSet.getInt("room"))
                .capacity(resultSet.getInt("capacity"))
                .build();
        cathedraDao.findById(resultSet.getInt("cathedra_id")).ifPresent(audience::setCathedra);

        return audience;
    }
}
