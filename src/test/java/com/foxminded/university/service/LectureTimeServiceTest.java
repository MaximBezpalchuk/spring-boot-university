package com.foxminded.university.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.foxminded.university.dao.jdbc.JdbcLectureTimeDao;
import com.foxminded.university.model.LectureTime;

@ExtendWith(MockitoExtension.class)
public class LectureTimeServiceTest {

	@Mock
	private JdbcLectureTimeDao lectureTimeDao;
	@InjectMocks
	private LectureTimeService lectureTimeService;

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
		LectureTime expected = LectureTime.builder().id(1).build();
		when(lectureTimeDao.findById(1)).thenReturn(expected);
		LectureTime actual = lectureTimeService.findById(1);

		assertEquals(expected, actual);
	}

	@Test
	void givenNewLectureTime_whenSave_thenSaved() {
		LocalTime start = LocalTime.of(9, 0);
		LocalTime end = LocalTime.of(10, 0);
		LectureTime lectureTime = LectureTime.builder()
				.start(start)
				.end(end)
				.build();
		String output = lectureTimeService.save(lectureTime);
		
		assertEquals("Lecture time added!", output);
	}
	
	@Test
	void givenExistingLectureTime_whenSave_thenSaved() {
		LocalTime start = LocalTime.of(9, 0);
		LocalTime end = LocalTime.of(10, 0);
		LectureTime lectureTime = LectureTime.builder()
				.start(start)
				.end(end)
				.build();
		when(lectureTimeDao.findByPeriod(start, end)).thenReturn(lectureTime);
		String output = lectureTimeService.save(lectureTime);
		
		assertEquals("Lecture time updated!", output);
	}
	
	@Test
	void givenLectureTimeLessThan30Minutes_whenSave_thenSaved() {
		LocalTime start = LocalTime.of(9, 0);
		LocalTime end = LocalTime.of(9, 20);
		LectureTime lectureTime = LectureTime.builder()
				.start(start)
				.end(end)
				.build();
		String output = lectureTimeService.save(lectureTime);
		
		assertEquals("Lecture can`t be less than 30 minutes", output);
	}
	
	@Test
	void givenLectureTimeWithWrongEnd_whenSave_thenSaved() {
		LocalTime start = LocalTime.of(9, 0);
		LocalTime end = LocalTime.of(8, 0);
		LectureTime lectureTime = LectureTime.builder()
				.start(start)
				.end(end)
				.build();
		String output = lectureTimeService.save(lectureTime);
		
		assertEquals("Lecture can`t start after end time", output);
	}

	@Test
	void givenExistingLectureTimeId_whenDelete_thenDeleted() {
		lectureTimeService.deleteById(1);

		verify(lectureTimeDao).deleteById(1);
	}
}
