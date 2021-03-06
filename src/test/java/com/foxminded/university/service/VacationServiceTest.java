package com.foxminded.university.service;

import com.foxminded.university.config.UniversityConfigProperties;
import com.foxminded.university.dao.VacationRepository;
import com.foxminded.university.exception.ChosenDurationException;
import com.foxminded.university.exception.DurationException;
import com.foxminded.university.exception.EntityNotFoundException;
import com.foxminded.university.exception.EntityNotUniqueException;
import com.foxminded.university.model.Degree;
import com.foxminded.university.model.Teacher;
import com.foxminded.university.model.Vacation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class VacationServiceTest {

    @Mock
    private VacationRepository vacationRepository;
    @Mock
    private UniversityConfigProperties universityConfig;
    @InjectMocks
    private VacationService vacationService;
    private Map<Degree, Integer> maxVacation;

    @BeforeEach
    void init() {
        maxVacation = new HashMap<>();
        maxVacation.put(Degree.PROFESSOR, 20);
        maxVacation.put(Degree.ASSISTANT, 16);
        maxVacation.put(Degree.UNKNOWN, 14);
        when(universityConfig.getMaxVacation()).thenReturn(maxVacation);
    }

    @Test
    void givenListOfVacations_whenFindAll_thenAllExistingVacationsFound() {
        Vacation vacation1 = Vacation.builder().id(1).build();
        List<Vacation> expected = Arrays.asList(vacation1);
        when(vacationRepository.findAll()).thenReturn(expected);
        List<Vacation> actual = vacationService.findAll();

        assertEquals(expected, actual);
    }

    @Test
    void givenPageable_whenFindAll_thenAllPageableVacationsFound() {
        List<Vacation> vacations = Arrays.asList(Vacation.builder().id(1).build());
        Page<Vacation> expected = new PageImpl<>(vacations, PageRequest.of(0, 1), 1);
        when(vacationRepository.findAllByTeacherId(PageRequest.of(0, 1), 1)).thenReturn(expected);
        Page<Vacation> actual = vacationService.findByTeacherId(PageRequest.of(0, 1), 1);

        assertEquals(expected, actual);
    }

    @Test
    void givenExistingVacation_whenFindById_thenVacationFound() {
        Optional<Vacation> expected = Optional.of(Vacation.builder().id(1).build());
        when(vacationRepository.findByTeacherIdAndId(1, 1)).thenReturn(expected);
        Vacation actual = vacationService.findByTeacherIdAndId(1, 1);

        assertEquals(expected.get(), actual);
    }

    @Test
    void givenExistingVacation_whenFindById_thenEntityNotFoundException() {
        when(vacationRepository.findById(10)).thenReturn(Optional.empty());
        Exception exception = assertThrows(EntityNotFoundException.class, () -> {
            vacationService.findByTeacherIdAndId(1, 10);
        });

        assertEquals("Can`t find any vacation with id: 10", exception.getMessage());
    }

    @Test
    void givenNewVacation_whenSave_thenSaved() {
        LocalDate start = LocalDate.of(2021, 1, 1);
        LocalDate end = LocalDate.of(2021, 1, 2);
        Vacation vacation = Vacation.builder()
            .start(start)
            .end(end)
            .teacher(Teacher.builder().id(1).degree(Degree.ASSISTANT).build())
            .build();
        vacationService.save(vacation);

        verify(vacationRepository).save(vacation);
    }

    @Test
    void givenExistingVacation_whenSave_thenSaved() {
        LocalDate start = LocalDate.of(2021, 1, 1);
        LocalDate end = LocalDate.of(2021, 1, 2);
        Vacation vacation = Vacation.builder()
            .id(1)
            .start(start)
            .end(end)
            .teacher(Teacher.builder().id(1).degree(Degree.ASSISTANT).build())
            .build();
        when(vacationRepository.findByStartAndEndAndTeacher(start, end, vacation.getTeacher())).thenReturn(Optional.of(vacation));
        vacationService.save(vacation);

        verify(vacationRepository).save(vacation);
    }

    @Test
    void givenNotUniqueVacation_whenSave_thenEntityNotUniqueException() {
        LocalDate start = LocalDate.of(2021, 1, 1);
        LocalDate end = LocalDate.of(2021, 1, 2);
        Vacation vacation1 = Vacation.builder()
            .id(1)
            .start(start)
            .end(end)
            .teacher(Teacher.builder().id(1).firstName("TestFirstName").lastName("TestLastName")
                .degree(Degree.ASSISTANT).build())
            .build();
        Vacation vacation2 = Vacation.builder()
            .id(11)
            .start(start)
            .end(end)
            .teacher(Teacher.builder().id(1).firstName("TestFirstName").lastName("TestLastName")
                .degree(Degree.ASSISTANT).build())
            .build();
        when(vacationRepository.findByStartAndEndAndTeacher(vacation1.getStart(), vacation1.getEnd(),
            vacation1.getTeacher())).thenReturn(Optional.of(vacation2));
        Exception exception = assertThrows(EntityNotUniqueException.class, () -> {
            vacationService.save(vacation1);
        });

        assertEquals(
            "Vacation with start(2021-01-01), end(2021-01-02) and teacher(TestFirstName TestLastName) id is already exists!",
            exception.getMessage());
    }

    @Test
    void givenVacationLessOneDay_whenSave_thenVacationLessOneDayException() {
        LocalDate start = LocalDate.of(2021, 1, 1);
        LocalDate end = LocalDate.of(2021, 1, 1);
        Vacation vacation = Vacation.builder().id(1).start(start).end(end).build();
        Exception exception = assertThrows(DurationException.class, () -> {
            vacationService.save(vacation);
        });

        assertEquals("Vacation can`t be less than 1 day! Vacation start is: 2021-01-01. Vacation end is: 2021-01-01",
            exception.getMessage());
    }

    @Test
    void givenVacationWithWrongDates_whenSave_thenVacationNotCorrectDateException() {
        LocalDate start = LocalDate.of(2021, 1, 1);
        LocalDate end = LocalDate.of(2020, 1, 1);
        Vacation vacation = Vacation.builder()
            .id(1)
            .start(start)
            .end(end)
            .build();
        Exception exception = assertThrows(DurationException.class, () -> {
            vacationService.save(vacation);
        });

        assertEquals(
            "Vacation start date can`t be after vacation end date! Vacation start is: 2021-01-01. Vacation end is: 2020-01-01",
            exception.getMessage());
    }

    @Test
    void givenVacationWithWrongDuration_whenSave_thenVacationDurationMoreThanMaxException() {
        LocalDate start = LocalDate.of(2021, 1, 1);
        LocalDate end = LocalDate.of(2021, 1, 10);
        Vacation vacation = Vacation.builder()
            .id(1)
            .start(start)
            .end(end)
            .teacher(Teacher.builder().id(1).degree(Degree.ASSISTANT).build())
            .build();
        when(vacationRepository.findByTeacherAndYear(vacation.getTeacher(), vacation.getStart().getYear()))
            .thenReturn(Arrays.asList(vacation));
        Exception exception = assertThrows(ChosenDurationException.class, () -> {
            vacationService.save(vacation);
        });

        assertEquals("Vacations duration(existing 9 plus appointed 9) can`t be more than max(16) for degree ASSISTANT!",
            exception.getMessage());
    }

    @Test
    void givenExistingVacationId_whenDelete_thenDeleted() {
        vacationService.delete(1);

        verify(vacationRepository).deleteById(1);
    }

    @Test
    void givenListOfVacations_whenFindByTeacherId_thenAllExistingVacationsFound() {
        Vacation vacation1 = Vacation.builder().id(1).build();
        Vacation vacation2 = Vacation.builder().id(1).build();
        List<Vacation> expected = Arrays.asList(vacation1, vacation2);
        when(vacationRepository.findByTeacherId(2)).thenReturn(expected);
        List<Vacation> actual = vacationService.findByTeacherId(2);

        assertEquals(expected, actual);
    }
}
