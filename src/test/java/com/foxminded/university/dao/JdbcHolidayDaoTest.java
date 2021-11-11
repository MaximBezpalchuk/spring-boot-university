package com.foxminded.university.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.jdbc.JdbcTestUtils.countRowsInTable;
import static org.springframework.test.jdbc.JdbcTestUtils.countRowsInTableWhere;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.foxminded.university.config.TestConfig;
import com.foxminded.university.dao.jdbc.JdbcHolidayDao;
import com.foxminded.university.model.Cathedra;
import com.foxminded.university.model.Holiday;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = TestConfig.class)
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
	void givenPageable_whenFindPaginatedHolidays_thenHolidaysFound() {
		List<Holiday> holidays = Arrays.asList(Holiday.builder()
				.id(1)
				.name("Christmas")
				.date(LocalDate.of(2021, 12, 25))
				.cathedra(Cathedra.builder().id(1).name("Fantastic Cathedra").build())
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
				.cathedra(Cathedra.builder().id(1).name("Fantastic Cathedra").build())
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
		int expected = countRowsInTable(template, TABLE_NAME) + 1;
		Holiday holiday = Holiday.builder()
				.name("Christmas2")
				.date(LocalDate.of(2021, 12, 25))
				.cathedra(Cathedra.builder().id(1).name("Fantastic Cathedra").build())
				.build();
		holidayDao.save(holiday);

		assertEquals(expected, countRowsInTable(template, TABLE_NAME));
	}

	@Test
	void givenExitstingHoliday_whenSaveWithChanges_thenChangesApplied() {
		Holiday expected = Holiday.builder()
				.id(1)
				.name("Test Name")
				.date(LocalDate.of(2021, 12, 26))
				.cathedra(Cathedra.builder().id(1).build())
				.build();
		holidayDao.save(expected);

		assertEquals(1,
				countRowsInTableWhere(template, TABLE_NAME, "id = 1 AND name = 'Test Name' and date= '2021-12-26'"));
	}

	@Test
	void whenDeleteExistingHoliday_thenAllExistingHolidaysFound() {
		int expected = countRowsInTable(template, TABLE_NAME) - 1;
		holidayDao.deleteById(3);

		assertEquals(expected, countRowsInTable(template, TABLE_NAME));
	}

	@Test
	void givenHolidayName_whenFindByNameAndDate_thenHolidayFound() {
		Optional<Holiday> expected = Optional.of(Holiday.builder()
				.id(1)
				.name("Christmas")
				.date(LocalDate.of(2021, 12, 25))
				.cathedra(Cathedra.builder().id(1).name("Fantastic Cathedra").build())
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
				.cathedra(Cathedra.builder().id(1).name("Fantastic Cathedra").build())
				.build());
		List<Holiday> actual = holidayDao.findByDate(LocalDate.of(2021, 12, 25));

		assertEquals(expected, actual);
	}
}
