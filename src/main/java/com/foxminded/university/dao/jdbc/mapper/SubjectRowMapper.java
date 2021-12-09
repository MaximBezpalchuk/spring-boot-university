package com.foxminded.university.dao.jdbc.mapper;

import com.foxminded.university.dao.jdbc.JdbcCathedraDao;
import com.foxminded.university.model.Subject;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class SubjectRowMapper implements RowMapper<Subject> {

    private JdbcCathedraDao cathedraDao;

    public SubjectRowMapper(JdbcCathedraDao cathedraDao) {
        this.cathedraDao = cathedraDao;
    }

    @Override
    public Subject mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        Subject subject = Subject.builder()
                .id(resultSet.getInt("id"))
                .name(resultSet.getString("name"))
                .description(resultSet.getString("description"))
                .build();
        cathedraDao.findById(resultSet.getInt("cathedra_id")).ifPresent(subject::setCathedra);

        return subject;
    }
}
