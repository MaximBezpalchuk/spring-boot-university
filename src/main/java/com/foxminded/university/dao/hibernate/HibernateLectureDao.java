package com.foxminded.university.dao.hibernate;

import com.foxminded.university.dao.LectureDao;
import com.foxminded.university.model.*;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Component
@Transactional
public class HibernateLectureDao implements LectureDao {

    private static final Logger logger = LoggerFactory.getLogger(HibernateAudienceDao.class);

    private SessionFactory sessionFactory;

    public HibernateLectureDao(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public List<Lecture> findAll() {
        logger.debug("Find all lectures");

        return sessionFactory.getCurrentSession()
                .createQuery("FROM Lecture", Lecture.class)
                .list();
    }

    @Override
    public Page<Lecture> findPaginatedLectures(Pageable pageable) {
        logger.debug("Find all lectures with pageSize:{} and offset:{}", pageable.getPageSize(), pageable.getOffset());
        int total = (int) (long) sessionFactory.getCurrentSession()
                .createQuery("SELECT COUNT(l) FROM Lecture l")
                .getSingleResult();
        List<Lecture> lectures = sessionFactory.getCurrentSession()
                .createQuery("FROM Lecture", Lecture.class)
                .setFirstResult((int) pageable.getOffset())
                .setMaxResults(pageable.getPageSize())
                .list();

        return new PageImpl<>(lectures, pageable, total);
    }

    @Override
    public Optional<Lecture> findById(int id) {
        logger.debug("Find lecture by id: {}", id);

        return findOrEmpty(
                () -> sessionFactory.getCurrentSession()
                        .createQuery("FROM Lecture WHERE id=:id", Lecture.class)
                        .setParameter("id", id)
                        .uniqueResult());
    }

    @Override
    public void save(Lecture lecture) {
        logger.debug("Save lecture {}", lecture);
        Session session = sessionFactory.getCurrentSession();

        if (lecture.getId() == 0) {
            session.save(lecture);
            logger.debug("New lecture created with id: {}", lecture.getId());
        } else {
            session.merge(lecture);
            logger.debug("Lecture with id {} was updated", lecture.getId());
        }
    }

    @Override
    public void deleteById(int id) {
        Session session = sessionFactory.getCurrentSession();
        session.delete(session.get(Lecture.class, id));
        logger.debug("Lecture with id {} was deleted", id);
    }

    @Override
    public Optional<Lecture> findByAudienceDateAndLectureTime(Audience audience, LocalDate date,
                                                              LectureTime lectureTime) {
        logger.debug("Find lectures by audience with id {}, date {} and lecture time id {}", audience.getId(), date,
                lectureTime.getId());

        return findOrEmpty(
                () -> sessionFactory.getCurrentSession()
                        .createQuery(
                                "FROM Lecture WHERE audience.id=:audience_id AND date=:date AND time.id=:time_id",
                                Lecture.class)
                        .setParameter("audience_id", audience.getId())
                        .setParameter("date", date)
                        .setParameter("time_id", lectureTime.getId())
                        .uniqueResult());
    }

    @Override
    public Optional<Lecture> findByTeacherAudienceDateAndLectureTime(Teacher teacher, Audience audience, LocalDate date,
                                                                     LectureTime lectureTime) {
        logger.debug("Find lectures by teacher with id: {}, audience with id {}, date {} and lecture time id {}",
                teacher.getId(), audience.getId(), date, lectureTime.getId());
        return findOrEmpty(
                () -> sessionFactory.getCurrentSession()
                        .createQuery(
                                "FROM Lecture WHERE teacher.id=:teacher_id AND audience.id=:audience_id AND date=:date AND time.id=:time_id",
                                Lecture.class)
                        .setParameter("teacher_id", teacher.getId())
                        .setParameter("audience_id", audience.getId())
                        .setParameter("date", date)
                        .setParameter("time_id", lectureTime.getId())
                        .uniqueResult());
    }

    @Override
    public List<Lecture> findLecturesByTeacherDateAndTime(Teacher teacher, LocalDate date, LectureTime time) {
        logger.debug("Find lectures by teacher with id {}, date {} and lecture time id {}", teacher.getId(), date,
                time.getId());

        return sessionFactory.getCurrentSession()
                .createQuery("FROM Lecture WHERE teacher.id=:teacher_id AND date=:date AND time.id=:time_id",
                        Lecture.class)
                .setParameter("teacher_id", teacher.getId())
                .setParameter("date", date)
                .setParameter("time_id", time.getId())
                .list();
    }

    @Override
    public List<Lecture> findLecturesByStudentAndPeriod(Student student, LocalDate start, LocalDate end) {
        logger.debug("Find lectures by student with id {} and period {} - {}", student.getId(), start, end);

        return sessionFactory.getCurrentSession()
                .createSQLQuery("SELECT lec.*, lg.group_id FROM lectures AS lec LEFT JOIN lectures_groups AS lg ON lg.lecture_id = lec.id WHERE group_id = (SELECT group_id FROM students WHERE id =:student_id) AND date >=:start AND date <=:end")
                .addEntity(Lecture.class)
                .setParameter("student_id", student.getId())
                .setParameter("start", start)
                .setParameter("end", end)
                .list();
    }

    @Override
    public List<Lecture> findLecturesByTeacherAndPeriod(Teacher teacher, LocalDate start, LocalDate end) {
        logger.debug("Find lectures by teacher with id {} and period {} - {}", teacher.getId(), start, end);

        return sessionFactory.getCurrentSession()
                .createQuery("FROM Lecture WHERE teacher=:teacher AND date>=:start AND date<=:end", Lecture.class)
                .setParameter("teacher", teacher)
                .setParameter("start", start)
                .setParameter("end", end)
                .list();
    }
}