package com.foxminded.university.dao.jdbc;

import com.foxminded.university.dao.StudentDao;
import com.foxminded.university.dao.jdbc.mapper.StudentRowMapper;
import com.foxminded.university.model.Group;
import com.foxminded.university.model.Student;
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

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Component
public class JdbcStudentDao implements StudentDao {

    private static final Logger logger = LoggerFactory.getLogger(JdbcStudentDao.class);

    private static final String SELECT_ALL = "SELECT * FROM students";
    private static final String COUNT_ALL = "SELECT COUNT(*) FROM students";
    private static final String SELECT_BY_PAGE = "SELECT * FROM students LIMIT ? OFFSET ?";
    private static final String SELECT_BY_ID = "SELECT * FROM students WHERE id = ?";
    private static final String INSERT_STUDENT = "INSERT INTO students(first_name, last_name, phone, address, email, gender, postal_code, education, birth_date, group_id) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    private static final String UPDATE_STUDENT = "UPDATE students SET first_name=?, last_name=?, phone=?, address=?, email=?, gender=?, postal_code=?, education=?, birth_date=?, group_id=? WHERE id=?";
    private static final String DELETE_STUDENT = "DELETE FROM students WHERE id = ?";
    private static final String SELECT_BY_FULL_NAME_AND_BIRTHDAY = "SELECT * FROM students WHERE first_name = ? AND last_name = ? AND birth_date = ?";
    private static final String SELECT_BY_GROUP_ID = "SELECT * FROM students WHERE group_id = ?";

    private final JdbcTemplate jdbcTemplate;
    private final StudentRowMapper rowMapper;

    public JdbcStudentDao(JdbcTemplate jdbcTemplate, StudentRowMapper rowMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.rowMapper = rowMapper;
    }

    @Override
    public List<Student> findAll() {
        logger.debug("Find all students");
        return jdbcTemplate.query(SELECT_ALL, rowMapper);
    }

    @Override
    public Page<Student> findPaginatedStudents(Pageable pageable) {
        logger.debug("Find all students with pageSize:{} and offset:{}", pageable.getPageSize(), pageable.getOffset());
        int total = jdbcTemplate.queryForObject(COUNT_ALL, Integer.class);
        List<Student> students = jdbcTemplate.query(SELECT_BY_PAGE, rowMapper, pageable.getPageSize(),
            pageable.getOffset());

        return new PageImpl<>(students, pageable, total);
    }

    @Override
    public Optional<Student> findById(int id) {
        logger.debug("Find student by id: {}", id);
        try {
            return Optional.of(jdbcTemplate.queryForObject(SELECT_BY_ID, rowMapper, id));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public void save(Student student) {
        logger.debug("Save student {}", student);
        if (student.getId() == 0) {
            KeyHolder keyHolder = new GeneratedKeyHolder();
            Group group = student.getGroup();
            jdbcTemplate.update(connection -> {
                PreparedStatement statement = connection.prepareStatement(INSERT_STUDENT,
                    Statement.RETURN_GENERATED_KEYS);
                statement.setString(1, student.getFirstName());
                statement.setString(2, student.getLastName());
                statement.setString(3, student.getPhone());
                statement.setString(4, student.getAddress());
                statement.setString(5, student.getEmail());
                statement.setString(6, student.getGender());
                statement.setString(7, student.getPostalCode());
                statement.setString(8, student.getEducation());
                statement.setObject(9, student.getBirthDate());
                if (group != null && group.getId() != 0) {
                    statement.setInt(10, student.getGroup().getId());
                } else {
                    statement.setObject(10, null);
                }
                return statement;
            }, keyHolder);
            student.setId((int) keyHolder.getKeyList().get(0).get("id"));
            logger.debug("New studetnt created with id: {}", student.getId());
        } else {

            Group group = student.getGroup();
            jdbcTemplate.update(connection -> {
                PreparedStatement statement = connection.prepareStatement(UPDATE_STUDENT);
                statement.setString(1, student.getFirstName());
                statement.setString(2, student.getLastName());
                statement.setString(3, student.getPhone());
                statement.setString(4, student.getAddress());
                statement.setString(5, student.getEmail());
                statement.setString(6, student.getGender());
                statement.setString(7, student.getPostalCode());
                statement.setString(8, student.getEducation());
                statement.setObject(9, student.getBirthDate());
                if (group != null && group.getId() != 0) {
                    statement.setInt(10, student.getGroup().getId());
                } else {
                    statement.setObject(10, null);
                }
                statement.setInt(11, student.getId());
                return statement;
            });
            logger.debug("Student with id {} was updated", student.getId());
        }
    }

    @Override
    public void deleteById(int id) {
        jdbcTemplate.update(DELETE_STUDENT, id);
        logger.debug("Student with id {} was deleted", id);
    }

    @Override
    public Optional<Student> findByFullNameAndBirthDate(String firstName, String lastName, LocalDate birthDate) {
        logger.debug("Find student with first name: {}, last name: {} and birthDate {}", firstName, lastName,
            birthDate);
        try {
            return Optional.of(jdbcTemplate.queryForObject(SELECT_BY_FULL_NAME_AND_BIRTHDAY, rowMapper, firstName,
                lastName, birthDate));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<Student> findByGroupId(int id) {
        logger.debug("Find students with group id {}", id);
        return jdbcTemplate.query(SELECT_BY_GROUP_ID, rowMapper, id);
    }
}
