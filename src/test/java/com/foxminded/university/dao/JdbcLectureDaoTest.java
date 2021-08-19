package com.foxminded.university.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.jdbc.JdbcTestUtils.countRowsInTable;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.foxminded.university.model.Audience;
import com.foxminded.university.model.Cathedra;
import com.foxminded.university.model.Group;
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

		Lecture actual = template.query("SELECT * FROM lectures WHERE id = 1", new ResultSetExtractor<Lecture>() {
			@Override
			public Lecture extractData(ResultSet rs) throws SQLException, DataAccessException {
				Lecture lecture = null;
				while (rs.next()) {
					lecture = Lecture.builder().id(rs.getInt("id"))
							.cathedra(Cathedra.builder().id(rs.getInt("cathedra_id")).build())
							.subject(Subject.builder().id(rs.getInt("subject_id")).build())
							.date(rs.getObject("date", LocalDate.class))
							.time(LectureTime.builder().id(rs.getInt("lecture_time_id")).build())
							.audience(Audience.builder().id(rs.getInt("audience_id")).build())
							.teacher(Teacher.builder().id(rs.getInt("teacher_id")).build())
							.build();
				}

				List<Group> groups = template.query(
						"SELECT * FROM groups WHERE id IN (SELECT group_id FROM lectures_groups WHERE lecture_id =1)",
						new ResultSetExtractor<List<Group>>() {
							@Override
							public List<Group> extractData(ResultSet rs) throws SQLException, DataAccessException {
								List<Group> groups = new ArrayList<>();
								while (rs.next()) {
									groups.add(Group.builder()
											.id(rs.getInt("id"))
											.name(rs.getString("name"))
											.cathedra(Cathedra.builder().id(rs.getInt("cathedra_id")).build())
											.build());
								}
								return groups;
							}
						});
				lecture.setGroups(groups);
				return lecture;
			}
		});

		assertEquals(expected, actual);
	}

	@Test
	void whenDeleteExistingLecture_thenAllExistingLecturesFound() {
		int expected = countRowsInTable(template, TABLE_NAME) - 1;
		lectureDao.deleteById(3);

		assertEquals(expected, countRowsInTable(template, TABLE_NAME));
	}
}
