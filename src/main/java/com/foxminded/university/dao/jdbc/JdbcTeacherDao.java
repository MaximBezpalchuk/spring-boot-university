package com.foxminded.university.dao.jdbc;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.List;
import java.util.function.Predicate;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.foxminded.university.dao.TeacherDao;
import com.foxminded.university.dao.jdbc.mapper.TeacherRowMapper;
import com.foxminded.university.model.Teacher;

@Component
public class JdbcTeacherDao implements TeacherDao {

	private final static String SELECT_ALL = "SELECT * FROM teachers";
	private final static String SELECT_BY_ID = "SELECT * FROM teachers WHERE id = ?";
	private final static String INSERT_TEACHER = "INSERT INTO teachers(id, first_name, last_name, phone, address, email, gender, postal_code, education, birth_date, cathedra_id,  degree) VALUES(DEFAULT, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
	private final static String UPDATE_TEACHER = "UPDATE teachers SET first_name=?, last_name=?, phone=?, address=?, email=?, gender=?, postal_code=?, education=?, birth_date=?, cathedra_id=?, degree=? WHERE id=?";
	private final static String DELETE_TEACHER = "DELETE FROM teachers WHERE id = ?";
	private final static String INSERT_SUBJECT = "INSERT INTO subjects_teachers(subject_id, teacher_id) VALUES (?,?)";
	private final static String DELETE_SUBJECT = "DELETE FROM subjects_teachers WHERE subject_id = ? AND teacher_id = ?";
	private final static String SELECT_BY_FULL_NAME_AND_BIRTHDAY = "SELECT * FROM teachers WHERE first_name = ? AND last_name = ? AND birth_date = ?";

	private final JdbcTemplate jdbcTemplate;
	private TeacherRowMapper rowMapper;

	public JdbcTeacherDao(JdbcTemplate jdbcTemplate, TeacherRowMapper rowMapper) {
		this.jdbcTemplate = jdbcTemplate;
		this.rowMapper = rowMapper;
	}

	@Override
	public List<Teacher> findAll() {
		return jdbcTemplate.query(SELECT_ALL, rowMapper);
	}

	@Override
	public Teacher findById(int id) {
		try {
			return jdbcTemplate.queryForObject(SELECT_BY_ID, rowMapper, id);
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	@Override
	@Transactional
	public void save(Teacher teacher) {
		Teacher teacherOld = Teacher.builder().build();
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
			teacher.getSubjects().stream()
					.forEach(subject -> jdbcTemplate.update(INSERT_SUBJECT, subject.getId(), teacher.getId()));
		} else {
			jdbcTemplate.update(UPDATE_TEACHER, teacher.getFirstName(), teacher.getLastName(), teacher.getPhone(),
					teacher.getAddress(), teacher.getEmail(), teacher.getGender().toString(), teacher.getPostalCode(),
					teacher.getEducation(), teacher.getBirthDate(), teacher.getCathedra().getId(),
					teacher.getDegree().toString(), teacher.getId());

			teacherOld = findById(teacher.getId());
			updateSubjects(teacherOld, teacher);
			deleteSubjects(teacherOld, teacher);
		}
	}

	@Override
	public void deleteById(int id) {
		jdbcTemplate.update(DELETE_TEACHER, id);
	}

	private void updateSubjects(Teacher teacherOld, Teacher teacherNew) {
		teacherNew.getSubjects().stream().filter(Predicate.not(teacherOld.getSubjects()::contains))
				.forEach(subject -> jdbcTemplate.update(INSERT_SUBJECT, subject.getId(), teacherNew.getId()));
	}

	private void deleteSubjects(Teacher teacherOld, Teacher teacherNew) {
		teacherOld.getSubjects().stream().filter(Predicate.not(teacherNew.getSubjects()::contains))
				.forEach(subject -> jdbcTemplate.update(DELETE_SUBJECT, subject.getId(), teacherNew.getId()));
	}

	@Override
	public Teacher findByFullNameAndBirthDate(String firstName, String lastName, LocalDate birthDate) {
		try {
			return jdbcTemplate.queryForObject(SELECT_BY_FULL_NAME_AND_BIRTHDAY, rowMapper, firstName, lastName,
					birthDate);
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}
}
