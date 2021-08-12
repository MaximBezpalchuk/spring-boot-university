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

import com.foxminded.university.config.SpringTestConfig;
import com.foxminded.university.dao.jdbc.JdbcAudienceDao;
import com.foxminded.university.model.Audience;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.jdbc.JdbcTestUtils.countRowsInTable;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = SpringTestConfig.class)
public class JdbcAudienceDaoTest {

	private final static String TABLE_NAME = "audiences";
	@Autowired
	private JdbcTemplate template;
	@Autowired
	private JdbcAudienceDao audienceDao;

	@Test
	void whenFindAll_thenAllExistingAudiencesFound() {
		int expected = countRowsInTable(template, TABLE_NAME);
		int actual = audienceDao.findAll().size();

		assertEquals(expected, actual);
	}

	@Test
	void givenExistingAudience_whenFindById_thenAudienceFound() {
		Audience actual = audienceDao.findById(1);
		Audience expected = Audience.build(1, 10, actual.getCathedra()).id(1).build();

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
		Audience actual = audienceDao.findById(1);
		audienceDao.save(Audience.build(100, 100, actual.getCathedra()).build());

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