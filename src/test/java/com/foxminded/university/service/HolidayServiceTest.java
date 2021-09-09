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

import com.foxminded.university.dao.jdbc.JdbcHolidayDao;
import com.foxminded.university.model.Holiday;

@ExtendWith(MockitoExtension.class)
public class HolidayServiceTest {

	@Mock
	private JdbcHolidayDao holidayDao;
	@InjectMocks
	private HolidayService holidayService;

	@Test
	void givenListOfHolidays_whenFindAll_thenAllExistingHolidaysFound() {
		Holiday holiday1 = Holiday.builder().id(1).build();
		List<Holiday> expected = Arrays.asList(holiday1);
		when(holidayDao.findAll()).thenReturn(expected);
		List<Holiday> actual = holidayService.findAll();

		assertEquals(expected, actual);
	}

	@Test
	void givenExistingHoliday_whenFindById_thenHolidayFound() {
		Holiday expected = Holiday.builder().id(1).build();
		when(holidayDao.findById(1)).thenReturn(expected);
		Holiday actual = holidayService.findById(1);

		assertEquals(expected, actual);
	}

	@Test
	void givenNewHoliday_whenSave_thenSaved() {
		Holiday holiday = Holiday.builder().build();
		holidayService.save(holiday);

		verify(holidayDao).save(holiday);
	}

	@Test
	void givenExistingHoliday_whenSave_thenSaved() {
		Holiday holiday = Holiday.builder().build();
		when(holidayDao.findByNameAndDate(holiday.getName(), holiday.getDate())).thenReturn(holiday);
		holidayService.save(holiday);

		verify(holidayDao).save(holiday);
	}

	@Test
	void givenExistingHolidayId_whenDelete_thenDeleted() {
		holidayService.deleteById(1);

		verify(holidayDao).deleteById(1);
	}
}
