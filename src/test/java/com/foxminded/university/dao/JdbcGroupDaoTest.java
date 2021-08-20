package com.foxminded.university.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.jdbc.JdbcTestUtils.countRowsInTable;
import static org.springframework.test.jdbc.JdbcTestUtils.countRowsInTableWhere;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.foxminded.university.model.Cathedra;
import com.foxminded.university.model.Group;
import com.foxminded.university.config.SpringTestConfig;
import com.foxminded.university.dao.jdbc.JdbcGroupDao;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = SpringTestConfig.class)
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
public class JdbcGroupDaoTest {

	private final static String TABLE_NAME = "groups";
	@Autowired
	private JdbcTemplate template;
	@Autowired
	private JdbcGroupDao groupDao;

	@Test
	void whenFindAll_thenAllExistingGroupsFound() {
		int expected = countRowsInTable(template, TABLE_NAME);
		int actual = groupDao.findAll().size();

		assertEquals(expected, actual);
	}

	@Test
	void givenExistingGroup_whenFindById_thenGroupFound() {
		Group actual = groupDao.findById(1);
		Group expected = Group.builder()
				.id(1)
				.name("Killers")
				.cathedra(actual.getCathedra())
				.build();

		assertEquals(expected, actual);
	}

	@Test
	void givenNotExistingGroup_whenFindById_thenIncorrestResultSize() {
		Exception exception = assertThrows(EmptyResultDataAccessException.class, () -> {
			groupDao.findById(100);
		});
		String expectedMessage = "Incorrect result size";
		String actualMessage = exception.getMessage();

		assertTrue(actualMessage.contains(expectedMessage));
	}

	@Test
	void givenNewGroup_whenSaveGroup_thenAllExistingGroupsFound() {
		int expected = countRowsInTable(template, TABLE_NAME) + 1;
		groupDao.save(Group.builder()
				.name("Test Name")
				.cathedra(Cathedra.builder()
						.id(1)
						.build())
				.build());

		assertEquals(expected, countRowsInTable(template, TABLE_NAME));
	}
	
	@Test
	void givenExitstingGroup_whenSaveWithChanges_thenChangesApplied() {
		Group expected = groupDao.findById(1);
		expected.setName("Killers 2");
		groupDao.save(expected);
		
		assertEquals(1, countRowsInTableWhere(template, TABLE_NAME, "id = 1 AND name = 'Killers 2'"));
	}
	
	@Test
	void whenDeleteExistingGroup_thenAllExistingGroupsFound() {
		int expected = countRowsInTable(template, TABLE_NAME) - 1;
		groupDao.deleteById(1);

		assertEquals(expected, countRowsInTable(template, TABLE_NAME));
	}
}
