package com.foxminded.university.dao;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import com.foxminded.university.dao.mapper.TeacherRowMapper;
import com.foxminded.university.model.Subject;
import com.foxminded.university.model.Teacher;

@Component
public class TeacherDao {

	private final static String SELECT_ALL = "SELECT * FROM teachers";
	private final static String SELECT_BY_ID = "SELECT * FROM teachers WHERE id = ?";
	private final static String INSERT_TEACHER = "INSERT INTO teachers(id, first_name, last_name, phone, address, email, gender, postal_code, education, birth_date, cathedra_id,  degree) VALUES(DEFAULT, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
	private final static String UPDATE_TEACHER = "UPDATE teachers SET first_name=?, last_name=?, phone=?, address=?, email=?, gender=?, postal_code=?, education=?, birth_date=?, cathedra_id=?, degree=? WHERE id=?";
	private final static String DELETE_TEACHER = "DELETE FROM teachers WHERE id = ?";
	private final static String INSERT_SUBJECT = "INSERT INTO subjects_teachers(subject_id,teacher_id) SELECT ?, ? WHERE NOT EXISTS(SELECT 1 FROM subjects_teachers WHERE subject_id = ? AND teacher_id = ?)";

	private final JdbcTemplate jdbcTemplate;
	private TeacherRowMapper rowMapper;

	public TeacherDao(JdbcTemplate jdbcTemplate, TeacherRowMapper rowMapper) {
		this.jdbcTemplate = jdbcTemplate;
		this.rowMapper = rowMapper;
	}

	public List<Teacher> findAll() {
		return jdbcTemplate.query(SELECT_ALL, rowMapper);
	}

	public Teacher findById(int id) {
		return jdbcTemplate.queryForObject(SELECT_BY_ID, rowMapper, id);
	}

	public void save(Teacher teacher) {
		if (teacher.getId() == 0) {
			KeyHolder keyHolder = new GeneratedKeyHolder();
			jdbcTemplate.update(connection -> {
				PreparedStatement statement = connection.prepareStatement(INSERT_TEACHER,
						Statement.RETURN_GENERATED_KEYS);
				statement.setString(1, teacher.getFirstName());
				statement.setString(2, teacher.getLastName());
				statement.setString(3, teacher.getPhone());
				statement.setString(4, teacher.getAddress());
				statement.setString(5, teacher.getEmail());
				statement.setString(6, teacher.getGender().toString());
				statement.setString(7, teacher.getPostalCode());
				statement.setString(8, teacher.getEducation());
				statement.setObject(9, teacher.getBirthDate());
				statement.setInt(10, teacher.getCathedra().getId());
				statement.setString(11, teacher.getDegree().toString());
				return statement;
			}, keyHolder);
			teacher.setId((int) keyHolder.getKeyList().get(0).get("id"));
			for (Subject subject : teacher.getSubjects()) {
				jdbcTemplate.update(INSERT_SUBJECT, subject.getId(), teacher.getId(), subject.getId(), teacher.getId());
			}
		} else {
			jdbcTemplate.update(UPDATE_TEACHER, teacher.getFirstName(), teacher.getLastName(), teacher.getPhone(),
					teacher.getAddress(), teacher.getEmail(), teacher.getGender().toString(), teacher.getPostalCode(),
					teacher.getEducation(), teacher.getBirthDate(), teacher.getCathedra().getId(),
					teacher.getDegree().toString(), teacher.getId());

			for (Subject subject : teacher.getSubjects()) {
				jdbcTemplate.update(INSERT_SUBJECT, subject.getId(), teacher.getId(), subject.getId(), teacher.getId());
			}
		}

	}

	public void deleteById(int id) {
		jdbcTemplate.update(DELETE_TEACHER, id);
	}
}
