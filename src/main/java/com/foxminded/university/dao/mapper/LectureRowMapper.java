package com.foxminded.university.dao.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import com.foxminded.university.dao.AudienceDao;
import com.foxminded.university.dao.CathedraDao;
import com.foxminded.university.dao.GroupDao;
import com.foxminded.university.dao.LectureTimeDao;
import com.foxminded.university.dao.SubjectDao;
import com.foxminded.university.dao.TeacherDao;
import com.foxminded.university.model.Lecture;

@Component
public class LectureRowMapper implements RowMapper<Lecture> {

	private CathedraDao cathedraDao;
	private GroupDao groupDao;
	private AudienceDao audienceDao;
	private LectureTimeDao lectureTimeDao;
	private TeacherDao teacherDao;
	private SubjectDao subjectDao;

	public LectureRowMapper(CathedraDao cathedraDao, GroupDao groupDao, AudienceDao audienceDao,
			LectureTimeDao lectureTimeDao, TeacherDao teacherDao, SubjectDao subjectDao) {
		this.cathedraDao = cathedraDao;
		this.groupDao = groupDao;
		this.audienceDao = audienceDao;
		this.lectureTimeDao = lectureTimeDao;
		this.teacherDao = teacherDao;
		this.subjectDao = subjectDao;
	}

	@Override
	public Lecture mapRow(ResultSet resultSet, int rowNum) throws SQLException {

		Lecture lecture = new Lecture(cathedraDao.findById(resultSet.getInt("cathedra_id")),
				subjectDao.findById(resultSet.getInt("subject_id")),
				new java.sql.Date(resultSet.getDate("date").getTime()).toLocalDate(),
				lectureTimeDao.findById(resultSet.getInt("lecture_time_id")),
				audienceDao.findById(resultSet.getInt("audience_id")), teacherDao.findById(resultSet.getInt("id")));

		lecture.setGroups(groupDao.findByLecture(resultSet.getInt("id")));

		lecture.setId(resultSet.getInt("id"));
		return lecture;
	}
}
