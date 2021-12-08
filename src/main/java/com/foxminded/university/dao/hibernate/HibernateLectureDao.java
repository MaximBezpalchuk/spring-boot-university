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

        return sessionFactory.getCurrentSession().getNamedQuery("findAllLectures").getResultList();
    }

    @Override
    public Page<Lecture> findPaginatedLectures(Pageable pageable) {
        logger.debug("Find all lectures with pageSize:{} and offset:{}", pageable.getPageSize(), pageable.getOffset());
        int total = (int) (long) sessionFactory.getCurrentSession()
                .getNamedQuery("countAllLectures")
                .getSingleResult();
        List<Lecture> lectures = sessionFactory.getCurrentSession()
                .getNamedQuery("findAllLectures")
                .setFirstResult((int) pageable.getOffset())
                .setMaxResults(pageable.getPageSize())
                .getResultList();

        return new PageImpl<>(lectures, pageable, total);
    }

    @Override
    public Optional<Lecture> findById(int id) {
        logger.debug("Find lecture by id: {}", id);

        return Optional.ofNullable(sessionFactory.getCurrentSession().get(Lecture.class, id));
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
    public void delete(Lecture lecture) {
        sessionFactory.getCurrentSession().delete(lecture);
        logger.debug("Lecture with id {} was deleted", lecture.getId());
    }

    @Override
    public Optional<Lecture> findByAudienceDateAndLectureTime(Audience audience, LocalDate date,
                                                              LectureTime lectureTime) {
        logger.debug("Find lectures by audience with id {}, date {} and lecture time id {}", audience.getId(), date,
                lectureTime.getId());

        return findOrEmpty(
                () -> (Lecture) sessionFactory.getCurrentSession()
                        .getNamedQuery("findLectureByAudienceDateAndLectureTime")
                        .setParameter("audience_id", audience.getId())
                        .setParameter("date", date)
                        .setParameter("time_id", lectureTime.getId())
                        .getSingleResult());
    }

    @Override
    public Optional<Lecture> findByTeacherAudienceDateAndLectureTime(Teacher teacher, Audience audience, LocalDate date,
                                                                     LectureTime lectureTime) {
        logger.debug("Find lectures by teacher with id: {}, audience with id {}, date {} and lecture time id {}",
                teacher.getId(), audience.getId(), date, lectureTime.getId());
        return findOrEmpty(
                () -> (Lecture) sessionFactory.getCurrentSession()
                        .getNamedQuery("findLectureByTeacherAudienceDateAndLectureTime")
                        .setParameter("teacher_id", teacher.getId())
                        .setParameter("audience_id", audience.getId())
                        .setParameter("date", date)
                        .setParameter("time_id", lectureTime.getId())
                        .getSingleResult());
    }

    @Override
    public List<Lecture> findLecturesByTeacherDateAndTime(Teacher teacher, LocalDate date, LectureTime time) {
        logger.debug("Find lectures by teacher with id {}, date {} and lecture time id {}", teacher.getId(), date,
                time.getId());

        return sessionFactory.getCurrentSession()
                .getNamedQuery("findLecturesByTeacherDateAndTime")
                .setParameter("teacher_id", teacher.getId())
                .setParameter("date", date)
                .setParameter("time_id", time.getId())
                .getResultList();
    }

    @Override
    public List<Lecture> findLecturesByStudentAndPeriod(Student student, LocalDate start, LocalDate end) {
        logger.debug("Find lectures by student with id {} and period {} - {}", student.getId(), start, end);

        return sessionFactory.getCurrentSession()
                .getNamedNativeQuery("findLecturesByStudentAndPeriod")
                .addEntity(Lecture.class)
                .setParameter("student_id", student.getId())
                .setParameter("start", start)
                .setParameter("end", end)
                .getResultList();
    }

    @Override
    public List<Lecture> findLecturesByTeacherAndPeriod(Teacher teacher, LocalDate start, LocalDate end) {
        logger.debug("Find lectures by teacher with id {} and period {} - {}", teacher.getId(), start, end);

        return sessionFactory.getCurrentSession()
                .getNamedQuery("findLecturesByTeacherAndPeriod")
                .setParameter("teacher", teacher)
                .setParameter("start", start)
                .setParameter("end", end)
                .getResultList();
    }
}