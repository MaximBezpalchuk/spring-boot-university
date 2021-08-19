package com.foxminded.university.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.jdbc.JdbcTestUtils.countRowsInTable;

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

import com.foxminded.university.model.Subject;
import com.foxminded.university.config.SpringTestConfig;
import com.foxminded.university.dao.jdbc.JdbcSubjectDao;
import com.foxminded.university.model.Cathedra;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = SpringTestConfig.class)
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
public class JdbcSubjectDaoTest {

	private final static String TABLE_NAME = "subjects";
	@Autowired
	private JdbcTemplate template;
	@Autowired
	private JdbcSubjectDao subjectDao;

	@Test
	void whenFindAll_thenAllExistingSubjectsFound() {
		int expected = countRowsInTable(template, TABLE_NAME);
		int actual = subjectDao.findAll().size();

		assertEquals(expected, actual);
	}

	@Test
	void givenExistingSubject_whenFindById_thenSubjectFound() {
		Subject actual = subjectDao.findById(1);
		Subject expected = Subject.builder()
				.cathedra(actual.getCathedra())
				.name("Weapon Tactics")
				.description("Learning how to use heavy weapon and guerrilla tactics")
				.id(1)
				.build();

		assertEquals(expected, actual);
	}

	@Test
	void givenNotExistingSubject_whenFindById_thenIncorrestResultSize() {
		Exception exception = assertThrows(EmptyResultDataAccessException.class, () -> {
			subjectDao.findById(100);
		});
		String expectedMessage = "Incorrect result size";
		String actualMessage = exception.getMessage();

		assertTrue(actualMessage.contains(expectedMessage));
	}

	@Test
	void givenNewSubject_whenSaveSubject_thenAllExistingSubjectsFound() {
		int expected = countRowsInTable(template, TABLE_NAME) + 1;
		Subject actual = subjectDao.findById(1);
		subjectDao.save(Subject.builder()
				.cathedra(actual.getCathedra())
				.name("Weapon Tactics123")
				.description("Learning how to use heavy weapon and guerrilla tactics123")
				.build());

		assertEquals(expected, countRowsInTable(template, TABLE_NAME));
	}

	@Test
	void givenExitstingSubject_whenSaveWithChanges_thenChangesApplied() {
		Subject expected = Subject.builder()
				.id(1)
				.name("Test Name")
				.description("Test Description")
				.cathedra(Cathedra.builder().id(1).build())
				.build();
		subjectDao.save(expected);
		
		String name = template.queryForObject("SELECT name FROM subjects WHERE id = 1", String.class);
		String description = template.queryForObject("SELECT description FROM subjects WHERE id = 1", String.class);
		
		assertEquals(expected.getName(), name);
		assertEquals(expected.getDescription(), description);
	}

	@Test
	void whenDeleteExistingSubject_thenAllExistingSubjectsFound() {
		int expected = countRowsInTable(template, TABLE_NAME) - 1;
		subjectDao.deleteById(1);

		assertEquals(expected, countRowsInTable(template, TABLE_NAME));
	}

	@Test
	void givenExistingSubject_whenFindByTeacherId_thenSubjectFound() {
		List<Subject> expected = new ArrayList<>();
		List<Subject> actual = subjectDao.findByTeacherId(1);
		Cathedra cathedra = actual.get(0).getCathedra();
		Subject subject1 = Subject.builder()
				.id(1).cathedra(cathedra)
				.name("Weapon Tactics")
				.description("Learning how to use heavy weapon and guerrilla tactics")
				.build();
		expected.add(subject1);

		assertEquals(expected, actual);
	}
}
