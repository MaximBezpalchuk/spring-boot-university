package com.foxminded.university.dao.hibernate;

import com.foxminded.university.dao.GroupDao;
import com.foxminded.university.model.Group;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Component
@Transactional
public class HibernateGroupDao implements GroupDao {

    private static final Logger logger = LoggerFactory.getLogger(HibernateAudienceDao.class);

    private SessionFactory sessionFactory;

    public HibernateGroupDao(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public List<Group> findAll() {
        logger.debug("Find all groups");

        return sessionFactory.getCurrentSession().getNamedQuery("findAllGroups").getResultList();
    }

    @Override
    public Optional<Group> findById(int id) {
        logger.debug("Find group by id: {}", id);

        return Optional.ofNullable(sessionFactory.getCurrentSession().get(Group.class, id));
    }

    @Override
    public void save(Group group) {
        logger.debug("Save group {}", group);
        Session session = sessionFactory.getCurrentSession();

        if (group.getId() == 0) {
            session.save(group);
            logger.debug("New group created with id: {}", group.getId());
        } else {
            session.merge(group);
            logger.debug("Group with id {} was updated", group.getId());
        }
    }

    @Override
    public void delete(Group group) {
        sessionFactory.getCurrentSession().delete(group);
        logger.debug("Group with id {} was deleted", group.getId());
    }

    @Override
    public Optional<Group> findByName(String name) {
        logger.debug("Find group with name {}", name);

        return findOrEmpty(
                () -> (Group) sessionFactory.getCurrentSession()
                        .getNamedQuery("findGroupByName")
                        .setParameter("name", name)
                        .getSingleResult());
    }
}
