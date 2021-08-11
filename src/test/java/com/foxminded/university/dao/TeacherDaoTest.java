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

import com.foxminded.university.SpringTestConfig;
import com.foxminded.university.dao.jdbc.JdbcCathedraDao;
import com.foxminded.university.dao.jdbc.JdbcSubjectDao;
import com.foxminded.university.dao.jdbc.JdbcTeacherDao;
import com.foxminded.university.model.Degree;
import com.foxminded.university.model.Gender;
import com.foxminded.university.model.Subject;
import com.foxminded.university.model.Teacher;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = { SpringTestConfig.class })
public class TeacherDaoTest {

	private final static String TABLE_NAME = "teachers";
	@Autowired
	private JdbcTemplate template;
	@Autowired
	private JdbcTeacherDao teacherDao;
	@Autowired
	private JdbcCathedraDao cathedraDao;
	@Autowired
	private JdbcSubjectDao subjectDao;

	@Test
	void whenFindAll_thenAllExistingTeachersFound() {
		int expected = countRowsInTable(template, TABLE_NAME);
		int actual = teacherDao.findAll().size();

		assertEquals(expected, actual);
	}

	@Test
	void givenExistingTeacher_whenFindById_thenTeacherFound() {
		Teacher expected = new Teacher.Builder("Daniel", "Morpheus", "Virtual Reality Capsule no 1", Gender.MALE,
				LocalDate.of(1970, 1, 1), cathedraDao.findById(1), Degree.PROFESSOR).setPhone("1")
						.setEmail("1@bigowl.com").setPostalCode("12345").setEducation("Higher education").setId(1)
						.build();
		List<Subject> subjects = new ArrayList<>();
		Subject subject = new Subject.Builder(cathedraDao.findById(1), "Weapon Tactics",
				"Learning how to use heavy weapon and guerrilla tactics").setId(1).build();
		subjects.add(subject);
		expected.setSubjects(subjects);
		Teacher actual = teacherDao.findById(1);

		assertEquals(expected, actual);
	}

	@Test
	void givenNotExistingTeacher_whenFindOne_thenIncorrestResultSize() {
		Exception exception = assertThrows(EmptyResultDataAccessException.class, () -> {
			teacherDao.findById(100);
		});
		String expectedMessage = "Incorrect result size";
		String actualMessage = exception.getMessage();

		assertTrue(actualMessage.contains(expectedMessage));
	}

	@DirtiesContext(methodMode = MethodMode.AFTER_METHOD)
	@Test
	void givenNewTeacher_whenSaveTeacher_thenAllExistingTeachersFound() {
		int expected = countRowsInTable(template, TABLE_NAME) + 1;
		Teacher teacher = new Teacher.Builder("Test", "Test", "Virtual Reality Capsule no 1", Gender.MALE,
				LocalDate.of(1970, 1, 1), cathedraDao.findById(1), Degree.PROFESSOR).setPhone("1")
						.setEmail("1@bigowl.com").setPostalCode("12345").setEducation("Higher education").build();
		List<Subject> subjects = new ArrayList<>();
		Subject subject = new Subject.Builder(cathedraDao.findById(1), "Weapon Tactics",
				"Learning how to use heavy weapon and guerrilla tactics").setId(1).build();
		subjects.add(subject);
		teacher.setSubjects(subjects);
		teacherDao.save(teacher);

		assertEquals(expected, countRowsInTable(template, TABLE_NAME));
	}

	@DirtiesContext(methodMode = MethodMode.AFTER_METHOD)
	@Test
	void givenExitstingTeacher_whenChange_thenAllExistingTeachersFound() {
		int expected = countRowsInTable(template, TABLE_NAME);
		Teacher teacher = teacherDao.findById(1);
		teacher.setFirstName("Test Name");
		teacherDao.save(teacher);
		int actual = countRowsInTable(template, TABLE_NAME);

		assertEquals(expected, actual);
	}

	@DirtiesContext(methodMode = MethodMode.AFTER_METHOD)
	@Test
	void whenDeleteExistingTeacher_thenAllExistingTeachersFound() {
		int expected = countRowsInTable(template, TABLE_NAME) - 1;
		teacherDao.deleteById(1);

		assertEquals(expected, countRowsInTable(template, TABLE_NAME));
	}

	@DirtiesContext(methodMode = MethodMode.AFTER_METHOD)
	@Test
	void givenExitstingTeacher_whenUpdateSubjects_thenAllExistingTeachersFound() {
		int expected = countRowsInTable(template, "subjects_teachers") + 1;
		Teacher teacher = teacherDao.findById(1);
		teacher.getSubjects().add(subjectDao.findById(2));
		teacherDao.save(teacher);
		int actual = countRowsInTable(template, "subjects_teachers");

		assertEquals(expected, actual);
	}
}
