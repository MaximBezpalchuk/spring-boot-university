package com.foxminded.university.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.jdbc.JdbcTestUtils.countRowsInTable;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.MethodMode;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.foxminded.university.model.Group;
import com.foxminded.university.config.SpringTestConfig;
import com.foxminded.university.dao.jdbc.JdbcGroupDao;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = SpringTestConfig.class)
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
	void givenNotExistingGroup_whenFindOne_thenIncorrestResultSize() {
		Exception exception = assertThrows(EmptyResultDataAccessException.class, () -> {
			groupDao.findById(100);
		});
		String expectedMessage = "Incorrect result size";
		String actualMessage = exception.getMessage();

		assertTrue(actualMessage.contains(expectedMessage));
	}

	@DirtiesContext(methodMode = MethodMode.AFTER_METHOD)
	@Test
	void givenNewGroup_whenSaveGroup_thenAllExistingGroupsFound() {
		int expected = countRowsInTable(template, TABLE_NAME) + 1;
		Group actual = groupDao.findById(1);
		groupDao.save(Group.builder().name("Test Name").cathedra(actual.getCathedra()).build());

		assertEquals(expected, countRowsInTable(template, TABLE_NAME));
	}

	@DirtiesContext(methodMode = MethodMode.AFTER_METHOD)
	@Test
	void givenExitstingGroup_whenChange_thenChangesApplied() {
		Group expected = groupDao.findById(1);
		expected.setName("Killers 2");
		groupDao.save(expected);
		Group actual = groupDao.findById(1);

		assertEquals(expected, actual);
	}

	@DirtiesContext(methodMode = MethodMode.AFTER_METHOD)
	@Test
	void whenDeleteExistingGroup_thenAllExistingGroupsFound() {
		int expected = countRowsInTable(template, TABLE_NAME) - 1;
		groupDao.deleteById(1);

		assertEquals(expected, countRowsInTable(template, TABLE_NAME));
	}
}
