package com.foxminded.university.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.jdbc.JdbcTestUtils.countRowsInTable;
import static org.springframework.test.jdbc.JdbcTestUtils.countRowsInTableWhere;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
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

import com.foxminded.university.model.Audience;
import com.foxminded.university.model.Cathedra;
import com.foxminded.university.model.Lecture;
import com.foxminded.university.model.LectureTime;
import com.foxminded.university.model.Subject;
import com.foxminded.university.model.Teacher;
import com.foxminded.university.config.SpringTestConfig;
import com.foxminded.university.dao.jdbc.JdbcLectureDao;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = SpringTestConfig.class)
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
public class JdbcLectureDaoTest {

	private final static String TABLE_NAME = "lectures";
	@Autowired
	private JdbcTemplate template;
	@Autowired
	private JdbcLectureDao lectureDao;

	@Test
	void whenFindAll_thenAllExistingLecturesFound() {
		int expected = countRowsInTable(template, TABLE_NAME);
		int actual = lectureDao.findAll().size();

		assertEquals(expected, actual);
	}

	@Test
	void givenExistingLecture_whenFindById_thenLectureFound() {
		Lecture actual = lectureDao.findById(1);
		Lecture expected = Lecture.builder()
				.id(1)
				.group(actual.getGroups())
				.cathedra(actual.getCathedra())
				.subject(actual.getSubject())
				.date(LocalDate.of(2021, 4, 4))
				.time(actual.getTime())
				.audience(actual.getAudience())
				.teacher(actual.getTeacher())
				.build();

		assertEquals(expected, actual);
	}

	@Test
	void givenNotExistingLecture_whenFindById_thenIncorrestResultSize() {
		Exception exception = assertThrows(EmptyResultDataAccessException.class, () -> {
			lectureDao.findById(100);
		});
		String expectedMessage = "Incorrect result size";
		String actualMessage = exception.getMessage();

		assertTrue(actualMessage.contains(expectedMessage));
	}

	@Test
	void givenNewLecture_whenSaveLecture_thenAllExistingLecturesFound() {
		int expected = countRowsInTable(template, TABLE_NAME) + 1;
		lectureDao.save(
				Lecture.builder().cathedra(Cathedra.builder()
						.id(1).build())
				.subject(Subject.builder().id(1).build())
						.date(LocalDate.of(2021, 4, 4))
						.time(LectureTime.builder().id(1).build())
						.audience(Audience.builder().id(1).build())
						.teacher(Teacher.builder().id(1).build())
						.build());

		assertEquals(expected, countRowsInTable(template, TABLE_NAME));
	}

	@Test
	void givenExitstingLecture_whenSaveWithChanges_thenChangesApplied() {
		Lecture expected = Lecture.builder()
				.id(1)
				.cathedra(Cathedra.builder().id(1).build())
				.subject(Subject.builder().id(2).build())
				.date(LocalDate.of(2021, 4, 5))
				.time(LectureTime.builder().id(2).build())
				.audience(Audience.builder().id(2).build())
				.teacher(Teacher.builder().id(2).build())
				.group(new ArrayList<>()).build();
		lectureDao.save(expected);

		assertEquals(1, countRowsInTableWhere(template, TABLE_NAME, 
				"id = 1 "
				+ "AND cathedra_id = 1 "
				+ "AND subject_id = 2 "
				+ "AND date = '2021-04-05' "
				+ "AND lecture_time_id = 2 "
				+ "AND audience_id = 2 "
				+ "AND teacher_id = 2"));

		assertEquals(0, countRowsInTableWhere(template, "lectures_groups", "lecture_id = 1 "));
	}

	@Test
	void whenDeleteExistingLecture_thenAllExistingLecturesFound() {
		int expected = countRowsInTable(template, TABLE_NAME) - 1;
		lectureDao.deleteById(3);

		assertEquals(expected, countRowsInTable(template, TABLE_NAME));
	}
	
	@Test
	void givenAudienceAndDate_whenFindByAudienceAndDate_thenLectureFound() {
		List<Lecture> actual = lectureDao.findByAudienceDateAndTimePeriod(Audience.builder().id(1).build(), 
				LocalDate.of(2021, 4, 4), 
				LectureTime.builder().start(LocalTime.of(8, 0)).end(LocalTime.of(12, 0)).build());
		Lecture lecture1 = Lecture.builder()
				.id(1)
				.group(actual.get(0).getGroups())
				.cathedra(actual.get(0).getCathedra())
				.subject(actual.get(0).getSubject())
				.date(LocalDate.of(2021, 4, 4))
				.time(actual.get(0).getTime())
				.audience(actual.get(0).getAudience())
				.teacher(actual.get(0).getTeacher())
				.build();
		Lecture lecture2 = Lecture.builder()
		.id(2)
		.group(actual.get(1).getGroups())
		.cathedra(actual.get(1).getCathedra())
		.subject(actual.get(1).getSubject())
		.date(LocalDate.of(2021, 4, 4))
		.time(LectureTime.builder().id(2).start(LocalTime.of(9, 40)).end(LocalTime.of(11, 10)).build())
		.audience(actual.get(1).getAudience())
		.teacher(actual.get(1).getTeacher())
		.build();
		List<Lecture> expected = Arrays.asList(lecture1, lecture2);

		assertEquals(expected, actual);
	}
	
	@Test
	void givenAudienceDateAndLectureTime_whenFindByAudienceDateAndLectureTime_thenLectureFound() {
		Lecture actual = lectureDao.findByAudienceDateAndLectureTime(Audience.builder().id(1).build(), LocalDate.of(2021, 4, 4), LectureTime.builder().id(1).build());
		Lecture expected = Lecture.builder()
				.id(1)
				.group(actual.getGroups())
				.cathedra(actual.getCathedra())
				.subject(actual.getSubject())
				.date(LocalDate.of(2021, 4, 4))
				.time(actual.getTime())
				.audience(actual.getAudience())
				.teacher(actual.getTeacher())
				.build();

		assertEquals(expected, actual);
	}
}
