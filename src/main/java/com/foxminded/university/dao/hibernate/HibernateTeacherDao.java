package com.foxminded.university.dao.hibernate;

import com.foxminded.university.dao.TeacherDao;
import com.foxminded.university.exception.EntityNotFoundException;
import com.foxminded.university.model.LectureTime;
import com.foxminded.university.model.Subject;
import com.foxminded.university.model.Teacher;
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
public class HibernateTeacherDao implements TeacherDao {

    private static final Logger logger = LoggerFactory.getLogger(HibernateAudienceDao.class);

    private SessionFactory sessionFactory;

    public HibernateTeacherDao(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public List<Teacher> findAll() {
        logger.debug("Find all teachers");

        return sessionFactory.getCurrentSession()
                .createQuery("FROM Teacher", Teacher.class)
                .list();
    }

    @Override
    public Page<Teacher> findPaginatedTeachers(Pageable pageable) {
        logger.debug("Find all teachers with pageSize:{} and offset:{}", pageable.getPageSize(), pageable.getOffset());
        int total = (int) (long) sessionFactory.getCurrentSession()
                .createQuery("SELECT COUNT(t) FROM Teacher t")
                .getSingleResult();
        List<Teacher> teachers = sessionFactory.getCurrentSession()
                .createQuery("FROM Teacher", Teacher.class)
                .setFirstResult((int) pageable.getOffset())
                .setMaxResults(pageable.getPageSize())
                .list();

        return new PageImpl<>(teachers, pageable, total);
    }

    @Override
    public Optional<Teacher> findById(int id) {
        logger.debug("Find teacher by id: {}", id);

        return Optional.of(sessionFactory.getCurrentSession().get(Teacher.class, id));
    }

    @Override
    public void save(Teacher teacher) {
        logger.debug("Save teacher {}", teacher);
        Session session = sessionFactory.getCurrentSession();

        if (teacher.getId() == 0) {
            session.save(teacher);
            logger.debug("New teacher created with id: {}", teacher.getId());
        } else {
            session.merge(teacher);
            logger.debug("Teacher with id {} was updated", teacher.getId());
        }
    }

    @Override
    public void deleteById(int id) {
        Session session = sessionFactory.getCurrentSession();
        session.delete(session.get(Teacher.class, id));
        logger.debug("Teacher with id {} was deleted", id);
    }

    @Override
    public Optional<Teacher> findByFullNameAndBirthDate(String firstName, String lastName, LocalDate birthDate) {
        logger.debug("Find teacher by first name: {}, last name: {} and birth date: {}", firstName, lastName,
                birthDate);

        return findOrEmpty(
                () -> sessionFactory.getCurrentSession()
                        .createQuery("FROM Teacher WHERE firstName=:first_name AND lastName=:last_name AND birthDate=:birth_date", Teacher.class)
                        .setParameter("first_name", firstName)
                        .setParameter("last_name", lastName)
                        .setParameter("birth_date", birthDate)
                        .uniqueResult());
    }

    @Override
    public List<Teacher> findByFreeDateAndSubjectWithCurrentTeacher(LocalDate date, LectureTime time, Subject subject) {
        logger.debug(
                "Find teachers who havent lectures and vacation this period: {} at {} - {} and who can teach this subject: {}",
                date, time.getStart(), time.getEnd(), subject.getName());
        List<Teacher> teachers = sessionFactory.getCurrentSession()
                .createSQLQuery("SELECT DISTINCT teachers.* FROM teachers LEFT JOIN subjects_teachers AS st ON teachers.id = st.teacher_id LEFT JOIN vacations AS vac ON teachers.id = vac.teacher_id WHERE st.subject_id =:subject_id AND ((:date NOT BETWEEN vac.start AND vac.finish) OR (vac.start IS NULL AND vac.finish IS NULL))AND NOT EXISTS (select id from lectures where lectures.teacher_id = teachers.id AND lectures.date =:date AND lectures.lecture_time_id = :lecture_time_id)")
                .addEntity(Teacher.class)
                .setParameter("subject_id", subject.getId())
                .setParameter("date", date)
                .setParameter("lecture_time_id", time.getId())
                .list();
        if (teachers.isEmpty()) {
            throw new EntityNotFoundException(
                    "Can`t find teachers who havent lectures and vacation this period:" + date + " at "
                            + time.getStart() + " - " + time.getEnd() + " and who can teach this subject: "
                            + subject.getName());
        }

        return teachers;
    }
}
