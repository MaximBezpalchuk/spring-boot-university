package com.foxminded.university.dao;

import com.foxminded.university.model.Cathedra;
import com.foxminded.university.model.Holiday;
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
public class HolidayRepositoryTest {

    @Autowired
    private EntityManager entityManager;
    @Autowired
    private HolidayRepository holidayRepository;

    @Test
    void whenFindAll_thenAllExistingHolidaysFound() {
        int expected = (int) (long) entityManager.createQuery("SELECT COUNT(h) FROM Holiday h").getSingleResult();
        List<Holiday> actual = holidayRepository.findAll();

        assertEquals(actual.size(), expected);
    }

    @Test
    void givenPageable_whenFindPaginatedHolidays_thenHolidaysFound() {
        List<Holiday> holidays = Arrays.asList(Holiday.builder()
            .id(1)
            .name("Christmas")
            .date(LocalDate.of(2021, 12, 25))
            .cathedra(entityManager.find(Cathedra.class, 1))
            .build());
        Page<Holiday> expected = new PageImpl<>(holidays, PageRequest.of(0, 1), 6);
        Page<Holiday> actual = holidayRepository.findAll(PageRequest.of(0, 1));

        assertEquals(expected, actual);
    }

    @Test
    void givenExistingHoliday_whenFindById_thenHolidayFound() {
        Optional<Holiday> expected = Optional.of(Holiday.builder()
            .id(1)
            .name("Christmas")
            .date(LocalDate.of(2021, 12, 25))
            .cathedra(entityManager.find(Cathedra.class, 1))
            .build());
        Optional<Holiday> actual = holidayRepository.findById(1);

        assertEquals(expected, actual);
    }

    @Test
    void givenNotExistingHoliday_whenFindById_thenReturnEmptyOptional() {
        assertEquals(holidayRepository.findById(100), Optional.empty());
    }

    @Test
    void givenNewHoliday_whenSaveHoliday_thenAllExistingHolidaysFound() {
        Holiday expected = Holiday.builder()
            .name("Christmas2")
            .date(LocalDate.of(2021, 12, 25))
            .cathedra(entityManager.find(Cathedra.class, 1))
            .build();
        holidayRepository.save(expected);
        Holiday actual = entityManager.find(Holiday.class, 7);

        assertEquals(expected, actual);
    }

    @Test
    void givenExistingHoliday_whenSaveWithChanges_thenChangesApplied() {
        Holiday expected = entityManager.find(Holiday.class, 1);
        expected.setName("Test Name");
        holidayRepository.save(expected);
        Holiday actual = entityManager.find(Holiday.class, 1);

        assertEquals(expected, actual);
    }

    @Test
    void whenDeleteExistingHoliday_thenHolidayDeleted() {
        holidayRepository.delete(Holiday.builder().id(2).build());
        Holiday actual = entityManager.find(Holiday.class, 2);

        assertNull(actual);
    }

    @Test
    void givenHolidayName_whenFindByNameAndDate_thenHolidayFound() {
        Optional<Holiday> expected = Optional.of(Holiday.builder()
            .id(1)
            .name("Christmas")
            .date(LocalDate.of(2021, 12, 25))
            .cathedra(entityManager.find(Cathedra.class, 1))
            .build());
        Optional<Holiday> actual = holidayRepository.findByNameAndDate("Christmas", LocalDate.of(2021, 12, 25));

        assertEquals(expected, actual);
    }

    @Test
    void givenHolidayName_whenFindByDate_thenHolidayFound() {
        List<Holiday> expected = Arrays.asList(Holiday.builder()
            .id(1)
            .name("Christmas")
            .date(LocalDate.of(2021, 12, 25))
            .cathedra(entityManager.find(Cathedra.class, 1))
            .build());
        List<Holiday> actual = holidayRepository.findByDate(LocalDate.of(2021, 12, 25));

        assertEquals(expected, actual);
    }
}