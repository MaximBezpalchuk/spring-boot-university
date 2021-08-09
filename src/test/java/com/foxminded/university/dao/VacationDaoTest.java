package com.foxminded.university.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.jdbc.JdbcTestUtils.countRowsInTable;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.MethodMode;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.foxminded.university.model.Vacation;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = { SpringTestConfig.class })
public class VacationDaoTest {

	private final static String TABLE_NAME = "vacations";
	@Autowired
	private JdbcTemplate template;
	@Autowired
	private VacationDao vacationDao;
	@Autowired
	private TeacherDao teacherDao;

	@Test
	void whenFindAll_thenAllExistingVacationsFound() {
		int expected = countRowsInTable(template, TABLE_NAME);
		int actual = vacationDao.findAll().size();

		assertEquals(expected, actual);
	}

	@Test
	void givenExistingVacation_whenFindById_thenVacationFound() {
		Vacation expected = new Vacation(LocalDate.of(2021, 1, 15), LocalDate.of(2021, 1, 29), teacherDao.findById(1));
		expected.setId(1);
		Vacation actual = vacationDao.findById(1);

		assertEquals(expected, actual);
	}

	@Test
	void givenNotExistingVacation_whenFindOne_thenIncorrestResultSize() {
		Exception exception = assertThrows(EmptyResultDataAccessException.class, () -> {
			vacationDao.findById(100);
		});
		String expectedMessage = "Incorrect result size";
		String actualMessage = exception.getMessage();

		assertTrue(actualMessage.contains(expectedMessage));
	}

	@DirtiesContext(methodMode = MethodMode.AFTER_METHOD)
	@Test
	void givenNewVacation_whenSaveVacation_thenAllExistingVacationsFound() {
		int expected = countRowsInTable(template, TABLE_NAME) + 1;
		vacationDao.save(new Vacation(LocalDate.of(2021, 1, 31), LocalDate.of(2021, 3, 29), teacherDao.findById(2)));

		assertEquals(expected, countRowsInTable(template, TABLE_NAME));
	}

	@DirtiesContext(methodMode = MethodMode.AFTER_METHOD)
	@Test
	void givenExitstingVacation_whenChange_thenAllExistingVacationsFound() {
		int expected = countRowsInTable(template, TABLE_NAME);
		Vacation vacation = vacationDao.findById(1);
		vacation.setTeacher(teacherDao.findById(2));
		vacationDao.save(vacation);
		int actual = countRowsInTable(template, TABLE_NAME);

		assertEquals(expected, actual);
	}

	@DirtiesContext(methodMode = MethodMode.AFTER_METHOD)
	@Test
	void whenDeleteExistingVacation_thenAllExistingVacationsFound() {
		int expected = countRowsInTable(template, TABLE_NAME) - 1;
		vacationDao.deleteById(1);

		assertEquals(expected, countRowsInTable(template, TABLE_NAME));
	}

	@Test
	void givenExistingVacation_whenFindByTeacherId_thenVacationFound() {
		List<Vacation> expected = new ArrayList<>();
		Vacation vacation1 = new Vacation(LocalDate.of(2021, 1, 15), LocalDate.of(2021, 1, 29), teacherDao.findById(1));
		vacation1.setId(1);
		Vacation vacation2 = new Vacation(LocalDate.of(2021, 6, 15), LocalDate.of(2021, 6, 29), teacherDao.findById(1));
		vacation2.setId(2);
		expected.add(vacation1);
		expected.add(vacation2);
		List<Vacation> actual = vacationDao.findByTeacherId(1);
		assertEquals(expected, actual);
	}
}
