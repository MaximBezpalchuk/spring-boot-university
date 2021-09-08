package com.foxminded.university.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.foxminded.university.dao.jdbc.JdbcAudienceDao;
import com.foxminded.university.model.Audience;

@ExtendWith(MockitoExtension.class)
public class AudienceServiceTest {

	@Mock
	private JdbcAudienceDao audienceDao;
	@InjectMocks
	private AudienceService audienceService;

	@Test
	void givenListOfAudiences_whenFindAll_thenAllExistingAudiencesFound() {
		Audience audience1 = Audience.builder().id(1).build();
		Audience audience2 = Audience.builder().id(2).build();
		List<Audience> expected = Arrays.asList(audience1, audience2);
		when(audienceDao.findAll()).thenReturn(expected);
		List<Audience> actual = audienceService.findAll();

		assertEquals(expected, actual);
	}

	@Test
	void givenExistingAudience_whenFindById_thenAudienceFound() {
		Audience expected = Audience.builder().id(1).build();
		when(audienceDao.findById(1)).thenReturn(expected);
		Audience actual = audienceService.findById(1);

		assertEquals(expected, actual);
	}

	@Test
	void givenNewAudience_whenSave_thenSaved() {
		Audience audience = Audience.builder().build();
		String output = audienceService.save(audience);
		
		assertEquals("Audience added!", output);
	}
	
	@Test
	void givenExistingAudience_whenSave_thenSaved() {
		Audience audience = Audience.builder().id(1).build();
		when(audienceDao.findByRoom(audience.getRoom())).thenReturn(audience);
		String output = audienceService.save(audience);
		
		assertEquals("Audience updated!", output);
	}

	@Test
	void givenExistingAudienceId_whenDelete_thenDeleted() {
		audienceService.deleteById(3);

		verify(audienceDao).deleteById(3);
	}

}
