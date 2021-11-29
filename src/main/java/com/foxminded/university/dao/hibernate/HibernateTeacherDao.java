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
                .uniqueResult();
        List<Teacher> teachers = sessionFactory.getCurrentSession()
                .createQuery("FROM Teacher", Teacher.class)
                .setFirstResult((int)pageable.getOffset())
                .setMaxResults(pageable.getPageSize())
                .list();

        return new PageImpl<>(teachers, pageable, total);
    }

    @Override
    public Optional<Teacher> findById(int id) {
        logger.debug("Find teacher by id: {}", id);

        return findOrEmpty(
                () -> sessionFactory.getCurrentSession()
                        .createQuery("FROM Teacher WHERE id=:id", Teacher.class)
                        .setParameter("id", id)
                        .uniqueResult());
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
        //TODO: check that these methods not needed
        //updateSubjects(teacherOld, teacher);
        //deleteSubjects(teacherOld, teacher);
    }

    @Override
    public void deleteById(int id) {
        Session session = sessionFactory.getCurrentSession();
        session.delete(session.get(Teacher.class, id));
        logger.debug("Teacher with id {} was deleted", id);
    }

    /*
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
    */

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


    //TODO: need to correct hql query to made something like in jdbs
    @Override
    public List<Teacher> findByFreeDateAndSubjectWithCurrentTeacher(LocalDate date, LectureTime time, Subject subject) {
        logger.debug(
                "Find teachers who havent lectures and vacation this period: {} at {} - {} and who can teach this subject: {}",
                date, time.getStart(), time.getEnd(), subject.getName());
        List<Teacher> teachers = sessionFactory.getCurrentSession()
                .createQuery("SELECT DISTINCT teacher FROM Teacher AS teacher, Subject AS subject, Vacation AS vacation", Teacher.class)
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
