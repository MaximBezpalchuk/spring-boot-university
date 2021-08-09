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
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.foxminded.university.model.Cathedra;
import com.foxminded.university.model.Holiday;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = { SpringTestConfig.class })
public class HolidayDaoTest {

	private final static String TABLE_NAME = "holidays";
	@Autowired
	private JdbcTemplate template;
	@Autowired
	private HolidayDao holidayDao;
	@Autowired
	private CathedraDao cathedraDao;

	@Test
	void whenFindAll_thenAllExistingAudiencesFound() {
		int expected = countRowsInTable(template, TABLE_NAME);
		int actual = holidayDao.findAll().size();

		assertEquals(expected, actual);
	}

	@Test
	void givenExistingAudience_whenFindById_thenAudinceFound() {
		Cathedra cathedra = cathedraDao.findById(1);
		Holiday expected = new Holiday("Christmas", LocalDate.of(2021, 12, 25), cathedra);
		expected.setId(1);
		Holiday actual = holidayDao.findById(1);

		assertEquals(expected, actual);
	}

	@Test
	void givenNotExistingAudience_whenFindOne_thenIncorrestResultSize() {
		Exception exception = assertThrows(EmptyResultDataAccessException.class, () -> {
			holidayDao.findById(100);
		});
		String expectedMessage = "Incorrect result size";
		String actualMessage = exception.getMessage();

		assertTrue(actualMessage.contains(expectedMessage));
	}

	@Test
	void givenNewAudience_whenSaveAudience_thenAllExistingAudiencesFound() {
		int expected = countRowsInTable(template, TABLE_NAME) + 1;
		Cathedra cathedra = cathedraDao.findById(1);
		Holiday holiday = new Holiday("Christmas2", LocalDate.of(2021, 12, 25), cathedra);
		holidayDao.save(holiday, cathedra);

		assertEquals(expected, countRowsInTable(template, TABLE_NAME));
	}

	@Test
	void givenExitstingAudience_whenChange_thenAllExistingAudiencesFound() {
		int expected = countRowsInTable(template, TABLE_NAME);
		Holiday holiday = holidayDao.findById(1);
		holiday.setName("Test Name");
		Cathedra cathedra = cathedraDao.findById(1);
		holidayDao.save(holiday, cathedra);
		int actual = countRowsInTable(template, TABLE_NAME);

		assertEquals(expected, actual);
	}

	@Test
	void whenDeleteExistingAudience_thenAllExistingAudiencesFound() {
		int expected = countRowsInTable(template, TABLE_NAME) - 1;
		holidayDao.deleteById(3);

		assertEquals(expected, countRowsInTable(template, TABLE_NAME));
	}
}
