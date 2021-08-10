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

import com.foxminded.university.model.Lecture;
import com.foxminded.university.model.LectureTime;
import com.foxminded.university.model.Subject;
import com.foxminded.university.model.Teacher;
import com.foxminded.university.model.Audience;
import com.foxminded.university.model.Cathedra;
import com.foxminded.university.model.Group;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = { SpringTestConfig.class })
public class LectureDaoTest {

	private final static String TABLE_NAME = "lectures";
	@Autowired
	private JdbcTemplate template;
	@Autowired
	private LectureDao lectureDao;
	@Autowired
	private CathedraDao cathedraDao;
	@Autowired
	private SubjectDao subjectDao;
	@Autowired
	private LectureTimeDao lectureTimeDao;
	@Autowired
	private AudienceDao audienceDao;
	@Autowired
	private TeacherDao teacherDao;
	@Autowired
	private GroupDao groupDao;

	@Test
	void whenFindAll_thenAllExistingLecturesFound() {
		int expected = countRowsInTable(template, TABLE_NAME);
		int actual = lectureDao.findAll().size();

		assertEquals(expected, actual);
	}

	@Test
	void givenExistingLecture_whenFindById_thenLectureFound() {
		Cathedra cathedra = cathedraDao.findById(1);
		Subject subject = subjectDao.findById(1);
		LectureTime lectureTime = lectureTimeDao.findById(1);
		Audience audience = audienceDao.findById(1);
		Teacher teacher = teacherDao.findById(1);
		List<Group> groups = new ArrayList<>();
		groups.add(groupDao.findById(1));
		Lecture expected = new Lecture.Builder(cathedra, subject, LocalDate.of(2021, 4, 4), lectureTime, audience,
				teacher).setId(1).setGroup(groups).build();
		Lecture actual = lectureDao.findById(1);

		assertEquals(expected, actual);
	}

	@Test
	void givenNotExistingLecture_whenFindOne_thenIncorrestResultSize() {
		Exception exception = assertThrows(EmptyResultDataAccessException.class, () -> {
			lectureDao.findById(100);
		});
		String expectedMessage = "Incorrect result size";
		String actualMessage = exception.getMessage();

		assertTrue(actualMessage.contains(expectedMessage));
	}

	@DirtiesContext(methodMode = MethodMode.AFTER_METHOD)
	@Test
	void givenNewLecture_whenSaveLecture_thenAllExistingLecturesFound() {
		int expected = countRowsInTable(template, TABLE_NAME) + 1;
		Cathedra cathedra = cathedraDao.findById(1);
		Subject subject = subjectDao.findById(1);
		LectureTime lectureTime = lectureTimeDao.findById(1);
		Audience audience = audienceDao.findById(2);
		Teacher teacher = teacherDao.findById(2);
		lectureDao.save(new Lecture.Builder(cathedra, subject, LocalDate.of(2021, 4, 4), lectureTime, audience, teacher)
				.build());

		assertEquals(expected, countRowsInTable(template, TABLE_NAME));
	}

	@DirtiesContext(methodMode = MethodMode.AFTER_METHOD)
	@Test
	void givenExitstingLecture_whenChange_thenAllExistingLecturesFound() {
		int expected = countRowsInTable(template, TABLE_NAME);
		Lecture lecture = lectureDao.findById(1);
		LectureTime lectureTime = lectureTimeDao.findById(6);
		lecture.setTime(lectureTime);
		lectureDao.save(lecture);
		int actual = countRowsInTable(template, TABLE_NAME);

		assertEquals(expected, actual);
	}

	@DirtiesContext(methodMode = MethodMode.AFTER_METHOD)
	@Test
	void whenDeleteExistingLecture_thenAllExistingLecturesFound() {
		int expected = countRowsInTable(template, TABLE_NAME) - 1;
		lectureDao.deleteById(3);

		assertEquals(expected, countRowsInTable(template, TABLE_NAME));
	}

	@DirtiesContext(methodMode = MethodMode.AFTER_METHOD)
	@Test
	void givenExitstingLecture_whenUpdateGroups_thenAllExistingLecturesFound() {
		int expected = countRowsInTable(template, "lectures_groups") + 1;
		lectureDao.updateGroups(lectureDao.findById(1), groupDao.findById(2));
		int actual = countRowsInTable(template, "lectures_groups");

		assertEquals(expected, actual);
	}

}
