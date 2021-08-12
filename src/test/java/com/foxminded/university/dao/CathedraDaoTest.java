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

import com.foxminded.university.config.SpringTestConfig;
import com.foxminded.university.dao.jdbc.JdbcCathedraDao;
import com.foxminded.university.model.Cathedra;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = { SpringTestConfig.class })
public class CathedraDaoTest {

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
		Cathedra expected = Cathedra.build("Fantastic Cathedra").id(1).build();
		Cathedra actual = cathedraDao.findById(1);

		assertEquals(expected, actual);
	}

	@Test
	void givenNotExistingCathedra_whenFindOne_thenIncorrestResultSize() {
		Exception exception = assertThrows(EmptyResultDataAccessException.class, () -> {
			cathedraDao.findById(100);
		});
		String expectedMessage = "Incorrect result size";
		String actualMessage = exception.getMessage();

		assertTrue(actualMessage.contains(expectedMessage));
	}

	@DirtiesContext(methodMode = MethodMode.AFTER_METHOD)
	@Test
	void givenNewCathedra_whenSaveCathedra_thenAllExistingCathedrasFound() {
		int expected = countRowsInTable(template, TABLE_NAME) + 1;
		cathedraDao.save(Cathedra.build("Fantastic Cathedra 2").build());

		assertEquals(expected, countRowsInTable(template, TABLE_NAME));
	}

	@DirtiesContext(methodMode = MethodMode.AFTER_METHOD)
	@Test
	void givenExitstingCathedra_whenChange_thenAllExistingCathedrasFound() {
		int expected = countRowsInTable(template, TABLE_NAME);
		Cathedra cathedra = cathedraDao.findById(1);
		cathedra.setName("TestName");
		cathedra.setId(1);
		cathedraDao.save(cathedra);
		int actual = countRowsInTable(template, TABLE_NAME);

		assertEquals(expected, actual);
	}

	@DirtiesContext(methodMode = MethodMode.AFTER_METHOD)
	@Test
	void whenDeleteExistingCathedra_thenAllExistingCathedrasFound() {
		int expected = countRowsInTable(template, TABLE_NAME) - 1;
		cathedraDao.deleteById(1);

		assertEquals(expected, countRowsInTable(template, TABLE_NAME));
	}

}
