package com.foxminded.university.dao;

import com.foxminded.university.model.Teacher;
import com.foxminded.university.model.Vacation;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@Transactional
public class VacationDaoTest {

    @Autowired
    private EntityManager entityManager;
    @Autowired
    private VacationDao vacationDao;

    @Test
    void whenFindAll_thenAllExistingVacationsFound() {
        int expected = (int) (long) entityManager.createQuery("SELECT COUNT(v) FROM Vacation v").getSingleResult();
        List<Vacation> actual = vacationDao.findAll();

        assertEquals(actual.size(), expected);
    }

    @Test
    void givenPageable_whenFindPaginatedVacations_thenVacationsFound() {
        Teacher teacher = entityManager.find(Teacher.class, 1);
        List<Vacation> vacations = Arrays.asList(Vacation.builder()
            .id(1)
            .start(LocalDate.of(2021, 1, 15))
            .end(LocalDate.of(2021, 1, 29))
            .teacher(teacher)
            .build());
        Page<Vacation> expected = new PageImpl<>(vacations, PageRequest.of(0, 1), 2);
        Page<Vacation> actual = vacationDao.findAllByTeacherId(PageRequest.of(0, 1), 1);

        assertEquals(expected, actual);
    }

    @Test
    void givenExistingVacation_whenFindById_thenVacationFound() {
        Teacher teacher = entityManager.find(Teacher.class, 1);
        Optional<Vacation> expected = Optional.of(Vacation.builder()
            .id(1)
            .start(LocalDate.of(2021, 1, 15))
            .end(LocalDate.of(2021, 1, 29))
            .teacher(teacher)
            .build());
        Optional<Vacation> actual = vacationDao.findById(1);

        assertEquals(expected, actual);
    }

    @Test
    void givenNotExistingVacation_whenFindById_thenReturnEmptyOptional() {
        assertEquals(vacationDao.findById(100), Optional.empty());
    }

    @Test
    void givenNewVacation_whenSaveVacation_thenAllExistingVacationsFound() {
        Vacation expected = Vacation.builder()
            .start(LocalDate.of(2021, 1, 31))
            .end(LocalDate.of(2021, 3, 29))
            .teacher(entityManager.find(Teacher.class, 1))
            .build();
        vacationDao.save(expected);
        Vacation actual = entityManager.find(Vacation.class, 5);

        assertEquals(expected, actual);
    }

    @Test
    void givenExistingVacation_whenChange_thenChangesApplied() {
        Vacation expected = Vacation.builder()
            .id(1)
            .start(LocalDate.of(2021, 1, 1))
            .end(LocalDate.of(2021, 1, 1))
            .teacher(entityManager.find(Teacher.class, 2))
            .build();
        vacationDao.save(expected);
        Vacation actual = entityManager.find(Vacation.class, 1);

        assertEquals(expected, actual);
    }

    @Test
    void whenDeleteExistingVacation_thenVacationDeleted() {
        vacationDao.delete(Vacation.builder().id(2).build());
        Vacation actual = entityManager.find(Vacation.class, 2);

        assertNull(actual);
    }

    @Test
    void givenExistingVacation_whenFindByTeacherId_thenVacationFound() {
        Vacation vacation1 = Vacation.builder()
            .id(1)
            .start(LocalDate.of(2021, 1, 15))
            .end(LocalDate.of(2021, 1, 29))
            .teacher(entityManager.find(Teacher.class, 1))
            .build();
        Vacation vacation2 = Vacation.builder()
            .id(2)
            .start(LocalDate.of(2021, 6, 15))
            .end(LocalDate.of(2021, 6, 29))
            .teacher(entityManager.find(Teacher.class, 1))
            .build();
        List<Vacation> expected = Arrays.asList(vacation1, vacation2);
        List<Vacation> actual = vacationDao.findByTeacherId(1);

        assertEquals(expected, actual);
    }

    @Test
    void givenStartAndEndAndTeacher_whenFindByPeriodAndTeacher_thenVacationFound() {
        Teacher teacher = entityManager.find(Teacher.class, 1);
        Optional<Vacation> expected = Optional.of(Vacation.builder()
            .id(1)
            .start(LocalDate.of(2021, 1, 15))
            .end(LocalDate.of(2021, 1, 29))
            .teacher(teacher)
            .build());
        Optional<Vacation> actual = vacationDao.findByPeriodAndTeacher(expected.get().getStart(),
            expected.get().getEnd(), teacher);

        assertEquals(expected, actual);
    }

    @Test
    void givenTeacherAndYear_whenFindByTeacherAndYear_thenVacationFound() {
        Teacher teacher = entityManager.find(Teacher.class, 1);
        Vacation vacation1 = Vacation.builder()
            .id(1)
            .start(LocalDate.of(2021, 1, 15))
            .end(LocalDate.of(2021, 1, 29))
            .teacher(teacher)
            .build();
        Vacation vacation2 = Vacation.builder()
            .id(2)
            .start(LocalDate.of(2021, 6, 15))
            .end(LocalDate.of(2021, 6, 29))
            .teacher(teacher)
            .build();
        List<Vacation> actual = vacationDao.findByTeacherIdAndYear(1, 2021);

        assertEquals(Arrays.asList(vacation1, vacation2), actual);
    }
}