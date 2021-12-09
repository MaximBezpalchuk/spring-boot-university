package com.foxminded.university.dao.hibernate;

import com.foxminded.university.config.TestConfig;
import com.foxminded.university.dao.SubjectDao;
import com.foxminded.university.model.Cathedra;
import com.foxminded.university.model.Subject;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {TestConfig.class})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@Transactional
public class HibernateSubjectDaoTest {

    @Autowired
    private SessionFactory sessionFactory;
    @Autowired
    private SubjectDao subjectDao;

    @Test
    void whenFindAll_thenAllExistingSubjectsFound() {
        int expected = (int) (long) sessionFactory.getCurrentSession().createQuery("SELECT COUNT(s) FROM Subject s").getSingleResult();
        List<Subject> actual = subjectDao.findAll();

        assertEquals(actual.size(), expected);
    }

    @Test
    void givenPageable_whenFindPaginatedSubjects_thenSubjectsFound() {
        List<Subject> subjects = Arrays.asList(Subject.builder()
                .cathedra(sessionFactory.getCurrentSession().get(Cathedra.class, 1))
                .name("Weapon Tactics")
                .description("Learning how to use heavy weapon and guerrilla tactics")
                .id(1)
                .build());
        Page<Subject> expected = new PageImpl<>(subjects, PageRequest.of(0, 1), 3);
        Page<Subject> actual = subjectDao.findPaginatedSubjects(PageRequest.of(0, 1));

        assertEquals(expected, actual);
    }

    @Test
    void givenExistingSubject_whenFindById_thenSubjectFound() {
        Optional<Subject> expected = Optional.of(Subject.builder()
                .cathedra(sessionFactory.getCurrentSession().get(Cathedra.class, 1))
                .name("Weapon Tactics")
                .description("Learning how to use heavy weapon and guerrilla tactics")
                .id(1)
                .build());
        Optional<Subject> actual = subjectDao.findById(1);

        assertEquals(expected, actual);
    }

    @Test
    void givenNotExistingSubject_whenFindById_thenReturnEmptyOptional() {
        assertEquals(subjectDao.findById(100), Optional.empty());
    }

    @Test
    void givenNewSubject_whenSaveSubject_thenAllExistingSubjectsFound() {
        Subject expected = Subject.builder()
                .cathedra(sessionFactory.getCurrentSession().get(Cathedra.class, 1))
                .name("Weapon Tactics123")
                .description("Learning how to use heavy weapon and guerrilla tactics123")
                .build();
        subjectDao.save(expected);
        Subject actual = sessionFactory.getCurrentSession().get(Subject.class, 4);

        assertEquals(expected, actual);
    }

    @Test
    void givenExistingSubject_whenSaveWithChanges_thenChangesApplied() {
        Subject expected = sessionFactory.getCurrentSession().get(Subject.class, 1);
        expected.setName("Test Name");
        subjectDao.save(expected);
        Subject actual = sessionFactory.getCurrentSession().get(Subject.class, 1);

        assertEquals(expected, actual);
    }

    @Test
    void whenDeleteExistingSubject_thenSubjectDeleted() {
        subjectDao.delete(Subject.builder().id(2).build());
        Subject actual = sessionFactory.getCurrentSession().get(Subject.class, 2);

        assertNull(actual);
    }

    @Test
    void givenSubjectName_whenFindByName_thenSubjectFound() {
        Optional<Subject> expected = Optional.of(Subject.builder()
                .cathedra(sessionFactory.getCurrentSession().get(Cathedra.class, 1))
                .name("Weapon Tactics")
                .description("Learning how to use heavy weapon and guerrilla tactics")
                .id(1)
                .build());
        Optional<Subject> actual = subjectDao.findByName("Weapon Tactics");

        assertEquals(expected, actual);
    }
}
