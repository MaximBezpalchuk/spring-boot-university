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

		return sessionFactory.getCurrentSession()
				.createQuery("FROM Group", Group.class)
				.list();
	}

	@Override
	public Optional<Group> findById(int id) {
		logger.debug("Find group by id: {}", id);

		return findOrEmpty(
				() -> sessionFactory.getCurrentSession()
						.createQuery("FROM Group WHERE id=:id", Group.class)
						.setParameter("id", id)
						.uniqueResult());
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
	public void deleteById(int id) {
		Session session = sessionFactory.getCurrentSession();
		session.delete(session.get(Group.class, id));
		logger.debug("Group with id {} was deleted", id);
	}

	//TODO: check if i can delete it
	@Override
	public List<Group> findByLectureId(int id) {
		logger.debug("Find groups with lecture id: {}", id);
		return sessionFactory.getCurrentSession()
				.createQuery(
						"SELECT group FROM Group AS group, Lecture AS lecture WHERE group IN elements(lecture.groups) AND lecture.id=:lecture_id",
						Group.class)
				.setParameter("lecture_id", id)
				.list();
	}

	@Override
	public Optional<Group> findByName(String name) {
		logger.debug("Find group with name {}", name);

		return findOrEmpty(
				() -> sessionFactory.getCurrentSession()
						.createQuery("FROM Group WHERE name=:name", Group.class)
						.setParameter("name", name)
						.uniqueResult());
	}
}
