package com.foxminded.university.dao.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import com.foxminded.university.dao.CathedraDao;
import com.foxminded.university.dao.SubjectDao;
import com.foxminded.university.model.Degree;
import com.foxminded.university.model.Gender;
import com.foxminded.university.model.Subject;
import com.foxminded.university.model.Teacher;

@Component
public class TeacherRowMapper implements RowMapper<Teacher> {

	private CathedraDao cathedraDao;
	private SubjectDao subjectDao;

	public TeacherRowMapper(CathedraDao cathedraDao, SubjectDao subjectDao) {
		this.subjectDao = subjectDao;
		this.cathedraDao = cathedraDao;
	}

	@Override
	public Teacher mapRow(ResultSet resultSet, int rowNum) throws SQLException {

		Teacher teacher = new Teacher.Builder().setFirstName(resultSet.getString("first_name"))
				.setLastName(resultSet.getString("last_name")).setPhone(resultSet.getString("phone"))
				.setAddress(resultSet.getString("address")).setEmail(resultSet.getString("email"))
				.setGender(Gender.valueOf(resultSet.getString("gender")))
				.setPostalCode(resultSet.getString("postal_code")).setEducation(resultSet.getString("education"))
				.setBirthDate(resultSet.getObject("birth_date", LocalDate.class))
				.setCathedra(cathedraDao.findById(resultSet.getInt("cathedra_id")))
				.setDegree(Degree.valueOf(resultSet.getString("degree"))).setId(resultSet.getInt("id"))
				.build();
		List<Subject> subjects = subjectDao.findByTeacherId(teacher.getId());
		if (!subjects.isEmpty()) {
			teacher.setSubjects(subjects);
		}

		return teacher;
	}

}
