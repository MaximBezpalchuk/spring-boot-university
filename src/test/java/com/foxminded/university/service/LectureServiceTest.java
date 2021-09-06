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

import com.foxminded.university.dao.jdbc.JdbcLectureDao;
import com.foxminded.university.model.Lecture;

@ExtendWith(MockitoExtension.class)
public class LectureServiceTest {

	@Mock
	private JdbcLectureDao lectureDao;
	@InjectMocks
	private LectureService lectureService;

	@Test
	void givenListOfLectures_whenFindAll_thenAllExistingLecturesFound() {
		Lecture lecture1 = Lecture.builder().id(1).build();
		List<Lecture> expected = Arrays.asList(lecture1);
		when(lectureDao.findAll()).thenReturn(expected);
		List<Lecture> actual = lectureService.findAll();

		assertEquals(expected, actual);
	}

	@Test
	void givenExistingLecture_whenFindById_thenLectureFound() {
		Lecture expected = Lecture.builder().id(1).build();
		when(lectureDao.findById(1)).thenReturn(expected);
		Lecture actual = lectureService.findById(1);

		assertEquals(expected, actual);
	}

	@Test
	void givenNewLecture_whenSave_thenSaved() {
		Lecture lecture = Lecture.builder().id(1).build();
		lectureService.save(lecture);

		verify(lectureDao).save(lecture);
	}

	@Test
	void givenExistingLectureId_whenDelete_thenDeleted() {
		lectureService.deleteById(1);

		verify(lectureDao).deleteById(1);
	}
}
