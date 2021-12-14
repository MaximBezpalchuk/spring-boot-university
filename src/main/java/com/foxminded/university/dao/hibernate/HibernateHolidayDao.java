package com.foxminded.university.dao.hibernate;

import com.foxminded.university.dao.HolidayDao;
import com.foxminded.university.model.Holiday;
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
public class HibernateHolidayDao implements HolidayDao {

    private static final Logger logger = LoggerFactory.getLogger(HibernateAudienceDao.class);

    private final SessionFactory sessionFactory;

    public HibernateHolidayDao(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public List<Holiday> findAll() {
        logger.debug("Find all holidays");

        return sessionFactory.getCurrentSession().getNamedQuery("findAllHolidays").getResultList();

    }

    @Override
    public Page<Holiday> findPaginatedHolidays(Pageable pageable) {
        logger.debug("Find all holidays with pageSize:{} and offset:{}", pageable.getPageSize(), pageable.getOffset());
        int total = (int) (long) sessionFactory.getCurrentSession()
            .getNamedQuery("countAllHolidays")
            .getSingleResult();
        List<Holiday> holidays = sessionFactory.getCurrentSession()
            .getNamedQuery("findAllHolidays")
            .setFirstResult((int) pageable.getOffset())
            .setMaxResults(pageable.getPageSize())
            .getResultList();

        return new PageImpl<>(holidays, pageable, total);
    }

    @Override
    public Optional<Holiday> findById(int id) {
        logger.debug("Find holiday by id: {}", id);

        return Optional.ofNullable(sessionFactory.getCurrentSession().get(Holiday.class, id));
    }

    @Override
    public void save(Holiday holiday) {
        logger.debug("Save holiday {}", holiday);
        Session session = sessionFactory.getCurrentSession();

        if (holiday.getId() == 0) {
            logger.debug("New holiday created with id: {}", holiday.getId());
            session.save(holiday);
        } else {
            logger.debug("Holiday with id {} was updated", holiday.getId());
            session.merge(holiday);
        }
    }

    @Override
    public void delete(Holiday holiday) {
        logger.debug("Holiday with id {} was deleted", holiday.getId());
        sessionFactory.getCurrentSession().delete(holiday);
    }

    @Override
    public Optional<Holiday> findByNameAndDate(String name, LocalDate date) {
        logger.debug("Find holiday with name: {} and date: {}", name, date);

        return sessionFactory.getCurrentSession()
            .getNamedQuery("findHolidayByNameAndDate")
            .setParameter("name", name)
            .setParameter("date", date)
            .uniqueResultOptional();
    }

    @Override
    public List<Holiday> findByDate(LocalDate date) {
        logger.debug("Find holiday by date: {}", date);

        return sessionFactory.getCurrentSession()
            .getNamedQuery("findHolidayByDate")
            .setParameter("date", date)
            .getResultList();
    }
}