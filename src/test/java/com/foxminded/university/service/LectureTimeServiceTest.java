package com.foxminded.university.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import com.foxminded.university.dao.jdbc.JdbcLectureTimeDao;
import com.foxminded.university.exception.EntityNotFoundException;
import com.foxminded.university.model.LectureTime;

@ExtendWith(MockitoExtension.class)
public class LectureTimeServiceTest {

	@Mock
	private JdbcLectureTimeDao lectureTimeDao;
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
	void givenExistingLectureTime_whenFindById_thenLectureTimeFound() throws EntityNotFoundException {
		Optional<LectureTime> expected = Optional.of(LectureTime.builder().id(1).build());
		when(lectureTimeDao.findById(1)).thenReturn(expected);
		LectureTime actual = lectureTimeService.findById(1);

		assertEquals(expected.get(), actual);
	}

	@Test
	void givenNewLectureTime_whenSave_thenSaved() throws Exception {
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
	void givenExistingLectureTime_whenSave_thenSaved() throws Exception {
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
	void givenLectureTimeLessThanMinLectureDuration_whenSave_thenNotSaved() throws Exception {
		LocalTime start = LocalTime.of(9, 0);
		LocalTime end = LocalTime.of(9, 20);
		LectureTime lectureTime = LectureTime.builder()
				.start(start)
				.end(end)
				.build();
		lectureTimeService.save(lectureTime);
		
		verify(lectureTimeDao, never()).save(lectureTime);
	}
	
	@Test
	void givenLectureTimeWithWrongEnd_whenSave_thenSaved() throws Exception {
		LocalTime start = LocalTime.of(9, 0);
		LocalTime end = LocalTime.of(8, 0);
		LectureTime lectureTime = LectureTime.builder()
				.start(start)
				.end(end)
				.build();
		lectureTimeService.save(lectureTime);
		
		verify(lectureTimeDao, never()).save(lectureTime);
	}

	@Test
	void givenExistingLectureTimeId_whenDelete_thenDeleted() {
		lectureTimeService.deleteById(1);

		verify(lectureTimeDao).deleteById(1);
	}
}
