package com.foxminded.university.dao.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import com.foxminded.university.dao.CathedraDao;
import com.foxminded.university.dao.TeacherDao;
import com.foxminded.university.model.Subject;

@Component
public class SubjectRowMapper implements RowMapper<Subject> {

	private CathedraDao cathedraDao;
	TeacherDao teacherDao;

	public SubjectRowMapper(CathedraDao cathedraDao, TeacherDao teacherDao) {
		this.cathedraDao = cathedraDao;
		this.teacherDao = teacherDao;
	}

	@Override
	public Subject mapRow(ResultSet resultSet, int rowNum) throws SQLException {
		Subject subject = new Subject(cathedraDao.findById(resultSet.getInt("cathedra_id")),
				resultSet.getString("name"), resultSet.getString("description"));
		subject.setTeachers(teacherDao.findBySubjectId(resultSet.getInt("id")));
		subject.setId(resultSet.getInt("id"));
		return subject;
	}

}
