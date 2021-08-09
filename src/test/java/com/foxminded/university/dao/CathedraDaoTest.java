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
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.foxminded.university.model.Cathedra;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = { SpringTestConfig.class })
public class CathedraDaoTest {

	private final static String TABLE_NAME = "cathedras";
	@Autowired
	private JdbcTemplate template;
	@Autowired
	private CathedraDao cathedraDao;

	@Test
	void whenFindAll_thenAllExistingCathedrasFound() {
		int expected = countRowsInTable(template, TABLE_NAME);
		int actual = cathedraDao.findAll().size();

		assertEquals(expected, actual);
	}

	@Test
	void givenExistingAudience_whenFindById_thenAudinceFound() {
		Cathedra expected = new Cathedra("Fantastic Cathedra");
		expected.setId(1);
		Cathedra actual = cathedraDao.findById(1);

		assertEquals(expected, actual);
	}

	@Test
	void givenNotExistingAudience_whenFindOne_thenIncorrestResultSize() {
		Exception exception = assertThrows(EmptyResultDataAccessException.class, () -> {
			cathedraDao.findById(100);
		});
		String expectedMessage = "Incorrect result size";
		String actualMessage = exception.getMessage();

		assertTrue(actualMessage.contains(expectedMessage));
	}

	@Test
	void givenNewAudience_whenSaveAudience_thenAllExistingAudiencesFound() {
		int expected = countRowsInTable(template, TABLE_NAME) + 1;
		cathedraDao.save(new Cathedra("Fantastic Cathedra 2"));

		assertEquals(expected, countRowsInTable(template, TABLE_NAME));
	}

	@Test
	void givenExitstingAudience_whenChange_thenAllExistingAudiencesFound() {
		int expected = countRowsInTable(template, TABLE_NAME);
		Cathedra cathedra = cathedraDao.findById(1);
		cathedra.setName("TestName");
		cathedra.setId(1);
		cathedraDao.save(cathedra);
		int actual = countRowsInTable(template, TABLE_NAME);

		assertEquals(expected, actual);
	}

	@Test
	void whenDeleteExistingAudience_thenAllExistingAudiencesFound() {
		int expected = countRowsInTable(template, TABLE_NAME) - 1;
		cathedraDao.deleteById(1);

		assertEquals(expected, countRowsInTable(template, TABLE_NAME));
	}

}
