package com.foxminded.university.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import com.foxminded.university.dao.AudienceDao;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.foxminded.university.exception.EntityNotFoundException;
import com.foxminded.university.exception.EntityNotUniqueException;
import com.foxminded.university.model.Audience;

@ExtendWith(MockitoExtension.class)
public class AudienceServiceTest {

	@Mock
	private AudienceDao audienceDao;
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
		Optional<Audience> expected = Optional.of(Audience.builder().id(1).build());
		when(audienceDao.findById(1)).thenReturn(expected);
		Audience actual = audienceService.findById(1);

		assertEquals(expected.get(), actual);
	}

	@Test
	void givenExistingAudience_whenFindById_thenEntityNotFoundException() {
		when(audienceDao.findById(10)).thenReturn(Optional.empty());
		Exception exception = assertThrows(EntityNotFoundException.class, () -> {
			audienceService.findById(10);
		});

		assertEquals("Can`t find any audience with id: 10", exception.getMessage());
	}

	@Test
	void givenNewAudience_whenSave_thenSaved() {
		Audience audience = Audience.builder().build();
		audienceService.save(audience);

		verify(audienceDao).save(audience);
	}

	@Test
	void givenExistingAudience_whenSave_thenSaved() {
		Audience audience = Audience.builder().id(1).room(123).build();
		when(audienceDao.findByRoom(audience.getRoom())).thenReturn(Optional.of(audience));
		audienceService.save(audience);

		verify(audienceDao).save(audience);
	}

	@Test
	void givenExistingAudienceId_whenDelete_thenDeleted() {
		audienceService.deleteById(3);

		verify(audienceDao).deleteById(3);
	}

	@Test
	void givenNotUniqueAudience_whenSave_thenEntityNotUniqueException() {
		Audience audience1 = Audience.builder()
				.id(1)
				.room(10)
				.build();
		Audience audience2 = Audience.builder()
				.id(5)
				.room(10)
				.build();
		when(audienceDao.findByRoom(audience1.getRoom())).thenReturn(Optional.of(audience2));
		Exception exception = assertThrows(EntityNotUniqueException.class, () -> {
			audienceService.save(audience1);
		});

		assertEquals("Audience with room number 10 is already exists!", exception.getMessage());
	}
}
