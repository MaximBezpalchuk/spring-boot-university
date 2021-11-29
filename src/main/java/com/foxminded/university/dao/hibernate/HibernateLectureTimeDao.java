package com.foxminded.university.dao.hibernate;

import com.foxminded.university.dao.LectureTimeDao;
import com.foxminded.university.model.LectureTime;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Component
public class HibernateLectureTimeDao implements LectureTimeDao {

	private static final Logger logger = LoggerFactory.getLogger(HibernateAudienceDao.class);

	private SessionFactory sessionFactory;

	public HibernateLectureTimeDao(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	@Override
	public List<LectureTime> findAll() {
		logger.debug("Find all lecture times");

		return sessionFactory.getCurrentSession()
				.createQuery("FROM LectureTime", LectureTime.class)
				.list();
	}

	@Override
	public Optional<LectureTime> findById(int id) {
		logger.debug("Find lecture time by id: {}", id);

		return findOrEmpty(
				() -> sessionFactory.getCurrentSession()
						.createQuery("FROM LectureTime WHERE id=:id", LectureTime.class)
						.setParameter("id", id)
						.uniqueResult());
	}

	@Override
	public void save(LectureTime lectureTime) {
		logger.debug("Save lecture time {}", lectureTime);
		Session session = sessionFactory.getCurrentSession();

		if (lectureTime.getId() == 0) {
			session.save(lectureTime);
			logger.debug("New lecture time created with id: {}", lectureTime.getId());
		} else {
			session.merge(lectureTime);
			logger.debug("Lecture time with id {} was updated", lectureTime.getId());
		}
	}

	@Override
	public void deleteById(int id) {
		Session session = sessionFactory.getCurrentSession();
		session.delete(session.get(LectureTime.class, id));
		logger.debug("Lecture time with id {} was deleted", id);
	}

	@Override
	public Optional<LectureTime> findByPeriod(LocalTime start, LocalTime end) {
		logger.debug("Find lecture time which starts at {} and end at {}", start, end);

		return findOrEmpty(
				() -> sessionFactory.getCurrentSession()
						.createQuery("FROM LectureTime WHERE start=:start AND end=:end", LectureTime.class)
						.setParameter("start", start)
						.setParameter("end", end)
						.uniqueResult());
	}
}