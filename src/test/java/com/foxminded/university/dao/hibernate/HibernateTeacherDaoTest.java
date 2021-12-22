package com.foxminded.university.dao.hibernate;

import com.foxminded.university.dao.TeacherDao;
import com.foxminded.university.model.*;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@Transactional
public class HibernateTeacherDaoTest {

    @Autowired
    private SessionFactory sessionFactory;
    @Autowired
    private TeacherDao teacherDao;

    @Test
    void whenFindAll_thenAllExistingTeachersFound() {
        int expected = (int) (long) sessionFactory.getCurrentSession().createQuery("SELECT COUNT(t) FROM Teacher t").getSingleResult();
        List<Teacher> actual = teacherDao.findAll();

        assertEquals(actual.size(), expected);
    }

    @Test
    void givenPageable_whenFindPaginatedTeachers_thenTeachersFound() {
        Cathedra cathedra = sessionFactory.getCurrentSession().get(Cathedra.class, 1);
        Subject subject = sessionFactory.getCurrentSession().get(Subject.class, 1);
        List<Teacher> teachers = Arrays.asList(Teacher.builder()
            .firstName("Daniel")
            .lastName("Morpheus")
            .address("Virtual Reality Capsule no 1")
            .gender(Gender.MALE)
            .birthDate(LocalDate.of(1970, 1, 1))
            .cathedra(cathedra)
            .degree(Degree.PROFESSOR)
            .phone("1")
            .email("1@bigowl.com")
            .postalCode("12345")
            .education("Higher education")
            .id(1)
            .subjects(Arrays.asList(subject))
            .build());
        Page<Teacher> expected = new PageImpl<>(teachers, PageRequest.of(0, 1), 2);
        Page<Teacher> actual = teacherDao.findPaginatedTeachers(PageRequest.of(0, 1));

        assertEquals(expected, actual);
    }

    @Test
    void givenExistingTeacher_whenFindById_thenTeacherFound() {
        Optional<Teacher> expected = Optional.of(Teacher.builder()
            .firstName("Daniel")
            .lastName("Morpheus")
            .address("Virtual Reality Capsule no 1")
            .gender(Gender.MALE)
            .birthDate(LocalDate.of(1970, 1, 1))
            .cathedra(sessionFactory.getCurrentSession().get(Cathedra.class, 1))
            .degree(Degree.PROFESSOR)
            .phone("1")
            .email("1@bigowl.com")
            .postalCode("12345")
            .education("Higher education")
            .subjects(Arrays.asList(sessionFactory.getCurrentSession().get(Subject.class, 1)))
            .id(1)
            .build());
        Optional<Teacher> actual = teacherDao.findById(1);

        assertEquals(expected, actual);
    }

    @Test
    void givenNotExistingTeacher_whenFindById_thenReturnEmptyOptional() {
        assertEquals(teacherDao.findById(100), Optional.empty());
    }

    @Test
    void givenNewTeacher_whenSaveTeacher_thenAllExistingTeachersFound() {
        Teacher expected = Teacher.builder()
            .firstName("Test")
            .lastName("Test")
            .address("Virtual Reality Capsule no 1")
            .gender(Gender.MALE)
            .birthDate(LocalDate.of(1970, 1, 1))
            .cathedra(sessionFactory.getCurrentSession().get(Cathedra.class, 1))
            .degree(Degree.PROFESSOR)
            .phone("1")
            .email("1@bigowl.com")
            .postalCode("12345")
            .education("Higher education")
            .subjects(Arrays.asList(sessionFactory.getCurrentSession().get(Subject.class, 1)))
            .build();
        teacherDao.save(expected);
        Teacher actual = sessionFactory.getCurrentSession().get(Teacher.class, 3);

        assertEquals(expected, actual);
    }

    @Test
    void givenExistingTeacher_whenSaveWithChanges_thenChangesApplied() {
        Teacher expected = Teacher.builder()
            .id(1)
            .firstName("TestName")
            .lastName("Test")
            .address("Virtual Reality Capsule no 2")
            .gender(Gender.MALE)
            .birthDate(LocalDate.of(1970, 1, 1))
            .cathedra(sessionFactory.getCurrentSession().get(Cathedra.class, 1))
            .degree(Degree.PROFESSOR)
            .phone("1")
            .email("123@bigowl.com")
            .postalCode("1234567")
            .education("Higher education123")
            .subjects(Arrays.asList(sessionFactory.getCurrentSession().get(Subject.class, 1)))
            .build();
        teacherDao.save(expected);
        Teacher actual = sessionFactory.getCurrentSession().get(Teacher.class, 1);

        assertEquals(expected, actual);
    }

    @Test
    void whenDeleteExistingTeacher_thenTeacherDeleted() {
        teacherDao.delete(Teacher.builder().id(2).build());
        Teacher actual = sessionFactory.getCurrentSession().get(Teacher.class, 2);

        assertNull(actual);
    }

    @Test
    void givenFirstNameAndLastNameAndBirthDate_whenFindByFullNameAndBirthDate_thenTeacherFound() {
        Optional<Teacher> expected = Optional.of(Teacher.builder()
            .firstName("Daniel")
            .lastName("Morpheus")
            .address("Virtual Reality Capsule no 1")
            .gender(Gender.MALE)
            .birthDate(LocalDate.of(1970, 1, 1))
            .cathedra(sessionFactory.getCurrentSession().get(Cathedra.class, 1))
            .degree(Degree.PROFESSOR)
            .phone("1")
            .email("1@bigowl.com")
            .postalCode("12345")
            .education("Higher education")
            .id(1)
            .subjects(Arrays.asList(sessionFactory.getCurrentSession().get(Subject.class, 1)))
            .build());
        Optional<Teacher> actual = teacherDao.findByFullNameAndBirthDate(expected.get().getFirstName(),
            expected.get().getLastName(), expected.get().getBirthDate());

        assertEquals(expected, actual);
    }
}
