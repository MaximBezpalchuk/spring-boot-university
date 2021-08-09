package com.foxminded.university.dao;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.jdbc.JdbcTestUtils.countRowsInTable;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = { SpringTestConfig.class })
public class AudienceDaoTest {

	private final static String TABLE_NAME = "audiences";
	@Autowired
	private JdbcTemplate template;
	@Autowired
	private AudienceDao audienceDao;

	@Test
	void whenFindAll_thenAllExistingAudiencesFound() {
		int expected = countRowsInTable(template, TABLE_NAME);
		int actual = audienceDao.findAll().size();

		assertEquals(expected, actual);
	}

}
