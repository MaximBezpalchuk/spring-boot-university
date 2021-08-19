package com.foxminded.university.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.jdbc.JdbcTestUtils.countRowsInTable;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.foxminded.university.config.SpringTestConfig;
import com.foxminded.university.dao.jdbc.JdbcHolidayDao;
import com.foxminded.university.model.Holiday;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = SpringTestConfig.class)
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
public class JdbcHolidayDaoTest {

	private final static String TABLE_NAME = "holidays";
	@Autowired
	private JdbcTemplate template;
	@Autowired
	private JdbcHolidayDao holidayDao;

	@Test
	void whenFindAll_thenAllExistingHolidaysFound() {
		int expected = countRowsInTable(template, TABLE_NAME);
		int actual = holidayDao.findAll().size();

		assertEquals(expected, actual);
	}

	@Test
	void givenExistingHoliday_whenFindById_thenHolidayFound() {
		Holiday actual = holidayDao.findById(1);
		Holiday expected = Holiday.builder()
				.id(1)
				.name("Christmas")
				.date(LocalDate.of(2021, 12, 25))
				.cathedra(actual.getCathedra())
				.build();

		assertEquals(expected, actual);
	}

	@Test
	void givenNotExistingHoliday_whenFindOne_thenIncorrestResultSize() {
		Exception exception = assertThrows(EmptyResultDataAccessException.class, () -> {
			holidayDao.findById(100);
		});
		String expectedMessage = "Incorrect result size";
		String actualMessage = exception.getMessage();

		assertTrue(actualMessage.contains(expectedMessage));
	}

	@Test
	void givenNewHoliday_whenSaveHoliday_thenAllExistingHolidaysFound() {
		int expected = countRowsInTable(template, TABLE_NAME) + 1;
		Holiday actual = holidayDao.findById(1);
		Holiday holiday = Holiday.builder()
				.name("Christmas2")
				.date(LocalDate.of(2021, 12, 25))
				.cathedra(actual.getCathedra())
				.build();
		holidayDao.save(holiday);

		assertEquals(expected, countRowsInTable(template, TABLE_NAME));
	}

	@Test
	void givenExitstingHoliday_whenChange_thenChangesApplied() {
		Holiday expected = holidayDao.findById(1);
		expected.setName("Test Name");
		holidayDao.save(expected);
		Holiday actual = holidayDao.findById(1);

		assertEquals(expected, actual);
	}

	@Test
	void whenDeleteExistingHoliday_thenAllExistingHolidaysFound() {
		int expected = countRowsInTable(template, TABLE_NAME) - 1;
		holidayDao.deleteById(3);

		assertEquals(expected, countRowsInTable(template, TABLE_NAME));
	}
}
