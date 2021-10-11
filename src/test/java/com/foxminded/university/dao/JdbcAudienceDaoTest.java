package com.foxminded.university.dao;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.annotation.DirtiesContext.ClassMode;

import com.foxminded.university.config.SpringTestConfig;
import com.foxminded.university.dao.jdbc.JdbcAudienceDao;
import com.foxminded.university.model.Audience;
import com.foxminded.university.model.Cathedra;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.jdbc.JdbcTestUtils.countRowsInTable;
import static org.springframework.test.jdbc.JdbcTestUtils.countRowsInTableWhere;

import java.util.Optional;

@ExtendWith(SpringExtension.class)
@WebAppConfiguration
@ContextConfiguration(classes = SpringTestConfig.class)
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
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
		Optional<Audience> actual = audienceDao.findById(1);
		Optional<Audience> expected = Optional.of(Audience.builder()
				.id(1)
				.room(1)
				.capacity(10)
				.cathedra(Cathedra.builder().id(1).name("Fantastic Cathedra").build())
				.build());

		assertEquals(expected, actual);
	}

	@Test
	void givenNotExistingAudience_whenFindById_thenReturnEmptyOptional() {
		assertEquals(audienceDao.findById(100), Optional.empty());
	}

	@Test
	void givenNewAudience_whenSaveAudience_thenAllExistingAudiencesFound() {
		int expected = countRowsInTable(template, TABLE_NAME) + 1;
		audienceDao.save(Audience.builder()
				.room(100)
				.capacity(100)
				.cathedra(Cathedra.builder()
						.id(1)
						.build())
				.build());

		assertEquals(expected, countRowsInTable(template, TABLE_NAME));
	}

	@Test
	void givenExitstingAudience_whenSaveWithChanges_thenChangesApplied() {
		Audience expected = Audience.builder().id(1).room(1).capacity(10)
				.cathedra(Cathedra.builder()
						.id(1)
						.name("Fantastic Cathedra")
						.build())
				.build();
		expected.setCapacity(100);
		audienceDao.save(expected);
		assertEquals(1, countRowsInTableWhere(template, TABLE_NAME, "id = 1 AND capacity = 100"));
	}

	@Test
	void whenDeleteExistingAudience_thenAllExistingAudiencesFound() {
		int expected = countRowsInTable(template, TABLE_NAME) - 1;
		audienceDao.deleteById(3);

		assertEquals(expected, countRowsInTable(template, TABLE_NAME));
	}
	
	@Test
	void givenRoom_whenFindByRoom_thenAudienceFound() {
		Optional<Audience> actual = audienceDao.findByRoom(1);
		Optional<Audience> expected = Optional.of(Audience.builder()
				.id(1)
				.room(1)
				.capacity(10)
				.cathedra(Cathedra.builder().id(1).name("Fantastic Cathedra").build())
				.build());

		assertEquals(expected, actual);
	}

}
