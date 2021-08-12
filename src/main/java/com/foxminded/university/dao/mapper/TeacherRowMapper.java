package com.foxminded.university.dao.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import com.foxminded.university.dao.jdbc.JdbcCathedraDao;
import com.foxminded.university.dao.jdbc.JdbcSubjectDao;
import com.foxminded.university.model.Degree;
import com.foxminded.university.model.Gender;
import com.foxminded.university.model.Subject;
import com.foxminded.university.model.Teacher;

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

		Teacher teacher = Teacher
				.build(resultSet.getString("first_name"), resultSet.getString("last_name"),
						resultSet.getString("address"), Gender.valueOf(resultSet.getString("gender")),
						resultSet.getObject("birth_date", LocalDate.class),
						cathedraDao.findById(resultSet.getInt("cathedra_id")),
						Degree.valueOf(resultSet.getString("degree")))
				.setPhone(resultSet.getString("phone")).setEmail(resultSet.getString("email"))
				.setPostalCode(resultSet.getString("postal_code")).setEducation(resultSet.getString("education"))
				.setId(resultSet.getInt("id")).build();
		List<Subject> subjects = subjectDao.findByTeacherId(teacher.getId());
		if (!subjects.isEmpty()) {
			teacher.setSubjects(subjects);
		}

		return teacher;
	}

}
