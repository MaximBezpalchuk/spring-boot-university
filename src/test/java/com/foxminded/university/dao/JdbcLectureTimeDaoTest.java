package com.foxminded.university.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.jdbc.JdbcTestUtils.countRowsInTable;

import java.time.LocalTime;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.MethodMode;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.foxminded.university.config.SpringTestConfig;
import com.foxminded.university.dao.jdbc.JdbcLectureTimeDao;
import com.foxminded.university.model.LectureTime;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = SpringTestConfig.class)
public class JdbcLectureTimeDaoTest {

	private final static String TABLE_NAME = "lecture_times";
	@Autowired
	private JdbcTemplate template;
	@Autowired
	private JdbcLectureTimeDao lectureTimeDao;

	@Test
	void whenFindAll_thenAllExistingLectureTimesFound() {
		int expected = countRowsInTable(template, TABLE_NAME);
		int actual = lectureTimeDao.findAll().size();

		assertEquals(expected, actual);
	}

	@Test
	void givenExistingLectureTime_whenFindById_thenLectureTimeFound() {
		LectureTime expected = LectureTime.builder().id(1).start(LocalTime.of(8, 0, 0)).end(LocalTime.of(9, 30, 0))
				.build();
		LectureTime actual = lectureTimeDao.findById(1);

		assertEquals(expected, actual);
	}

	@Test
	void givenNotExistingLectureTime_whenFindOne_thenIncorrestResultSize() {
		Exception exception = assertThrows(EmptyResultDataAccessException.class, () -> {
			lectureTimeDao.findById(100);
		});
		String expectedMessage = "Incorrect result size";
		String actualMessage = exception.getMessage();

		assertTrue(actualMessage.contains(expectedMessage));
	}

	@DirtiesContext(methodMode = MethodMode.AFTER_METHOD)
	@Test
	void givenNewLectureTime_whenSaveLectureTime_thenAllExistingLectureTimesFound() {
		int expected = countRowsInTable(template, TABLE_NAME) + 1;
		lectureTimeDao.save(LectureTime.builder().start(LocalTime.of(21, 0, 0)).end(LocalTime.of(22, 30, 0)).build());

		assertEquals(expected, countRowsInTable(template, TABLE_NAME));
	}

	@DirtiesContext(methodMode = MethodMode.AFTER_METHOD)
	@Test
	void givenExitstingLectureTime_whenChange_thenChangesApplied() {
		LectureTime expected = lectureTimeDao.findById(1);
		expected.setStart(LocalTime.of(21, 0, 0));
		lectureTimeDao.save(expected);
		LectureTime actual = lectureTimeDao.findById(1);

		assertEquals(expected, actual);
	}

	@DirtiesContext(methodMode = MethodMode.AFTER_METHOD)
	@Test
	void whenDeleteExistingLectureTime_thenAllExistingLectureTimesFound() {
		int expected = countRowsInTable(template, TABLE_NAME) - 1;
		lectureTimeDao.deleteById(3);

		assertEquals(expected, countRowsInTable(template, TABLE_NAME));
	}

}
