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
public class GroupDaoTest {

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
		Group expected = Group.build("Killers", actual.getCathedra()).id(1).build();

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
		groupDao.save(Group.build("Test Name", actual.getCathedra()).build());

		assertEquals(expected, countRowsInTable(template, TABLE_NAME));
	}

	@DirtiesContext(methodMode = MethodMode.AFTER_METHOD)
	@Test
	void givenExitstingGroup_whenChange_thenAllExistingGroupsFound() {
		int expected = countRowsInTable(template, TABLE_NAME);
		Group group = groupDao.findById(1);
		group.setName("Killers 2");
		groupDao.save(group);
		int actual = countRowsInTable(template, TABLE_NAME);

		template.update(
				"DROP TABLE cathedras, audiences, groups, lectures, students, subjects, teachers, vacations, holidays, lecture_times, subjects_teachers, lectures_groups");
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
