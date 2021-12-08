package com.foxminded.university.dao.hibernate;

import com.foxminded.university.dao.StudentDao;
import com.foxminded.university.model.Student;
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
public class HibernateStudentDao implements StudentDao {

    private static final Logger logger = LoggerFactory.getLogger(HibernateAudienceDao.class);

    private SessionFactory sessionFactory;

    public HibernateStudentDao(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public List<Student> findAll() {
        logger.debug("Find all students");

        return sessionFactory.getCurrentSession().getNamedQuery("findAllStudents").getResultList();
    }

    @Override
    public Page<Student> findPaginatedStudents(Pageable pageable) {
        logger.debug("Find all students with pageSize:{} and offset:{}", pageable.getPageSize(), pageable.getOffset());
        int total = (int) (long) sessionFactory.getCurrentSession()
                .getNamedQuery("countAllStudents")
                .getSingleResult();
        List<Student> students = sessionFactory.getCurrentSession()
                .getNamedQuery("findAllStudents")
                .setFirstResult((int) pageable.getOffset())
                .setMaxResults(pageable.getPageSize())
                .getResultList();

        return new PageImpl<>(students, pageable, total);
    }

    @Override
    public Optional<Student> findById(int id) {
        logger.debug("Find student by id: {}", id);

        return Optional.ofNullable(sessionFactory.getCurrentSession().get(Student.class, id));
    }

    @Override
    public void save(Student student) {
        logger.debug("Save student {}", student);
        Session session = sessionFactory.getCurrentSession();

        if (student.getId() == 0) {
            session.save(student);
            logger.debug("New student created with id: {}", student.getId());
        } else {
            session.merge(student);
            logger.debug("Student with id {} was updated", student.getId());
        }
    }

    @Override
    public void delete(Student student) {
        sessionFactory.getCurrentSession().delete(student);
        logger.debug("Student with id {} was deleted", student.getId());
    }

    @Override
    public Optional<Student> findByFullNameAndBirthDate(String firstName, String lastName, LocalDate birthDate) {
        logger.debug("Find student with first name: {}, last name: {} and birthDate {}", firstName, lastName,
                birthDate);

        return findOrEmpty(
                () -> (Student) sessionFactory.getCurrentSession()
                        .getNamedQuery("findStudentByFullNameAndBirthDate")
                        .setParameter("first_name", firstName)
                        .setParameter("last_name", lastName)
                        .setParameter("birth_date", birthDate)
                        .getSingleResult());
    }

    @Override
    public List<Student> findByGroupId(int id) {
        logger.debug("Find students with group id {}", id);

        return sessionFactory.getCurrentSession()
                .getNamedQuery("findStudentByGroupId")
                .setParameter("group_id", id)
                .getResultList();
    }
}
