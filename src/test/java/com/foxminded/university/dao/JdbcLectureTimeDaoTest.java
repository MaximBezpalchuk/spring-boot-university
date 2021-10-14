package com.foxminded.university.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.jdbc.JdbcTestUtils.countRowsInTable;
import static org.springframework.test.jdbc.JdbcTestUtils.countRowsInTableWhere;

import java.time.LocalTime;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.foxminded.university.config.WebMvcTestConfig;
import com.foxminded.university.dao.jdbc.JdbcLectureTimeDao;
import com.foxminded.university.model.LectureTime;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = WebMvcTestConfig.class)
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
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
		Optional<LectureTime> expected = Optional.of(LectureTime.builder()
				.id(1).start(LocalTime.of(8, 0, 0))
				.end(LocalTime.of(9, 30, 0))
				.build());
		Optional<LectureTime> actual = lectureTimeDao.findById(1);

		assertEquals(expected, actual);
	}

	@Test
	void givenNotExistingLectureTime_whenFindById_thenReturnEmptyOptional() {
		assertEquals(lectureTimeDao.findById(100), Optional.empty());
	}

	@Test
	void givenNewLectureTime_whenSaveLectureTime_thenAllExistingLectureTimesFound() {
		int expected = countRowsInTable(template, TABLE_NAME) + 1;
		lectureTimeDao.save(LectureTime.builder()
				.start(LocalTime.of(21, 0, 0))
				.end(LocalTime.of(22, 30, 0))
				.build());

		assertEquals(expected, countRowsInTable(template, TABLE_NAME));
	}

	@Test
	void givenExitstingLectureTime_whenSaveWithChanges_thenChangesApplied() {
		LectureTime expected = LectureTime.builder()
				.id(1)
				.start(LocalTime.of(21, 0, 0))
				.end(LocalTime.of(21, 0, 0))
				.build();
		lectureTimeDao.save(expected);
		assertEquals(1, countRowsInTableWhere(template, TABLE_NAME, 
				"id = 1 "
				+ "AND start = '21:00:00' "
				+ "AND finish = '21:00:00'"));
	}

	@Test
	void whenDeleteExistingLectureTime_thenAllExistingLectureTimesFound() {
		int expected = countRowsInTable(template, TABLE_NAME) - 1;
		lectureTimeDao.deleteById(3);

		assertEquals(expected, countRowsInTable(template, TABLE_NAME));
	}
	
	@Test
	void givenPeriod_whenFindByPeriod_thenLectureTimeFound() {
		Optional<LectureTime> expected = Optional.of(LectureTime.builder()
				.id(1).start(LocalTime.of(8, 0, 0))
				.end(LocalTime.of(9, 30, 0))
				.build());
		Optional<LectureTime> actual = lectureTimeDao.findByPeriod(expected.get().getStart(), expected.get().getEnd());

		assertEquals(expected, actual);
	}
}
