package com.foxminded.university.dao.jdbc;

import com.foxminded.university.dao.LectureDao;
import com.foxminded.university.dao.jdbc.mapper.LectureRowMapper;
import com.foxminded.university.model.*;
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

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class JdbcLectureDao implements LectureDao {

    private static final Logger logger = LoggerFactory.getLogger(JdbcLectureDao.class);

    private static final String SELECT_ALL = "SELECT * FROM lectures";
    private static final String COUNT_ALL = "SELECT COUNT(*) FROM lectures";
    private static final String SELECT_BY_PAGE = "SELECT * FROM lectures LIMIT ? OFFSET ?";
    private static final String SELECT_BY_ID = "SELECT * FROM lectures WHERE id = ?";
    private static final String INSERT_LECTURE = "INSERT INTO lectures(cathedra_id, subject_id, date, lecture_time_id, audience_id, teacher_id) VALUES(?, ?, ?, ?, ?, ?)";
    private static final String UPDATE_LECTURE = "UPDATE lectures SET cathedra_id=?, subject_id=?, date=?, lecture_time_id=?, audience_id=?, teacher_id=? WHERE id=?";
    private static final String DELETE_LECTURE = "DELETE FROM lectures WHERE id = ?";
    private static final String INSERT_GROUP = "INSERT INTO lectures_groups(group_id, lecture_id) VALUES (?,?)";
    private static final String DELETE_GROUP = "DELETE FROM lectures_groups WHERE group_id = ? AND lecture_id = ?";
    private static final String SELECT_BY_AUDIENCE_DATE_LECTURE_TIME = "SELECT * FROM lectures WHERE audience_id = ? AND date = ? AND lecture_time_id = ?";
    private static final String SELECT_BY_TEACHER_ID_DATE_AND_LECTURE_TIME_ID = "SELECT * FROM lectures WHERE teacher_id = ? AND date = ? AND lecture_time_id = ?";
    private static final String SELECT_BY_TEACHER_AUDIENCE_DATE_LECTURE_TIME = "SELECT * FROM lectures WHERE teacher_id = ? AND audience_id = ? AND date = ? AND lecture_time_id = ?";
    private static final String SELECT_BY_STUDENT_ID_AND_PERIOD = "SELECT lec.*, lg.group_id FROM lectures AS lec LEFT JOIN lectures_groups AS lg ON lg.lecture_id = lec.id WHERE group_id = (SELECT group_id FROM students WHERE id = ?) AND date >= ? AND date <= ?";
    private static final String SELECT_BY_TEACHER_ID_AND_PERIOD = "SELECT * FROM lectures WHERE teacher_id = ? AND date >= ? AND date <= ?";

    private final JdbcTemplate jdbcTemplate;
    private final LectureRowMapper rowMapper;

    public JdbcLectureDao(JdbcTemplate jdbcTemplate, LectureRowMapper rowMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.rowMapper = rowMapper;
    }

    @Override
    public List<Lecture> findAll() {
        logger.debug("Find all lectures");
        return jdbcTemplate.query(SELECT_ALL, rowMapper);
    }

    @Override
    public Page<Lecture> findPaginatedLectures(Pageable pageable) {
        logger.debug("Find all lectures with pageSize:{} and offset:{}", pageable.getPageSize(), pageable.getOffset());
        int total = jdbcTemplate.queryForObject(COUNT_ALL, Integer.class);
        List<Lecture> lectures = jdbcTemplate.query(SELECT_BY_PAGE, rowMapper, pageable.getPageSize(),
            pageable.getOffset());

        return new PageImpl<>(lectures, pageable, total);
    }

    @Override
    public Optional<Lecture> findById(int id) {
        logger.debug("Find lecture by id: {}", id);
        try {
            return Optional.of(jdbcTemplate.queryForObject(SELECT_BY_ID, rowMapper, id));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    @Transactional
    public void save(Lecture lecture) {
        logger.debug("Save lecture {}", lecture);
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
            logger.debug("New lecture created with id: {}", lecture.getId());
        } else {
            jdbcTemplate.update(UPDATE_LECTURE, lecture.getCathedra().getId(), lecture.getSubject().getId(),
                lecture.getDate(), lecture.getTime().getId(), lecture.getAudience().getId(),
                lecture.getTeacher().getId(), lecture.getId());
            logger.debug("Lecture with id {} was updated", lecture.getId());
        }
        insertGroup(lecture);
        deleteGroup(lecture);
    }

    @Override
    public void deleteById(int id) {
        jdbcTemplate.update(DELETE_LECTURE, id);
        logger.debug("Lecture with id {} was deleted", id);
    }

    private void insertGroup(Lecture lecture) {
        Optional<Lecture> lectureOpt = findById(lecture.getId());
        if (lectureOpt.isPresent()) {
            for (Group group : lecture.getGroups().stream()
                .filter(group -> !lectureOpt.get().getGroups().contains(group)).collect(Collectors.toList())) {
                jdbcTemplate.update(INSERT_GROUP, group.getId(), lecture.getId());
                logger.debug("Insert group with id {} into lecture with id {}", group.getId(), lecture.getId());
            }
        }
    }

    private void deleteGroup(Lecture lecture) {
        Optional<Lecture> lectureOpt = findById(lecture.getId());
        if (lectureOpt.isPresent()) {
            for (Group group : lectureOpt.get().getGroups().stream()
                .filter(group -> !lecture.getGroups().contains(group)).collect(Collectors.toList())) {
                jdbcTemplate.update(DELETE_GROUP, group.getId(), lecture.getId());
                logger.debug("Delete group with id {} from lecture with id {}", group.getId(), lecture.getId());
            }
        }
    }

    @Override
    public Optional<Lecture> findByAudienceDateAndLectureTime(Audience audience, LocalDate date,
                                                              LectureTime lectureTime) {
        logger.debug("Find lectures by audience with id {}, date {} and lecture time id {}", audience.getId(), date,
            lectureTime.getId());
        try {
            return Optional.of(
                jdbcTemplate.queryForObject(SELECT_BY_AUDIENCE_DATE_LECTURE_TIME, rowMapper, audience.getId(), date,
                    lectureTime.getId()));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<Lecture> findByTeacherAudienceDateAndLectureTime(Teacher teacher, Audience audience, LocalDate date,
                                                                     LectureTime lectureTime) {
        logger.debug("Find lectures by teacher with id: {}, audience with id {}, date {} and lecture time id {}",
            teacher.getId(), audience.getId(), date,
            lectureTime.getId());
        try {
            return Optional.of(jdbcTemplate.queryForObject(SELECT_BY_TEACHER_AUDIENCE_DATE_LECTURE_TIME, rowMapper,
                teacher.getId(), audience.getId(), date,
                lectureTime.getId()));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<Lecture> findLecturesByTeacherDateAndTime(Teacher teacher, LocalDate date, LectureTime time) {
        logger.debug("Find lectures by teacher with id {}, date {} and lecture time id {}", teacher.getId(), date,
            time.getId());
        return jdbcTemplate.query(SELECT_BY_TEACHER_ID_DATE_AND_LECTURE_TIME_ID, rowMapper, teacher.getId(), date,
            time.getId());
    }

    @Override
    public List<Lecture> findLecturesByStudentAndPeriod(Student student, LocalDate start, LocalDate end) {
        logger.debug("Find lectures by student with id {} and period {} - {}", student.getId(), start, end);
        return jdbcTemplate.query(SELECT_BY_STUDENT_ID_AND_PERIOD, rowMapper, student.getId(), start, end);
    }

    @Override
    public List<Lecture> findLecturesByTeacherAndPeriod(Teacher teacher, LocalDate start, LocalDate end) {
        logger.debug("Find lectures by teacher with id {} and period {} - {}", teacher.getId(), start, end);
        return jdbcTemplate.query(SELECT_BY_TEACHER_ID_AND_PERIOD, rowMapper, teacher.getId(), start, end);
    }
}
