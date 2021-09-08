package com.foxminded.university.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.foxminded.university.dao.jdbc.JdbcVacationDao;
import com.foxminded.university.model.Vacation;

@ExtendWith(MockitoExtension.class)
public class VacationServiceTest {

	@Mock
	private JdbcVacationDao vacationDao;
	@InjectMocks
	private VacationService vacationService;

	@Test
	void givenListOfVacations_whenFindAll_thenAllExistingVacationsFound() {
		Vacation vacation1 = Vacation.builder().id(1).build();
		List<Vacation> expected = Arrays.asList(vacation1);
		when(vacationDao.findAll()).thenReturn(expected);
		List<Vacation> actual = vacationService.findAll();

		assertEquals(expected, actual);
	}

	@Test
	void givenExistingVacation_whenFindById_thenVacationFound() {
		Vacation expected = Vacation.builder().id(1).build();
		when(vacationDao.findById(1)).thenReturn(expected);
		Vacation actual = vacationService.findById(1);

		assertEquals(expected, actual);
	}
	
	@Test
	void givenNewVacation_whenSave_thenSaved() {
		LocalDate start = LocalDate.of(2021, 1, 1);
		LocalDate end = LocalDate.of(2021, 1, 2);
		Vacation vacation = Vacation.builder().start(start).end(end).build();
		String output = vacationService.save(vacation);
		
		assertEquals("Vacation added!", output);
	}

	@Test
	void givenExistingVacation_whenSave_thenSaved() {
		LocalDate start = LocalDate.of(2021, 1, 1);
		LocalDate end = LocalDate.of(2021, 1, 2);
		Vacation vacation = Vacation.builder().id(1).start(start).end(end).build();
		when(vacationDao.findByPeriodAndTeacher(start, end, vacation.getTeacher())).thenReturn(vacation);
		String output = vacationService.save(vacation);
		
		assertEquals("Vacation updated!", output);
	}
	
	@Test
	void givenVacationLess1Day_whenSave_thenSaved() {
		LocalDate start = LocalDate.of(2021, 1, 1);
		LocalDate end = LocalDate.of(2021, 1, 1);
		Vacation vacation = Vacation.builder().id(1).start(start).end(end).build();
		when(vacationDao.findByPeriodAndTeacher(start, end, vacation.getTeacher())).thenReturn(vacation);
		String output = vacationService.save(vacation);
		
		assertEquals("Vacation can`t be less than 1 day", output);
	}
	
	@Test
	void givenVacationWithWrongDates_whenSave_thenSaved() {
		LocalDate start = LocalDate.of(2021, 1, 1);
		LocalDate end = LocalDate.of(2020, 1, 1);
		Vacation vacation = Vacation.builder().id(1).start(start).end(end).build();
		when(vacationDao.findByPeriodAndTeacher(start, end, vacation.getTeacher())).thenReturn(vacation);
		String output = vacationService.save(vacation);
		
		assertEquals("Vacation can`t start after end date", output);
	}

	@Test
	void givenExistingVacationId_whenDelete_thenDeleted() {
		vacationService.deleteById(1);

		verify(vacationDao).deleteById(1);
	}

	@Test
	void givenListOfVacations_whenFindByTeacherId_thenAllExistingVacationsFound() {
		Vacation vacation1 = Vacation.builder().id(1).build();
		Vacation vacation2 = Vacation.builder().id(1).build();
		List<Vacation> expected = Arrays.asList(vacation1, vacation2);
		when(vacationDao.findByTeacherId(2)).thenReturn(expected);
		List<Vacation> actual = vacationService.findByTeacherId(2);

		assertEquals(expected, actual);
	}
}
