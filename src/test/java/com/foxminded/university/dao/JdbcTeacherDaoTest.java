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
import com.foxminded.university.dao.jdbc.JdbcTeacherDao;
import com.foxminded.university.model.Degree;
import com.foxminded.university.model.Gender;
import com.foxminded.university.model.Subject;
import com.foxminded.university.model.Teacher;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = SpringTestConfig.class)
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
public class JdbcTeacherDaoTest {

	private final static String TABLE_NAME = "teachers";
	@Autowired
	private JdbcTemplate template;
	@Autowired
	private JdbcTeacherDao teacherDao;

	@Test
	void whenFindAll_thenAllExistingTeachersFound() {
		int expected = countRowsInTable(template, TABLE_NAME);
		int actual = teacherDao.findAll().size();

		assertEquals(expected, actual);
	}

	@Test
	void givenExistingTeacher_whenFindById_thenTeacherFound() {
		Teacher actual = teacherDao.findById(1);
		Teacher expected = Teacher.builder()
				.firstName("Daniel")
				.lastName("Morpheus")
				.address("Virtual Reality Capsule no 1")
				.gender(Gender.MALE)
				.birthDate(LocalDate.of(1970, 1, 1))
				.cathedra(actual.getCathedra())
				.degree(Degree.PROFESSOR)
				.phone("1")
				.email("1@bigowl.com")
				.postalCode("12345")
				.education("Higher education")
				.id(1)
				.build();
		List<Subject> subjects = new ArrayList<>();
		Subject subject = Subject.builder().cathedra(actual.getCathedra()).name("Weapon Tactics")
				.description("Learning how to use heavy weapon and guerrilla tactics").id(1).build();
		subjects.add(subject);
		expected.setSubjects(subjects);

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

	@Test
	void givenNewTeacher_whenSaveTeacher_thenAllExistingTeachersFound() {
		int expected = countRowsInTable(template, TABLE_NAME) + 1;
		Teacher actual = teacherDao.findById(1);
		Teacher teacher = Teacher.builder()
				.firstName("Test")
				.lastName("Test")
				.address("Virtual Reality Capsule no 1")
				.gender(Gender.MALE)
				.birthDate(LocalDate.of(1970, 1, 1))
				.cathedra(actual.getCathedra())
				.degree(Degree.PROFESSOR)
				.phone("1")
				.email("1@bigowl.com")
				.postalCode("12345")
				.education("Higher education")
				.build();
		List<Subject> subjects = new ArrayList<>();
		Subject subject = Subject.builder().cathedra(actual.getCathedra()).name("Weapon Tactics")
				.description("Learning how to use heavy weapon and guerrilla tactics").id(1).build();
		subjects.add(subject);
		teacher.setSubjects(subjects);
		teacherDao.save(teacher);

		assertEquals(expected, countRowsInTable(template, TABLE_NAME));
	}

	@Test
	void givenExitstingTeacher_whenChange_thenChangesApplied() {
		Teacher expected = teacherDao.findById(1);
		expected.setFirstName("Test Name");
		teacherDao.save(expected);
		Teacher actual = teacherDao.findById(1);

		assertEquals(expected, actual);
	}

	@Test
	void whenDeleteExistingTeacher_thenAllExistingTeachersFound() {
		int expected = countRowsInTable(template, TABLE_NAME) - 1;
		teacherDao.deleteById(1);

		assertEquals(expected, countRowsInTable(template, TABLE_NAME));
	}

	@Test
	void givenExitstingTeacher_whenUpdateSubjects_thenAllExistingTeachersFound() {
		int expected = countRowsInTable(template, "subjects_teachers") + 1;
		Teacher teacher = teacherDao.findById(1);
		teacher.getSubjects().add(Subject.builder()
				.cathedra(teacher.getCathedra())
				.name("Wandless Magic")
				.description("Learning how to use spells without magic wand")
				.id(2)
				.build());
		teacherDao.save(teacher);
		int actual = countRowsInTable(template, "subjects_teachers");

		assertEquals(expected, actual);
	}
}
