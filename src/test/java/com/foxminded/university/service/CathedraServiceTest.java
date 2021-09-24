package com.foxminded.university.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.foxminded.university.dao.jdbc.JdbcCathedraDao;
import com.foxminded.university.exception.EntityNotFoundException;
import com.foxminded.university.model.Cathedra;

@ExtendWith(MockitoExtension.class)
public class CathedraServiceTest {

	@Mock
	private JdbcCathedraDao cathedraDao;
	@InjectMocks
	private CathedraService cathedraService;

	@Test
	void givenListOfCathedras_whenFindAll_thenAllExistingCathedrasFound() {
		List<Cathedra> expected = Arrays.asList(Cathedra.builder().id(1).build());
		when(cathedraDao.findAll()).thenReturn(expected);
		List<Cathedra> actual = cathedraService.findAll();

		assertEquals(expected, actual);
	}

	@Test
	void givenExistingCathedra_whenFindById_thenCathedraFound() throws EntityNotFoundException {
		Optional<Cathedra> expected = Optional.of(Cathedra.builder().id(1).build());
		when(cathedraDao.findById(1)).thenReturn(expected);
		Cathedra actual = cathedraService.findById(1);

		assertEquals(expected.get(), actual);
	}

	@Test
	void givenNewCathedra_whenSave_thenSaved() throws Exception {
		Cathedra cathedra = Cathedra.builder().build();
		cathedraService.save(cathedra);
		
		verify(cathedraDao).save(cathedra);
	}
	
	@Test
	void givenExistingCathedra_whenSave_thenSaved() throws Exception {
		Cathedra cathedra = Cathedra.builder().name("TestName").build();
		when(cathedraDao.findByName(cathedra.getName())).thenReturn(Optional.of(cathedra));
		cathedraService.save(cathedra);
		
		verify(cathedraDao).save(cathedra);
	}

	@Test
	void givenExistingAudienceId_whenDelete_thenDeleted() {
		cathedraService.deleteById(1);

		verify(cathedraDao).deleteById(1);
	}
}
