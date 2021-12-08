package com.foxminded.university.dao.hibernate;

import com.foxminded.university.dao.AudienceDao;
import com.foxminded.university.model.Audience;
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
public class HibernateAudienceDao implements AudienceDao {

    private static final Logger logger = LoggerFactory.getLogger(HibernateAudienceDao.class);

    private SessionFactory sessionFactory;

    public HibernateAudienceDao(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public List<Audience> findAll() {
        logger.debug("Find all audiences");

        return sessionFactory.getCurrentSession().getNamedQuery("findAllAudiences").getResultList();
    }

    @Override
    public Optional<Audience> findById(int id) {
        logger.debug("Find audience by id: {}", id);

        return Optional.ofNullable(sessionFactory.getCurrentSession().get(Audience.class, id));
    }

    @Override
    public void save(Audience audience) {
        logger.debug("Save audience {}", audience);
        Session session = sessionFactory.getCurrentSession();

        if (audience.getId() == 0) {
            session.save(audience);
            logger.debug("New audience created with id: {}", audience.getId());
        } else {
            session.merge(audience);
            logger.debug("Audience with id {} was updated", audience.getId());
        }
    }

    @Override
    public void delete(Audience audience) {
        sessionFactory.getCurrentSession().delete(audience);
        logger.debug("Audience with id {} was deleted", audience.getId());
    }

    @Override
    public Optional<Audience> findByRoom(int room) {
        logger.debug("Find audience by room number: {}", room);
        return findOrEmpty(
                () -> (Audience) sessionFactory.getCurrentSession()
                        .getNamedQuery("findAudienceByRoomNumber")
                        .setParameter("room", room)
                        .getSingleResult());
    }
}
