package com.foxminded.university.dao.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.jdbc.core.RowMapper;

import com.foxminded.university.dao.AudienceDao;
import com.foxminded.university.dao.CathedraDao;
import com.foxminded.university.dao.GroupDao;
import com.foxminded.university.dao.LectureTimeDao;
import com.foxminded.university.model.Audience;
import com.foxminded.university.model.Cathedra;
import com.foxminded.university.model.Group;
import com.foxminded.university.model.Lecture;
import com.foxminded.university.model.LectureTime;
import com.foxminded.university.model.Subject;
import com.foxminded.university.model.Teacher;

public class LectureRowMapper implements RowMapper<Lecture> {

	private CathedraDao cathedraDao;
	private GroupDao groupDao;
	private AudienceDao audienceDao;
	private LectureTimeDao lectureTimeDao;

	public LectureRowMapper(CathedraDao cathedraDao, GroupDao groupDao, AudienceDao audienceDao, LectureTimeDao lectureTimeDao) {
		this.cathedraDao = cathedraDao;
		this.groupDao = groupDao;
		this.audienceDao = audienceDao;
		this.lectureTimeDao = lectureTimeDao;
	}

	@Override
	public Lecture mapRow(ResultSet resultSet, int rowNum) throws SQLException {

		//private int id;
		resultSet.getInt("id")
		//private Cathedra cathedra;
		cathedraDao.findById(resultSet.getInt("cathedra_id"))
		//private List<Group> groups = new ArrayList<>(); - need to realize method
		groupDao.findByLecture(resultSet.getInt("id"))
		//private Teacher teacher; //need teacherDao
		
		//private Audience audience;
		audienceDao.findById(resultSet.getInt("audience_id"))
		//private LocalDate date;
		resultSet.getDate("date").getTime()).toLocalDate()
		//private Subject subject; //need subjectDao
		
		//private LectureTime time;
		lectureTimeDao.findById(resultSet.getInt("lecture_time_id"))
		
		
		
		//TODO: add inserts
		Lecture lecture = new Lecture();
		lecture.setId(resultSet.getInt("id"));
		return lecture;
	}
}
