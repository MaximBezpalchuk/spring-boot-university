package com.foxminded.university.dao.hibernate;

import com.foxminded.university.config.TestConfig;
import com.foxminded.university.dao.HolidayDao;
import com.foxminded.university.model.Cathedra;
import com.foxminded.university.model.Holiday;
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

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {TestConfig.class})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@Transactional
public class HibernateHolidayDaoTest {

    @Autowired
    private SessionFactory sessionFactory;
    @Autowired
    private HolidayDao holidayDao;

    @Test
    void whenFindAll_thenAllExistingHolidaysFound() {
        int expected = (int) (long) sessionFactory.getCurrentSession().createQuery("SELECT COUNT(h) FROM Holiday h").getSingleResult();
        List<Holiday> actual = holidayDao.findAll();

        assertEquals(actual.size(), expected);
    }

    @Test
    void givenPageable_whenFindPaginatedHolidays_thenHolidaysFound() {
        List<Holiday> holidays = Arrays.asList(Holiday.builder()
                .id(1)
                .name("Christmas")
                .date(LocalDate.of(2021, 12, 25))
                .cathedra(sessionFactory.getCurrentSession().get(Cathedra.class, 1))
                .build());
        Page<Holiday> expected = new PageImpl<>(holidays, PageRequest.of(0, 1), 6);
        Page<Holiday> actual = holidayDao.findPaginatedHolidays(PageRequest.of(0, 1));

        assertEquals(expected, actual);
    }

    @Test
    void givenExistingHoliday_whenFindById_thenHolidayFound() {
        Optional<Holiday> expected = Optional.of(Holiday.builder()
                .id(1)
                .name("Christmas")
                .date(LocalDate.of(2021, 12, 25))
                .cathedra(sessionFactory.getCurrentSession().get(Cathedra.class, 1))
                .build());
        Optional<Holiday> actual = holidayDao.findById(1);

        assertEquals(expected, actual);
    }

    @Test
    void givenNotExistingHoliday_whenFindById_thenReturnEmptyOptional() {
        assertEquals(holidayDao.findById(100), Optional.empty());
    }

    @Test
    void givenNewHoliday_whenSaveHoliday_thenAllExistingHolidaysFound() {
        Holiday expected = Holiday.builder()
                .name("Christmas2")
                .date(LocalDate.of(2021, 12, 25))
                .cathedra(sessionFactory.getCurrentSession().get(Cathedra.class, 1))
                .build();
        holidayDao.save(expected);
        Holiday actual = sessionFactory.getCurrentSession().get(Holiday.class, 7);

        assertEquals(expected, actual);
    }

    @Test
    void givenExistingHoliday_whenSaveWithChanges_thenChangesApplied() {
        Holiday expected = sessionFactory.getCurrentSession().get(Holiday.class, 1);
        expected.setName("Test Name");
        holidayDao.save(expected);
        Holiday actual = sessionFactory.getCurrentSession().get(Holiday.class, 1);

        assertEquals(expected, actual);
    }

    @Test
    void whenDeleteExistingHoliday_thenHolidayDeleted() {
        holidayDao.deleteById(2);
        Holiday actual = sessionFactory.getCurrentSession().get(Holiday.class, 2);

        assertNull(actual);
    }

    @Test
    void givenHolidayName_whenFindByNameAndDate_thenHolidayFound() {
        Optional<Holiday> expected = Optional.of(Holiday.builder()
                .id(1)
                .name("Christmas")
                .date(LocalDate.of(2021, 12, 25))
                .cathedra(sessionFactory.getCurrentSession().get(Cathedra.class, 1))
                .build());
        Optional<Holiday> actual = holidayDao.findByNameAndDate("Christmas", LocalDate.of(2021, 12, 25));

        assertEquals(expected, actual);
    }

    @Test
    void givenHolidayName_whenFindByDate_thenHolidayFound() {
        List<Holiday> expected = Arrays.asList(Holiday.builder()
                .id(1)
                .name("Christmas")
                .date(LocalDate.of(2021, 12, 25))
                .cathedra(sessionFactory.getCurrentSession().get(Cathedra.class, 1))
                .build());
        List<Holiday> actual = holidayDao.findByDate(LocalDate.of(2021, 12, 25));

        assertEquals(expected, actual);
    }
}