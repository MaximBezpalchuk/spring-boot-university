package com.foxminded.university.dao.jdbc;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.foxminded.university.dao.LectureDao;
import com.foxminded.university.dao.jdbc.mapper.LectureRowMapper;
import com.foxminded.university.model.Audience;
import com.foxminded.university.model.Group;
import com.foxminded.university.model.Lecture;
import com.foxminded.university.model.LectureTime;
import com.foxminded.university.model.Teacher;

@Component
public class JdbcLectureDao implements LectureDao {

	private final static String SELECT_ALL = "SELECT * FROM lectures";
	private final static String SELECT_BY_ID = "SELECT * FROM lectures WHERE id = ?";
	private final static String INSERT_LECTURE = "INSERT INTO lectures(cathedra_id, subject_id, date, lecture_time_id, audience_id, teacher_id) VALUES(?, ?, ?, ?, ?, ?)";
	private final static String UPDATE_LECTURE = "UPDATE lectures SET cathedra_id=?, subject_id=?, date=?, lecture_time_id=?, audience_id=?, teacher_id=? WHERE id=?";
	private final static String DELETE_LECTURE = "DELETE FROM lectures WHERE id = ?";
	private final static String INSERT_GROUP = "INSERT INTO lectures_groups(group_id, lecture_id) VALUES (?,?)";
	private final static String DELETE_GROUP = "DELETE FROM lectures_groups WHERE group_id = ? AND lecture_id = ?";
	private final static String SELECT_BY_AUDIENCE_DATE_LECTURE_TIME = "SELECT * FROM lectures WHERE audience_id = ? AND date = ? AND lecture_time_id = ?";
	private final static String SELECT_BY_TEACHER_ID_DATE_AND_LECTURE_TIME_ID = "SELECT * FROM lectures WHERE teacher_id = ? AND date = ? AND lecture_time_id = ?";

	private final JdbcTemplate jdbcTemplate;
	private LectureRowMapper rowMapper;

	public JdbcLectureDao(JdbcTemplate jdbcTemplate, LectureRowMapper rowMapper) {
		this.jdbcTemplate = jdbcTemplate;
		this.rowMapper = rowMapper;
	}

	@Override
	public List<Lecture> findAll() {
		return jdbcTemplate.query(SELECT_ALL, rowMapper);
	}

	@Override
	public Lecture findById(int id) {
		return jdbcTemplate.queryForObject(SELECT_BY_ID, rowMapper, id);
	}

	@Override
	@Transactional
	public void save(Lecture lecture) {
		if (lecture.getId() == 0) {
			KeyHolder keyHolder = new GeneratedKeyHolder();
			jdbcTemplate.update(connection -> {
				PreparedStatement statement = connection.prepareStatement(INSERT_LECTURE,
						Statement.RETURN_GENERATED_KEYS);
				statement.setInt(1, lecture.getCathedra().getId());
				statement.setInt(2, lecture.getSubject().getId());
				statement.setObject(3, lecture.getDate());
				statement.setInt(4, lecture.getTime().getId());
				statement.setInt(5, lecture.getAudience().getId());
				statement.setInt(6, lecture.getTeacher().getId());
				return statement;
			}, keyHolder);
			lecture.setId((int) keyHolder.getKeyList().get(0).get("id"));
		} else {
			jdbcTemplate.update(UPDATE_LECTURE, lecture.getCathedra().getId(), lecture.getSubject().getId(),
					lecture.getDate(), lecture.getTime().getId(), lecture.getAudience().getId(),
					lecture.getTeacher().getId(), lecture.getId());
		}
		insertGroup(lecture);
		deleteGroup(lecture);
	}

	@Override
	public void deleteById(int id) {
		jdbcTemplate.update(DELETE_LECTURE, id);
	}

	private void insertGroup(Lecture lecture) {
		for (Group group : lecture.getGroups().stream()
				.filter(group -> !findById(lecture.getId()).getGroups().contains(group)).collect(Collectors.toList())) {
			jdbcTemplate.update(INSERT_GROUP, group.getId(), lecture.getId());
		}
	}

	private void deleteGroup(Lecture lecture) {
		for (Group group : findById(lecture.getId()).getGroups().stream()
				.filter(group -> !lecture.getGroups().contains(group)).collect(Collectors.toList())) {
			jdbcTemplate.update(DELETE_GROUP, group.getId(), lecture.getId());
		}
	}

	@Override
	public Lecture findByAudienceDateAndLectureTime(Audience audience, LocalDate date, LectureTime lectureTime) {
		return jdbcTemplate.queryForObject(SELECT_BY_AUDIENCE_DATE_LECTURE_TIME, rowMapper, audience.getId(), date,
				lectureTime.getId());
	}

	@Override
	public List<Lecture> findLecturesByTeacherDateAndTime(Teacher teacher, LocalDate date, LectureTime time) {
		return jdbcTemplate.query(SELECT_BY_TEACHER_ID_DATE_AND_LECTURE_TIME_ID, rowMapper, teacher.getId(), date,
				time.getId());
	}
}
