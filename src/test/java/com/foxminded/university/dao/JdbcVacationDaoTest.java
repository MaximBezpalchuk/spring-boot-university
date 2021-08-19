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
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.foxminded.university.config.SpringTestConfig;
import com.foxminded.university.dao.jdbc.JdbcVacationDao;
import com.foxminded.university.model.Teacher;
import com.foxminded.university.model.Vacation;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = SpringTestConfig.class)
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
public class JdbcVacationDaoTest {

	private final static String TABLE_NAME = "vacations";
	@Autowired
	private JdbcTemplate template;
	@Autowired
	private JdbcVacationDao vacationDao;

	@Test
	void whenFindAll_thenAllExistingVacationsFound() {
		int expected = countRowsInTable(template, TABLE_NAME);
		int actual = vacationDao.findAll().size();

		assertEquals(expected, actual);
	}

	@Test
	void givenExistingVacation_whenFindById_thenVacationFound() {
		Vacation actual = vacationDao.findById(1);
		Vacation expected = Vacation.builder()
				.id(1)
				.start(LocalDate.of(2021, 1, 15))
				.end(LocalDate.of(2021, 1, 29))
				.teacher(actual.getTeacher())
				.build();

		assertEquals(expected, actual);
	}

	@Test
	void givenNotExistingVacation_whenFindById_thenIncorrestResultSize() {
		Exception exception = assertThrows(EmptyResultDataAccessException.class, () -> {
			vacationDao.findById(100);
		});
		String expectedMessage = "Incorrect result size";
		String actualMessage = exception.getMessage();

		assertTrue(actualMessage.contains(expectedMessage));
	}

	@Test
	void givenNewVacation_whenSaveVacation_thenAllExistingVacationsFound() {
		int expected = countRowsInTable(template, TABLE_NAME) + 1;
		vacationDao.save(Vacation.builder()
				.start(LocalDate.of(2021, 1, 31))
				.end(LocalDate.of(2021, 3, 29))
				.teacher(Teacher.builder().id(1).build())
				.build());

		assertEquals(expected, countRowsInTable(template, TABLE_NAME));
	}

	@Test
	void givenExitstingVacation_whenChange_thenChangesApplied() {
		Vacation expected = Vacation.builder()
				.id(1)
				.start(LocalDate.of(2021, 1, 1))
				.end(LocalDate.of(2021, 1, 1))
				.teacher(Teacher.builder().id(2).build())
				.build();
		vacationDao.save(expected);
		int teacher_id = template.queryForObject("SELECT teacher_id FROM vacations WHERE id = 1", Integer.class);
		LocalDate start = template.queryForObject("SELECT start FROM vacations WHERE id = 1", LocalDate.class);
		LocalDate end = template.queryForObject("SELECT finish FROM vacations WHERE id = 1", LocalDate.class);

		assertEquals(expected.getStart(), start);
		assertEquals(expected.getEnd(), end);
		assertEquals(expected.getTeacher().getId(), teacher_id);
	}

	@Test
	void whenDeleteExistingVacation_thenAllExistingVacationsFound() {
		int expected = countRowsInTable(template, TABLE_NAME) - 1;
		vacationDao.deleteById(1);

		assertEquals(expected, countRowsInTable(template, TABLE_NAME));
	}

	@Test
	void givenExistingVacation_whenFindByTeacherId_thenVacationFound() {
		List<Vacation> expected = new ArrayList<>();
		List<Vacation> actual = vacationDao.findByTeacherId(1);
		Vacation vacation1 = Vacation.builder()
				.id(1)
				.start(LocalDate.of(2021, 1, 15))
				.end(LocalDate.of(2021, 1, 29))
				.teacher(actual.get(0).getTeacher())
				.build();
		Vacation vacation2 = Vacation.builder()
				.id(2)
				.start(LocalDate.of(2021, 6, 15))
				.end(LocalDate.of(2021, 6, 29))
				.teacher(actual.get(0).getTeacher())
				.build();
		expected.add(vacation1);
		expected.add(vacation2);

		assertEquals(expected, actual);
	}
}
