package com.foxminded.university.dao.jdbc;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import com.foxminded.university.dao.JdbcDao;
import com.foxminded.university.dao.mapper.LectureRowMapper;
import com.foxminded.university.model.Group;
import com.foxminded.university.model.Lecture;

@Component
public class JdbcLectureDao implements JdbcDao<Lecture> {

	private final static String SELECT_ALL = "SELECT * FROM lectures";
	private final static String SELECT_BY_ID = "SELECT * FROM lectures WHERE id = ?";
	private final static String INSERT_LECTURE = "INSERT INTO lectures(cathedra_id, subject_id, date, lecture_time_id, audience_id, teacher_id) VALUES(?, ?, ?, ?, ?, ?)";
	private final static String UPDATE_LECTURE = "UPDATE lectures SET cathedra_id=?, subject_id=?, date=?, lecture_time_id=?, audience_id=?, teacher_id=? WHERE id=?";
	private final static String DELETE_LECTURE = "DELETE FROM lectures WHERE id = ?";
	private final static String INSERT_GROUPS = "INSERT INTO lectures_groups(group_id,lecture_id) SELECT ?, ? WHERE NOT EXISTS(SELECT 1 FROM lectures_groups WHERE group_id = ? AND lecture_id = ?)";

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
			for (Group group : lecture.getGroups()) {
				jdbcTemplate.update(INSERT_GROUPS, group.getId(), lecture.getId(), group.getId(), lecture.getId());
			}
		} else {

			jdbcTemplate.update(UPDATE_LECTURE, lecture.getCathedra().getId(), lecture.getSubject().getId(),
					lecture.getDate(), lecture.getTime().getId(), lecture.getAudience().getId(),
					lecture.getTeacher().getId(), lecture.getId());
			List<Group> groups = lecture.getGroups();
			for (Group group : groups) {
				jdbcTemplate.update(INSERT_GROUPS, group.getId(), lecture.getId(), group.getId(), lecture.getId());
			}
			jdbcTemplate
					.update(String.format("DELETE FROM lectures_groups WHERE lecture_id = %s AND group_id NOT IN (%s)",
							lecture.getId(), groups.stream().map(group -> group.getId()).map(Object::toString)
									.collect(Collectors.joining(", "))));

		}

	}

	@Override
	public void deleteById(int id) {
		jdbcTemplate.update(DELETE_LECTURE, id);
	}
}
