package com.foxminded.university.dao.jdbc.mapper;

import com.foxminded.university.model.LectureTime;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalTime;

@Component
public class LectureTimeRowMapper implements RowMapper<LectureTime> {

    @Override
    public LectureTime mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        return LectureTime.builder()
            .id(resultSet.getInt("id"))
            .start(resultSet.getObject("start", LocalTime.class))
            .end(resultSet.getObject("finish", LocalTime.class))
            .build();
    }
}
