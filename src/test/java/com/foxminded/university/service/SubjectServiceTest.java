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

import com.foxminded.university.dao.jdbc.JdbcSubjectDao;
import com.foxminded.university.model.Subject;

@ExtendWith(MockitoExtension.class)
public class SubjectServiceTest {

	@Mock
	private JdbcSubjectDao subjectDao;
	@InjectMocks
	private SubjectService subjectService;

	@Test
	void givenListOfSubjects_whenFindAll_thenAllExistingSubjectsFound() {
		Subject subject1 = Subject.builder().id(1).build();
		List<Subject> expected = Arrays.asList(subject1);
		when(subjectDao.findAll()).thenReturn(expected);
		List<Subject> actual = subjectService.findAll();

		assertEquals(expected, actual);
	}

	@Test
	void givenExistingSubject_whenFindById_thenSubjectFound() {
		Subject expected = Subject.builder().id(1).build();
		when(subjectDao.findById(1)).thenReturn(expected);
		Subject actual = subjectService.findById(1);

		assertEquals(expected, actual);
	}

	@Test
	void givenNewSubject_whenSave_thenSaved() {
		Subject lectureTime = Subject.builder().id(1).build();
		subjectService.save(lectureTime);

		verify(subjectDao).save(lectureTime);
	}

	@Test
	void givenExistingSubjectId_whenDelete_thenDeleted() {
		subjectService.deleteById(1);

		verify(subjectDao).deleteById(1);
	}

	@Test
	void givenListOfSubjects_whenFindByTeacherId_thenAllExistingSubjectsFound() {
		Subject subject1 = Subject.builder().id(1).build();
		Subject subject2 = Subject.builder().id(1).build();
		List<Subject> expected = Arrays.asList(subject1, subject2);
		when(subjectDao.findByTeacherId(2)).thenReturn(expected);
		List<Subject> actual = subjectService.findByTeacherId(2);

		assertEquals(expected, actual);
	}
}
