package com.foxminded.university.dao.hibernate;

import com.foxminded.university.dao.StudentDao;
import com.foxminded.university.model.Student;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
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
public class HibernateStudentDao implements StudentDao {

	private static final Logger logger = LoggerFactory.getLogger(HibernateAudienceDao.class);

	private SessionFactory sessionFactory;

	public HibernateStudentDao(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	@Override
	public List<Student> findAll() {
		logger.debug("Find all students");

		return sessionFactory.getCurrentSession()
				.createQuery("FROM Student", Student.class)
				.list();
	}

	@Override
	public Page<Student> findPaginatedStudents(Pageable pageable) {
		logger.debug("Find all students with pageSize:{} and offset:{}", pageable.getPageSize(), pageable.getOffset());
		int total = (int) (long) sessionFactory.getCurrentSession()
				.createQuery("SELECT COUNT(s) FROM Student s")
				.uniqueResult();
		List<Student> students = sessionFactory.getCurrentSession()
				.createQuery("FROM Student", Student.class)
				.setFirstResult((int)pageable.getOffset())
				.setMaxResults(pageable.getPageSize())
				.list();

		return new PageImpl<>(students, pageable, total);
	}

	@Override
	public Optional<Student> findById(int id) {
		logger.debug("Find student by id: {}", id);

		return findOrEmpty(
				() -> sessionFactory.getCurrentSession()
						.createQuery("FROM Student WHERE id=:id", Student.class)
						.setParameter("id", id)
						.uniqueResult());
	}

	@Override
	public void save(Student student) {
		logger.debug("Save student {}", student);
		Session session = sessionFactory.getCurrentSession();

		if (student.getId() == 0) {
			session.save(student);
			logger.debug("New student created with id: {}", student.getId());
		} else {
			session.merge(student);
			logger.debug("Student with id {} was updated", student.getId());
		}
	}

	@Override
	public void deleteById(int id) {
		Session session = sessionFactory.getCurrentSession();
		session.delete(session.get(Student.class, id));
		logger.debug("Student with id {} was deleted", id);
	}

	@Override
	public Optional<Student> findByFullNameAndBirthDate(String firstName, String lastName, LocalDate birthDate) {
		logger.debug("Find student with first name: {}, last name: {} and birthDate {}", firstName, lastName,
				birthDate);

		return findOrEmpty(
				() -> sessionFactory.getCurrentSession()
						.createQuery(
								"FROM Student WHERE firstName=:first_name AND lastName=:last_name AND birthDate=:birth_date",
								Student.class)
						.setParameter("first_name", firstName)
						.setParameter("last_name", lastName)
						.setParameter("birth_date", birthDate)
						.uniqueResult());
	}

	@Override
	public List<Student> findByGroupId(int id) {
		logger.debug("Find students with group id {}", id);

		return sessionFactory.getCurrentSession()
				.createQuery("FROM Student WHERE group.id=:group_id", Student.class)
				.setParameter("group_id", id)
				.list();
	}
}
