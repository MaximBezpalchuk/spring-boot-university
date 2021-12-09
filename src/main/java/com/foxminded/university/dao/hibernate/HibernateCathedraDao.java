package com.foxminded.university.dao.hibernate;

import com.foxminded.university.dao.CathedraDao;
import com.foxminded.university.model.Cathedra;
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
public class HibernateCathedraDao implements CathedraDao {

    private static final Logger logger = LoggerFactory.getLogger(HibernateAudienceDao.class);

    private final SessionFactory sessionFactory;

    public HibernateCathedraDao(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public List<Cathedra> findAll() {
        logger.debug("Find all cathedras");

        return sessionFactory.getCurrentSession().getNamedQuery("findAllCathedras").getResultList();
    }

    @Override
    public Optional<Cathedra> findById(int id) {
        logger.debug("Find cathedra by id: {}", id);

        return Optional.ofNullable(sessionFactory.getCurrentSession().get(Cathedra.class, id));
    }

    @Override
    public void save(Cathedra cathedra) {
        logger.debug("Save cathedra {}", cathedra);
        Session session = sessionFactory.getCurrentSession();

        if (cathedra.getId() == 0) {
            session.save(cathedra);
            logger.debug("New cathedra created with id: {}", cathedra.getId());
        } else {
            session.merge(cathedra);
            logger.debug("Cathedra with id {} was updated", cathedra.getId());
        }
    }

    @Override
    public void delete(Cathedra cathedra) {
        sessionFactory.getCurrentSession().delete(cathedra);
        logger.debug("Cathedra with id {} was deleted", cathedra.getId());
    }

    @Override
    public Optional<Cathedra> findByName(String name) {
        logger.debug("Find audience by name: {}", name);
        return findOrEmpty(
                () -> (Cathedra) sessionFactory.getCurrentSession()
                        .getNamedQuery("findCathedraByName")
                        .setParameter("name", name)
                        .getSingleResult());
    }
}
