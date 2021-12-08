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

        return sessionFactory.getCurrentSession().getNamedQuery("findAllSubjects").getResultList();
    }

    @Override
    public Page<Subject> findPaginatedSubjects(Pageable pageable) {
        logger.debug("Find all subjects with pageSize:{} and offset:{}", pageable.getPageSize(), pageable.getOffset());
        int total = (int) (long) sessionFactory.getCurrentSession()
                .getNamedQuery("countAllSubjects")
                .getSingleResult();
        List<Subject> subjects = sessionFactory.getCurrentSession()
                .getNamedQuery("findAllSubjects")
                .setFirstResult((int) pageable.getOffset())
                .setMaxResults(pageable.getPageSize())
                .getResultList();

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
    public void delete(Subject subject) {
        sessionFactory.getCurrentSession().delete(subject);
        logger.debug("Subject with id {} was deleted", subject.getId());
    }

    @Override
    public Optional<Subject> findByName(String name) {
        logger.debug("Find subject by name: {}", name);

        return findOrEmpty(
                () -> (Subject) sessionFactory.getCurrentSession()
                        .getNamedQuery("findSubjectByName")
                        .setParameter("name", name)
                        .getSingleResult());
    }
}
