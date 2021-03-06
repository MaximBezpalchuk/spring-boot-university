package com.foxminded.university.service;

import com.foxminded.university.dao.HolidayRepository;
import com.foxminded.university.exception.EntityNotFoundException;
import com.foxminded.university.exception.EntityNotUniqueException;
import com.foxminded.university.model.Holiday;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class HolidayServiceTest {

    @Mock
    private HolidayRepository holidayRepository;
    @InjectMocks
    private HolidayService holidayService;

    @Test
    void givenListOfHolidays_whenFindAll_thenAllExistingHolidaysFound() {
        Holiday holiday1 = Holiday.builder().id(1).build();
        List<Holiday> expected = Arrays.asList(holiday1);
        when(holidayRepository.findAll()).thenReturn(expected);
        List<Holiday> actual = holidayService.findAll();

        assertEquals(expected, actual);
    }

    @Test
    void givenPageable_whenFindAll_thenAllPageableHolidaysFound() {
        List<Holiday> holidays = Arrays.asList(Holiday.builder().id(1).build());
        Page<Holiday> expected = new PageImpl<>(holidays, PageRequest.of(0, 1), 1);
        when(holidayRepository.findAll(PageRequest.of(0, 1))).thenReturn(expected);
        Page<Holiday> actual = holidayService.findAll(PageRequest.of(0, 1));

        assertEquals(expected, actual);
    }

    @Test
    void givenExistingHoliday_whenFindById_thenHolidayFound() {
        Optional<Holiday> expected = Optional.of(Holiday.builder().id(1).build());
        when(holidayRepository.findById(1)).thenReturn(expected);
        Holiday actual = holidayService.findById(1);

        assertEquals(expected.get(), actual);
    }

    @Test
    void givenExistingHoliday_whenFindById_thenEntityNotFoundException() {
        when(holidayRepository.findById(10)).thenReturn(Optional.empty());
        Exception exception = assertThrows(EntityNotFoundException.class, () -> {
            holidayService.findById(10);
        });

        assertEquals("Can`t find any holiday with id: 10", exception.getMessage());
    }

    @Test
    void givenNewHoliday_whenSave_thenSaved() {
        Holiday holiday = Holiday.builder().build();
        holidayService.save(holiday);

        verify(holidayRepository).save(holiday);
    }

    @Test
    void givenExistingHoliday_whenSave_thenSaved() {
        Holiday holiday = Holiday.builder()
            .name("TestName")
            .date(LocalDate.of(2020, 1, 1))
            .build();
        when(holidayRepository.findByNameAndDate(holiday.getName(), holiday.getDate())).thenReturn(Optional.of(holiday));
        holidayService.save(holiday);

        verify(holidayRepository).save(holiday);
    }

    @Test
    void givenExistingHolidayId_whenDelete_thenDeleted() {
        holidayService.delete(1);

        verify(holidayRepository).deleteById(1);
    }

    @Test
    void givenNotUniqueAudience_whenSave_thenEntityNotUniqueException() {
        Holiday holiday1 = Holiday.builder()
            .id(1)
            .name("TestName")
            .date(LocalDate.of(2020, 1, 1))
            .build();
        Holiday holiday2 = Holiday.builder()
            .id(10)
            .name("TestName")
            .date(LocalDate.of(2020, 1, 1))
            .build();
        when(holidayRepository.findByNameAndDate(holiday1.getName(), holiday1.getDate())).thenReturn(Optional.of(holiday2));
        Exception exception = assertThrows(EntityNotUniqueException.class, () -> {
            holidayService.save(holiday1);
        });

        assertEquals("Holiday with name TestName and date 2020-01-01 is already exists!", exception.getMessage());
    }
}
