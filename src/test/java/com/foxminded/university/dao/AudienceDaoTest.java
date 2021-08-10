package com.foxminded.university.dao;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.MethodMode;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.foxminded.university.model.Audience;
import com.foxminded.university.model.Cathedra;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.jdbc.JdbcTestUtils.countRowsInTable;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = { SpringTestConfig.class })
public class AudienceDaoTest {

	private final static String TABLE_NAME = "audiences";
	@Autowired
	private JdbcTemplate template;
	@Autowired
	private AudienceDao audienceDao;
	@Autowired
	private CathedraDao cathedraDao;

	@Test
	void whenFindAll_thenAllExistingAudiencesFound() {
		int expected = countRowsInTable(template, TABLE_NAME);
		int actual = audienceDao.findAll().size();

		assertEquals(expected, actual);
	}

	@Test
	void givenExistingAudience_whenFindById_thenAudienceFound() {
		Cathedra cathedra = cathedraDao.findById(1);
		Audience expected = new Audience.Builder(1, 10, cathedra).setId(1).build();
		Audience actual = audienceDao.findById(1);

		assertEquals(expected, actual);
	}

	@Test
	void givenNotExistingAudience_whenFindOne_thenIncorrestResultSize() {
		Exception exception = assertThrows(EmptyResultDataAccessException.class, () -> {
			audienceDao.findById(100);
		});
		String expectedMessage = "Incorrect result size";
		String actualMessage = exception.getMessage();

		assertTrue(actualMessage.contains(expectedMessage));
	}

	@DirtiesContext(methodMode = MethodMode.AFTER_METHOD)
	@Test
	void givenNewAudience_whenSaveAudience_thenAllExistingAudiencesFound() {
		int expected = countRowsInTable(template, TABLE_NAME) + 1;
		Cathedra cathedra = cathedraDao.findById(1);
		audienceDao.save(new Audience.Builder(100, 100, cathedra).build());

		assertEquals(expected, countRowsInTable(template, TABLE_NAME));
	}

	@DirtiesContext(methodMode = MethodMode.AFTER_METHOD)
	@Test
	void givenExitstingAudience_whenChange_thenAllExistingAudiencesFound() {
		int expected = countRowsInTable(template, TABLE_NAME);
		Audience audience = audienceDao.findById(1);
		audience.setCapacity(100);
		audienceDao.save(audience);
		int actual = countRowsInTable(template, TABLE_NAME);

		assertEquals(expected, actual);
	}

	@DirtiesContext(methodMode = MethodMode.AFTER_METHOD)
	@Test
	void whenDeleteExistingAudience_thenAllExistingAudiencesFound() {
		int expected = countRowsInTable(template, TABLE_NAME) - 1;
		audienceDao.deleteById(3);

		assertEquals(expected, countRowsInTable(template, TABLE_NAME));
	}

}
