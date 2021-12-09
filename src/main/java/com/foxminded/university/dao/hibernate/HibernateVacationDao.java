package com.foxminded.university.dao.hibernate;

import com.foxminded.university.dao.VacationDao;
import com.foxminded.university.model.Teacher;
import com.foxminded.university.model.Vacation;
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
public class HibernateVacationDao implements VacationDao {

    private static final Logger logger = LoggerFactory.getLogger(HibernateAudienceDao.class);

    private final SessionFactory sessionFactory;

    public HibernateVacationDao(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public List<Vacation> findAll() {
        logger.debug("Find all vacations");

        return sessionFactory.getCurrentSession().getNamedQuery("findAllVacations").getResultList();
    }

    @Override
    public Page<Vacation> findPaginatedVacationsByTeacherId(Pageable pageable, int id) {
        logger.debug("Find all vacations with pageSize:{} and offset:{} by teacherId:{}", pageable.getPageSize(),
                pageable.getOffset(), id);
        int total = (int) (long) sessionFactory.getCurrentSession()
                .getNamedQuery("countAllVacationsByTeacherId")
                .setParameter("teacher_id", id)
                .getSingleResult();
        List<Vacation> vacations = sessionFactory.getCurrentSession()
                .getNamedQuery("findAllVacationsByTeacherId")
                .setParameter("teacher_id", id)
                .setFirstResult((int) pageable.getOffset())
                .setMaxResults(pageable.getPageSize())
                .getResultList();

        return new PageImpl<>(vacations, pageable, total);
    }

    @Override
    public Optional<Vacation> findById(int id) {
        logger.debug("Find vacation by id: {}", id);

        return Optional.ofNullable(sessionFactory.getCurrentSession().get(Vacation.class, id));
    }

    @Override
    public void save(Vacation vacation) {

        logger.debug("Save vacation {}", vacation);
        Session session = sessionFactory.getCurrentSession();

        if (vacation.getId() == 0) {
            session.save(vacation);
            logger.debug("New vacation created with id: {}", vacation.getId());
        } else {
            session.merge(vacation);
            logger.debug("Vacation with id {} was updated", vacation.getId());
        }
    }

    @Override
    public void delete(Vacation vacation) {
        sessionFactory.getCurrentSession().delete(vacation);
        logger.debug("Vacation with id {} was deleted", vacation.getId());
    }

    @Override
    public List<Vacation> findByTeacherId(int id) {
        logger.debug("Find vacations by teacher id: {}", id);
        return sessionFactory.getCurrentSession()
                .getNamedQuery("findAllVacationsByTeacherId")
                .setParameter("teacher_id", id)
                .getResultList();
    }

    @Override
    public Optional<Vacation> findByPeriodAndTeacher(LocalDate start, LocalDate end, Teacher teacher) {
        logger.debug("Find vacation by vacation start: {}, end: {}, teacher id: {}", start, end, teacher.getId());

        return findOrEmpty(
                () -> (Vacation) sessionFactory.getCurrentSession()
                        .getNamedNativeQuery("findVacationByPeriodAndTeacher")
                        .addEntity(Vacation.class)
                        .setParameter("start", start)
                        .setParameter("end", end)
                        .setParameter("teacher_id", teacher.getId())
                        .getSingleResult());
    }

    @Override
    public List<Vacation> findByDateInPeriodAndTeacher(LocalDate date, Teacher teacher) {
        logger.debug("Find vacations by vacation date: {} and teacher id: {}", date, teacher.getId());
        return sessionFactory.getCurrentSession()
                .getNamedNativeQuery("findVacationsByDateInPeriodAndTeacher")
                .addEntity(Vacation.class)
                .setParameter("date", date)
                .setParameter("teacher_id", teacher.getId())
                .getResultList();
    }

    @Override
    public List<Vacation> findByTeacherIdAndYear(int id, int year) {
        logger.debug("Find vacations by teacher id: {} and year: {}", id, year);
        return sessionFactory.getCurrentSession()
                .getNamedQuery("findVacationsByTeacherIdAndYear")
                .setParameter("teacher_id", id)
                .setParameter("year", year)
                .getResultList();
    }
}
