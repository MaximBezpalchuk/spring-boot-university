package com.foxminded.university.dao.jdbc;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.foxminded.university.dao.TeacherDao;
import com.foxminded.university.dao.jdbc.mapper.TeacherRowMapper;
import com.foxminded.university.exception.EntityNotFoundException;
import com.foxminded.university.model.LectureTime;
import com.foxminded.university.model.Subject;
import com.foxminded.university.model.Teacher;

@Component
public class JdbcTeacherDao implements TeacherDao {

	private static final Logger logger = LoggerFactory.getLogger(JdbcTeacherDao.class);

	private static final String SELECT_ALL = "SELECT * FROM teachers";
	private static final String COUNT_ALL = "SELECT COUNT(*) FROM teachers";
	private static final String SELECT_BY_PAGE = "SELECT * FROM teachers LIMIT ? OFFSET ?";
	private static final String SELECT_BY_ID = "SELECT * FROM teachers WHERE id = ?";
	private static final String INSERT_TEACHER = "INSERT INTO teachers(id, first_name, last_name, phone, address, email, gender, postal_code, education, birth_date, cathedra_id,  degree) VALUES(DEFAULT, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
	private static final String UPDATE_TEACHER = "UPDATE teachers SET first_name=?, last_name=?, phone=?, address=?, email=?, gender=?, postal_code=?, education=?, birth_date=?, cathedra_id=?, degree=? WHERE id=?";
	private static final String DELETE_TEACHER = "DELETE FROM teachers WHERE id = ?";
	private static final String INSERT_SUBJECT = "INSERT INTO subjects_teachers(subject_id, teacher_id) VALUES (?,?)";
	private static final String DELETE_SUBJECT = "DELETE FROM subjects_teachers WHERE subject_id = ? AND teacher_id = ?";
	private static final String SELECT_BY_FULL_NAME_AND_BIRTHDAY = "SELECT * FROM teachers WHERE first_name = ? AND last_name = ? AND birth_date = ?";
	private static final String SELECT_BY_FREE_OF_VACATIONS_DATE_AND_SUBJECT_WITH_CURRENT_TEACHER = "SELECT DISTINCT teachers.* FROM teachers LEFT JOIN subjects_teachers AS st ON teachers.id = st.teacher_id LEFT JOIN vacations AS vac ON teachers.id = vac.teacher_id WHERE st.subject_id = ? AND ((?::DATE NOT BETWEEN vac.start AND vac.finish) OR (vac.start IS NULL AND vac.finish IS NULL))AND NOT EXISTS (select id from lectures where lectures.teacher_id = teachers.id AND lectures.date = ?::DATE AND lectures.lecture_time_id = ?)";

	private final JdbcTemplate jdbcTemplate;
	private TeacherRowMapper rowMapper;

	public JdbcTeacherDao(JdbcTemplate jdbcTemplate, TeacherRowMapper rowMapper) {
		this.jdbcTemplate = jdbcTemplate;
		this.rowMapper = rowMapper;
	}

	@Override
	public List<Teacher> findAll() {
		logger.debug("Find all teachers");
		return jdbcTemplate.query(SELECT_ALL, rowMapper);
	}

	@Override
	public Page<Teacher> findPaginatedTeachers(Pageable pageable) {
		logger.debug("Find all teachers with pageSize:{} and offset:{}", pageable.getPageSize(), pageable.getOffset());
		int total = jdbcTemplate.queryForObject(COUNT_ALL, Integer.class);
		List<Teacher> teachers = jdbcTemplate.query(SELECT_BY_PAGE, rowMapper, pageable.getPageSize(),
				pageable.getOffset());

		return new PageImpl<>(teachers, pageable, total);
	}

	@Override
	public Optional<Teacher> findById(int id) {
		logger.debug("Find teacher by id: {}", id);
		try {
			return Optional.of(jdbcTemplate.queryForObject(SELECT_BY_ID, rowMapper, id));
		} catch (EmptyResultDataAccessException e) {
			return Optional.empty();
		}
	}

	@Override
	@Transactional
	public void save(Teacher teacher) {
		logger.debug("Save teacher {}", teacher);
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
			logger.debug("New teacher created with id: {}", teacher.getId());
		} else {
			jdbcTemplate.update(UPDATE_TEACHER, teacher.getFirstName(), teacher.getLastName(), teacher.getPhone(),
					teacher.getAddress(), teacher.getEmail(), teacher.getGender().toString(), teacher.getPostalCode(),
					teacher.getEducation(), teacher.getBirthDate(), teacher.getCathedra().getId(),
					teacher.getDegree().toString(), teacher.getId());

			Teacher teacherOld = findById(teacher.getId()).orElse(null);
			updateSubjects(teacherOld, teacher);
			deleteSubjects(teacherOld, teacher);
			logger.debug("Teacher with id {} was updated", teacher.getId());
		}
	}

	@Override
	public void deleteById(int id) {
		jdbcTemplate.update(DELETE_TEACHER, id);
		logger.debug("Teacher with id {} was deleted", id);
	}

	private void updateSubjects(Teacher teacherOld, Teacher teacherNew) {
		Predicate<Subject> subjPredicate = teacherOld.getSubjects()::contains;
		teacherNew.getSubjects().stream().filter(subjPredicate.negate()::test)
				.forEach(subject -> jdbcTemplate.update(INSERT_SUBJECT, subject.getId(), teacherNew.getId()));
		logger.debug("Update subjects in teacher with id {}", teacherNew.getId());
	}

	private void deleteSubjects(Teacher teacherOld, Teacher teacherNew) {
		Predicate<Subject> subjPredicate = teacherNew.getSubjects()::contains;
		teacherOld.getSubjects().stream().filter(subjPredicate.negate()::test)
				.forEach(subject -> jdbcTemplate.update(DELETE_SUBJECT, subject.getId(), teacherNew.getId()));
		logger.debug("Delete subjects in teacher with id {}", teacherNew.getId());
	}

	@Override
	public Optional<Teacher> findByFullNameAndBirthDate(String firstName, String lastName, LocalDate birthDate) {
		logger.debug("Find teacher by first name: {}, last name: {} and birth date: {}", firstName, lastName,
				birthDate);
		try {
			return Optional.of(jdbcTemplate.queryForObject(SELECT_BY_FULL_NAME_AND_BIRTHDAY, rowMapper, firstName,
					lastName, birthDate));
		} catch (EmptyResultDataAccessException e) {
			return Optional.empty();
		}
	}

	@Override
	public List<Teacher> findByFreeDateAndSubjectWithCurrentTeacher(LocalDate date, LectureTime time, Subject subject) {
		logger.debug(
				"Find teachers who havent lectures and vacation this period: {} at {} - {} and who can teach this subject: {}",
				date, time.getStart(), time.getEnd(), subject.getName());
		List<Teacher> teachers = jdbcTemplate.query(SELECT_BY_FREE_OF_VACATIONS_DATE_AND_SUBJECT_WITH_CURRENT_TEACHER,
				rowMapper, subject.getId(), date, date, time.getId());
		if (teachers.isEmpty()) {
			throw new EntityNotFoundException(
					"Can`t find teachers who havent lectures and vacation this period:" + date + " at "
							+ time.getStart() + " - " + time.getEnd() + " and who can teach this subject: "
							+ subject.getName());
		}

		return teachers;
	}
}
