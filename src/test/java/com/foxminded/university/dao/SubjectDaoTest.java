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
import org.springframework.test.annotation.DirtiesContext.MethodMode;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.foxminded.university.model.Subject;
import com.foxminded.university.model.Cathedra;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = { SpringTestConfig.class })
public class SubjectDaoTest {

	private final static String TABLE_NAME = "subjects";
	@Autowired
	private JdbcTemplate template;
	@Autowired
	private SubjectDao subjectDao;
	@Autowired
	private CathedraDao cathedraDao;

	@Test
	void whenFindAll_thenAllExistingSubjectsFound() {
		int expected = countRowsInTable(template, TABLE_NAME);
		int actual = subjectDao.findAll().size();

		assertEquals(expected, actual);
	}

	@Test
	void givenExistingSubject_whenFindById_thenSubjectFound() {
		Cathedra cathedra = cathedraDao.findById(1);
		Subject expected = new Subject.Builder(cathedra, "Weapon Tactics",
				"Learning how to use heavy weapon and guerrilla tactics").setId(1).build();
		Subject actual = subjectDao.findById(1);

		assertEquals(expected, actual);
	}

	@Test
	void givenNotExistingSubject_whenFindOne_thenIncorrestResultSize() {
		Exception exception = assertThrows(EmptyResultDataAccessException.class, () -> {
			subjectDao.findById(100);
		});
		String expectedMessage = "Incorrect result size";
		String actualMessage = exception.getMessage();

		assertTrue(actualMessage.contains(expectedMessage));
	}

	@DirtiesContext(methodMode = MethodMode.AFTER_METHOD)
	@Test
	void givenNewSubject_whenSaveSubject_thenAllExistingSubjectsFound() {
		int expected = countRowsInTable(template, TABLE_NAME) + 1;
		Cathedra cathedra = cathedraDao.findById(1);
		subjectDao.save(new Subject.Builder(cathedra, "Weapon Tactics123",
				"Learning how to use heavy weapon and guerrilla tactics123").build());

		assertEquals(expected, countRowsInTable(template, TABLE_NAME));
	}

	@DirtiesContext(methodMode = MethodMode.AFTER_METHOD)
	@Test
	void givenExitstingSubject_whenChange_thenAllExistingSubjectsFound() {
		int expected = countRowsInTable(template, TABLE_NAME);
		Subject subject = subjectDao.findById(1);
		subject.setName("Test Name");
		subjectDao.save(subject);
		int actual = countRowsInTable(template, TABLE_NAME);

		assertEquals(expected, actual);
	}

	@DirtiesContext(methodMode = MethodMode.AFTER_METHOD)
	@Test
	void whenDeleteExistingSubject_thenAllExistingSubjectsFound() {
		int expected = countRowsInTable(template, TABLE_NAME) - 1;
		subjectDao.deleteById(1);

		assertEquals(expected, countRowsInTable(template, TABLE_NAME));
	}

	@Test
	void givenExistingSubject_whenFindByTeacherId_thenSubjectFound() {
		List<Subject> expected = new ArrayList<>();
		Cathedra cathedra = cathedraDao.findById(1);
		Subject subject1 = new Subject.Builder(cathedra, "Weapon Tactics",
				"Learning how to use heavy weapon and guerrilla tactics").setId(1).build();
		expected.add(subject1);
		List<Subject> actual = subjectDao.findByTeacherId(1);
		assertEquals(expected, actual);
	}
}
