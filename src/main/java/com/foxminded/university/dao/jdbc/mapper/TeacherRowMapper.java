package com.foxminded.university.dao.jdbc.mapper;

import com.foxminded.university.dao.jdbc.JdbcCathedraDao;
import com.foxminded.university.dao.jdbc.JdbcSubjectDao;
import com.foxminded.university.model.Degree;
import com.foxminded.university.model.Gender;
import com.foxminded.university.model.Teacher;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

@Component
public class TeacherRowMapper implements RowMapper<Teacher> {

    private JdbcCathedraDao cathedraDao;
    private JdbcSubjectDao subjectDao;

    public TeacherRowMapper(JdbcCathedraDao cathedraDao, JdbcSubjectDao subjectDao) {
        this.subjectDao = subjectDao;
        this.cathedraDao = cathedraDao;
    }

    @Override
    public Teacher mapRow(ResultSet resultSet, int rowNum) throws SQLException {

        Teacher teacher = Teacher.builder()
                .firstName(resultSet.getString("first_name"))
                .lastName(resultSet.getString("last_name"))
                .address(resultSet.getString("address"))
                .gender(Gender.valueOf(resultSet.getString("gender")))
                .birthDate(resultSet.getObject("birth_date", LocalDate.class))
                .degree(Degree.valueOf(resultSet.getString("degree")))
                .phone(resultSet.getString("phone"))
                .email(resultSet.getString("email"))
                .postalCode(resultSet.getString("postal_code"))
                .education(resultSet.getString("education"))
                .id(resultSet.getInt("id"))
                .subjects(subjectDao.findByTeacherId(resultSet.getInt("id")))
                .build();
        cathedraDao.findById(resultSet.getInt("cathedra_id")).ifPresent(teacher::setCathedra);

        return teacher;
    }

}
