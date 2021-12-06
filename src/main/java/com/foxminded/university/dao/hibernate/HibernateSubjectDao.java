package com.foxminded.university.dao.hibernate;

import com.foxminded.university.dao.SubjectDao;
import com.foxminded.university.model.Subject;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Component
@Transactional
public class HibernateSubjectDao implements SubjectDao {

    private static final Logger logger = LoggerFactory.getLogger(HibernateAudienceDao.class);

    private SessionFactory sessionFactory;

    public HibernateSubjectDao(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public List<Subject> findAll() {
        logger.debug("Find all subjects");

        return sessionFactory.getCurrentSession()
                .createQuery("FROM Subject", Subject.class)
                .list();
    }

    @Override
    public Page<Subject> findPaginatedSubjects(Pageable pageable) {
        logger.debug("Find all subjects with pageSize:{} and offset:{}", pageable.getPageSize(), pageable.getOffset());
        int total = (int) (long) sessionFactory.getCurrentSession()
                .createQuery("SELECT COUNT(s) FROM Subject s")
                .uniqueResult();
        List<Subject> subjects = sessionFactory.getCurrentSession()
                .createQuery("FROM Subject", Subject.class)
                .setFirstResult((int) pageable.getOffset())
                .setMaxResults(pageable.getPageSize())
                .list();

        return new PageImpl<>(subjects, pageable, total);
    }

    @Override
    public Optional<Subject> findById(int id) {
        logger.debug("Find subject by id: {}", id);

        return Optional.ofNullable(sessionFactory.getCurrentSession().get(Subject.class, id));
    }

    @Override
    public void save(Subject subject) {
        logger.debug("Save subject {}", subject);
        Session session = sessionFactory.getCurrentSession();

        if (subject.getId() == 0) {
            session.save(subject);
            logger.debug("New subject created with id: {}", subject.getId());
        } else {
            session.merge(subject);
            logger.debug("Subject with id {} was updated", subject.getId());
        }
    }

    @Override
    public void deleteById(int id) {
        Session session = sessionFactory.getCurrentSession();
        session.delete(session.get(Subject.class, id));
        logger.debug("Subject with id {} was deleted", id);
    }

    @Override
    public Optional<Subject> findByName(String name) {
        logger.debug("Find subject by name: {}", name);

        return findOrEmpty(
                () -> sessionFactory.getCurrentSession()
                        .createQuery("FROM Subject WHERE name=:name", Subject.class)
                        .setParameter("name", name)
                        .uniqueResult());
    }
}
