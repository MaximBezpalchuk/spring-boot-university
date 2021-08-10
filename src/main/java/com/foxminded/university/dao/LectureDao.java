package com.foxminded.university.dao;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import com.foxminded.university.dao.mapper.LectureRowMapper;
import com.foxminded.university.model.Group;
import com.foxminded.university.model.Lecture;

@Component
public class LectureDao {

	private final static String SELECT_ALL = "SELECT * FROM lectures";
	private final static String SELECT_BY_ID = "SELECT * FROM lectures WHERE id = ?";
	private final static String INSERT_LECTURE = "INSERT INTO lectures(cathedra_id, subject_id, date, lecture_time_id, audience_id, teacher_id) VALUES(?, ?, ?, ?, ?, ?)";
	private final static String UPDATE_LECTURE = "UPDATE lectures SET cathedra_id=?, subject_id=?, date=?, lecture_time_id=?, audience_id=?, teacher_id=? WHERE id=?";
	private final static String DELETE_LECTURE = "DELETE FROM lectures WHERE id = ?";
	private final static String INSERT_GROUPS = "INSERT INTO lectures_groups(lecture_id, group_id) VALUES(?, ?)";

	private final JdbcTemplate jdbcTemplate;
	private LectureRowMapper rowMapper;

	public LectureDao(JdbcTemplate jdbcTemplate, LectureRowMapper rowMapper) {
		this.jdbcTemplate = jdbcTemplate;
		this.rowMapper = rowMapper;
	}

	public List<Lecture> findAll() {
		return jdbcTemplate.query(SELECT_ALL, rowMapper);
	}

	public Lecture findById(int id) {
		return jdbcTemplate.queryForObject(SELECT_BY_ID, rowMapper, id);
	}

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

	}

	public void deleteById(int id) {
		jdbcTemplate.update(DELETE_LECTURE, id);
	}

	public void updateGroups(Lecture lecture, Group group) {
		jdbcTemplate.update(INSERT_GROUPS, lecture.getId(), group.getId());
	}

}
