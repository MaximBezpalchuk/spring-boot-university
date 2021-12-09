package com.foxminded.university.dao.jdbc.mapper;

import com.foxminded.university.dao.jdbc.*;
import com.foxminded.university.model.Lecture;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

@Component
public class LectureRowMapper implements RowMapper<Lecture> {

    private JdbcCathedraDao cathedraDao;
    private JdbcGroupDao groupDao;
    private JdbcAudienceDao audienceDao;
    private JdbcLectureTimeDao lectureTimeDao;
    private JdbcTeacherDao teacherDao;
    private JdbcSubjectDao subjectDao;

    public LectureRowMapper(JdbcCathedraDao cathedraDao, JdbcGroupDao groupDao, JdbcAudienceDao audienceDao,
                            JdbcLectureTimeDao lectureTimeDao, JdbcTeacherDao teacherDao, JdbcSubjectDao subjectDao) {
        this.cathedraDao = cathedraDao;
        this.groupDao = groupDao;
        this.audienceDao = audienceDao;
        this.lectureTimeDao = lectureTimeDao;
        this.teacherDao = teacherDao;
        this.subjectDao = subjectDao;
    }

    @Override
    public Lecture mapRow(ResultSet resultSet, int rowNum) throws SQLException {

        Lecture lecture = Lecture.builder()
                .id(resultSet.getInt("id"))
                .date(resultSet.getObject("date", LocalDate.class))
                .group(groupDao.findByLectureId(resultSet.getInt("id")))
                .build();
        cathedraDao.findById(resultSet.getInt("cathedra_id")).ifPresent(lecture::setCathedra);
        subjectDao.findById(resultSet.getInt("subject_id")).ifPresent(lecture::setSubject);
        lectureTimeDao.findById(resultSet.getInt("lecture_time_id")).ifPresent(lecture::setTime);
        audienceDao.findById(resultSet.getInt("audience_id")).ifPresent(lecture::setAudience);
        teacherDao.findById(resultSet.getInt("teacher_id")).ifPresent(lecture::setTeacher);

        return lecture;
    }
}
