package com.foxminded.university.dao.jdbc.mapper;

import com.foxminded.university.dao.jdbc.JdbcCathedraDao;
import com.foxminded.university.model.Holiday;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

@Component
public class HolidayRowMapper implements RowMapper<Holiday> {

    private final JdbcCathedraDao cathedraDao;

    public HolidayRowMapper(JdbcCathedraDao cathedraDao) {
        this.cathedraDao = cathedraDao;
    }

    @Override
    public Holiday mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        Holiday holiday = Holiday.builder()
            .id(resultSet.getInt("id"))
            .name(resultSet.getString("name"))
            .date(resultSet.getObject("date", LocalDate.class))
            .build();
        cathedraDao.findById(resultSet.getInt("cathedra_id")).ifPresent(holiday::setCathedra);

        return holiday;
    }
}