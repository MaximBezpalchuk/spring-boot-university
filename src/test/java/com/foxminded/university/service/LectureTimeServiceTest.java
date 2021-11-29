package com.foxminded.university.service;

import com.foxminded.university.dao.LectureTimeDao;
import com.foxminded.university.exception.ChosenDurationException;
import com.foxminded.university.exception.DurationException;
import com.foxminded.university.exception.EntityNotFoundException;
import com.foxminded.university.exception.EntityNotUniqueException;
import com.foxminded.university.model.LectureTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class LectureTimeServiceTest {

    @Mock
    private LectureTimeDao lectureTimeDao;
    @InjectMocks
    private LectureTimeService lectureTimeService;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(lectureTimeService, "minLectureDurationInMinutes", 30);
    }

    @Test
    void givenListOfLectureTimes_whenFindAll_thenAllExistingLectureTimesFound() {
        LectureTime lectureTime1 = LectureTime.builder().id(1).build();
        List<LectureTime> expected = Arrays.asList(lectureTime1);
        when(lectureTimeDao.findAll()).thenReturn(expected);
        List<LectureTime> actual = lectureTimeService.findAll();

        assertEquals(expected, actual);
    }

    @Test
    void givenExistingLectureTime_whenFindById_thenLectureTimeFound() {
        Optional<LectureTime> expected = Optional.of(LectureTime.builder().id(1).build());
        when(lectureTimeDao.findById(1)).thenReturn(expected);
        LectureTime actual = lectureTimeService.findById(1);

        assertEquals(expected.get(), actual);
    }

    @Test
    void givenExistingLectureTime_whenFindById_thenEntityNotFoundException() {
        when(lectureTimeDao.findById(100)).thenReturn(Optional.empty());
        Exception exception = assertThrows(EntityNotFoundException.class, () -> {
            lectureTimeService.findById(100);
        });

        assertEquals("Can`t find any lecture time with id: 100", exception.getMessage());
    }

    @Test
    void givenNewLectureTime_whenSave_thenSaved() {
        LocalTime start = LocalTime.of(9, 0);
        LocalTime end = LocalTime.of(10, 0);
        LectureTime lectureTime = LectureTime.builder()
                .start(start)
                .end(end)
                .build();
        lectureTimeService.save(lectureTime);

        verify(lectureTimeDao).save(lectureTime);
    }

    @Test
    void givenExistingLectureTime_whenSave_thenSaved() {
        LocalTime start = LocalTime.of(9, 0);
        LocalTime end = LocalTime.of(10, 0);
        LectureTime lectureTime = LectureTime.builder()
                .start(start)
                .end(end)
                .build();
        when(lectureTimeDao.findByPeriod(start, end)).thenReturn(Optional.of(lectureTime));
        lectureTimeService.save(lectureTime);

        verify(lectureTimeDao).save(lectureTime);
    }

    @Test
    void givenNotUniqueLectureTime_whenSave_thenEntityNotUniqueException() {
        LocalTime start = LocalTime.of(9, 0);
        LocalTime end = LocalTime.of(10, 0);
        LectureTime lectureTime1 = LectureTime.builder()
                .id(1)
                .start(start)
                .end(end)
                .build();
        LectureTime lectureTime2 = LectureTime.builder()
                .id(2)
                .start(start)
                .end(end)
                .build();
        when(lectureTimeDao.findByPeriod(lectureTime1.getStart(),
                lectureTime1.getEnd())).thenReturn(Optional.of(lectureTime2));
        Exception exception = assertThrows(EntityNotUniqueException.class, () -> {
            lectureTimeService.save(lectureTime1);
        });

        assertEquals("Lecture time with start time 09:00 and end time 10:00 is already exists!",
                exception.getMessage());
    }

    @Test
    void givenLectureTimeLessThanMinLectureDuration_whenSave_thenLectureTimeDurationMoreThanChosenTimeException() {
        LocalTime start = LocalTime.of(9, 0);
        LocalTime end = LocalTime.of(9, 20);
        LectureTime lectureTime = LectureTime.builder()
                .start(start)
                .end(end)
                .build();
        Exception exception = assertThrows(ChosenDurationException.class, () -> {
            lectureTimeService.save(lectureTime);
        });

        assertEquals("Duration 20 minutes is less than min lecture duration (30 minutes)!", exception.getMessage());
    }

    @Test
    void givenLectureTimeWithWrongEnd_whenSave_thenLectureTimeNotCorrectException() {
        LocalTime start = LocalTime.of(9, 0);
        LocalTime end = LocalTime.of(8, 0);
        LectureTime lectureTime = LectureTime.builder()
                .start(start)
                .end(end)
                .build();
        Exception exception = assertThrows(DurationException.class, () -> {
            lectureTimeService.save(lectureTime);
        });

        assertEquals("Lecture time`s start (09:00) can`t be after lecture time`s end (08:00)!", exception.getMessage());
    }

    @Test
    void givenExistingLectureTimeId_whenDelete_thenDeleted() {
        lectureTimeService.deleteById(1);

        verify(lectureTimeDao).deleteById(1);
    }
}
