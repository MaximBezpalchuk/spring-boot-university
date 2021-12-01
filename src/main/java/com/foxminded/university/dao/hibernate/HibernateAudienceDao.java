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

        return sessionFactory.getCurrentSession()
                .createQuery("FROM Audience", Audience.class)
                .list();
    }

    @Override
    public Optional<Audience> findById(int id) {
        logger.debug("Find audience by id: {}", id);

        return findOrEmpty(
                () -> sessionFactory.getCurrentSession()
                        .createQuery("FROM Audience WHERE id=:id", Audience.class)
                        .setParameter("id", id)
                        .uniqueResult());
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
    public void deleteById(int id) {
        Session session = sessionFactory.getCurrentSession();
        session.delete(session.get(Audience.class, id));
        logger.debug("Audience with id {} was deleted", id);
    }

    @Override
    public Optional<Audience> findByRoom(int room) {
        logger.debug("Find audience by room number: {}", room);
        return findOrEmpty(
                () -> sessionFactory.getCurrentSession()
                        .createQuery("FROM Audience WHERE room=:room", Audience.class)
                        .setParameter("room", room)
                        .uniqueResult());
    }
}
