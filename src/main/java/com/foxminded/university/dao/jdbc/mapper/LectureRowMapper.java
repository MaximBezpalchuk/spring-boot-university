package com.foxminded.university.dao.jdbc.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import com.foxminded.university.dao.jdbc.JdbcAudienceDao;
import com.foxminded.university.dao.jdbc.JdbcCathedraDao;
import com.foxminded.university.dao.jdbc.JdbcGroupDao;
import com.foxminded.university.dao.jdbc.JdbcLectureTimeDao;
import com.foxminded.university.dao.jdbc.JdbcSubjectDao;
import com.foxminded.university.dao.jdbc.JdbcTeacherDao;
import com.foxminded.university.model.Group;
import com.foxminded.university.model.Lecture;

@Component
public class LectureRowMapper implements RowMapper<Lecture> {

	private JdbcCathedraDao cathedraDao;
	private JdbcGroupDao groupDao;
	private JdbcAudienceDao audienceDao;
	private JdbcLectureTimeDao lectureTimeDao;
	private JdbcTeacherDao teacherDao;
	private JdbcSubjectDao subjectDao;

	public LectureRowMapper(JdbcCathedraDao cathedraDao, JdbcGroupDao groupDao, JdbcAudienceDao audienceDao,
			JdbcLectureTimeDao lectureTimeDao, JdbcTeacherDao teacherDao, JdbcSubjectDao subjectDao) {
		this.cathedraDao = cathedraDao;
		this.groupDao = groupDao;
		this.audienceDao = audienceDao;
		this.lectureTimeDao = lectureTimeDao;
		this.teacherDao = teacherDao;
		this.subjectDao = subjectDao;
	}

	@Override
	public Lecture mapRow(ResultSet resultSet, int rowNum) throws SQLException {

		Lecture lecture = Lecture.builder().id(resultSet.getInt("id"))
				.cathedra(cathedraDao.findById(resultSet.getInt("cathedra_id")))
				.subject(subjectDao.findById(resultSet.getInt("subject_id")))
				.date(resultSet.getObject("date", LocalDate.class))
				.time(lectureTimeDao.findById(resultSet.getInt("lecture_time_id")))
				.audience(audienceDao.findById(resultSet.getInt("audience_id")))
				.teacher(teacherDao.findById(resultSet.getInt("teacher_id"))).build();
		List<Group> groups = groupDao.findByLectureId(lecture.getId());
		lecture.setGroups(groups);
		return lecture;
	}
}
