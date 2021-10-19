package com.foxminded.university.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.jdbc.JdbcTestUtils.countRowsInTable;
import static org.springframework.test.jdbc.JdbcTestUtils.countRowsInTableWhere;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.foxminded.university.config.TestConfig;
import com.foxminded.university.dao.jdbc.JdbcCathedraDao;
import com.foxminded.university.model.Cathedra;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = TestConfig.class)
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
public class JdbcCathedraDaoTest {

	private final static String TABLE_NAME = "cathedras";
	@Autowired
	private JdbcTemplate template;
	@Autowired
	private JdbcCathedraDao cathedraDao;

	@Test
	void whenFindAll_thenAllExistingCathedrasFound() {
		int expected = countRowsInTable(template, TABLE_NAME);
		int actual = cathedraDao.findAll().size();

		assertEquals(expected, actual);
	}

	@Test
	void givenExistingCathedra_whenFindById_thenCathedraFound() {
		Optional<Cathedra> expected = Optional.of(Cathedra.builder()
				.id(1)
				.name("Fantastic Cathedra")
				.build());
		Optional<Cathedra> actual = cathedraDao.findById(1);

		assertEquals(expected, actual);
	}

	@Test
	void givenNotExistingCathedra_whenFindById_thenReturnEmptyOptional() {
		assertEquals(cathedraDao.findById(100), Optional.empty());
	}

	@Test
	void givenNewCathedra_whenSaveCathedra_thenAllExistingCathedrasFound() {
		int expected = countRowsInTable(template, TABLE_NAME) + 1;
		cathedraDao.save(Cathedra.builder()
				.name("Fantastic Cathedra 2")
				.build());

		assertEquals(expected, countRowsInTable(template, TABLE_NAME));
	}

	@Test
	void givenExitstingCathedra_whenSaveWithChanges_thenChangesApplied() {
		Cathedra expected = Cathedra.builder()
				.id(1)
				.name("TestName")
				.build();
		cathedraDao.save(expected);
		
		assertEquals(1, countRowsInTableWhere(template, TABLE_NAME, "id = 1 AND name = 'TestName'"));
	}

	@Test
	void whenDeleteExistingCathedra_thenAllExistingCathedrasFound() {
		int expected = countRowsInTable(template, TABLE_NAME) - 1;
		cathedraDao.deleteById(1);

		assertEquals(expected, countRowsInTable(template, TABLE_NAME));
	}
	
	@Test
	void givenCathedraName_whenFindByName_thenCathedraFound() {
		Optional<Cathedra> expected = Optional.of(Cathedra.builder()
				.id(1)
				.name("Fantastic Cathedra")
				.build());
		Optional<Cathedra> actual = cathedraDao.findByName("Fantastic Cathedra");

		assertEquals(expected, actual);
	}
}
