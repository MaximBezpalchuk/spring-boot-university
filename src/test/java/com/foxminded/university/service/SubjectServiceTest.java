package com.foxminded.university.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.isA;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.foxminded.university.dao.jdbc.JdbcSubjectDao;
import com.foxminded.university.exception.EntityNotFoundException;
import com.foxminded.university.exception.EntityNotUniqueException;
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
	void givenPageable_whenFindAll_thenAllPageableSubjectsFound() {
		List<Subject> subjects = Arrays.asList(Subject.builder().id(1).build());
		Page<Subject> expected = new PageImpl<>(subjects, PageRequest.of(0, 1), 1);
		when(subjectDao.findPaginatedSubjects(isA(Pageable.class))).thenReturn(expected);
		Page<Subject> actual = subjectService.findAll(PageRequest.of(0, 1));

		assertEquals(expected, actual);
	}

	@Test
	void givenExistingSubject_whenFindById_thenSubjectFound() {
		Optional<Subject> expected = Optional.of(Subject.builder().id(1).build());
		when(subjectDao.findById(1)).thenReturn(expected);
		Subject actual = subjectService.findById(1);

		assertEquals(expected.get(), actual);
	}

	@Test
	void givenExistingSubject_whenFindById_thenEntityNotFoundException() {
		when(subjectDao.findById(10)).thenReturn(Optional.empty());
		Exception exception = assertThrows(EntityNotFoundException.class, () -> {
			subjectService.findById(10);
		});

		assertEquals("Can`t find any subject with id: 10", exception.getMessage());
	}

	@Test
	void givenNewSubject_whenSave_thenSaved() {
		Subject subject = Subject.builder().id(1).build();
		subjectService.save(subject);

		verify(subjectDao).save(subject);
	}

	@Test
	void givenExistingSubject_whenSave_thenSaved() {
		Subject subject = Subject.builder().id(1).name("TestName").build();
		when(subjectDao.findByName(subject.getName())).thenReturn(Optional.of(subject));
		subjectService.save(subject);

		verify(subjectDao).save(subject);
	}

	@Test
	void givenExistingSubjectId_whenDelete_thenDeleted() {
		subjectService.deleteById(1);

		verify(subjectDao).deleteById(1);
	}

	@Test
	void givenNotUniqueSubject_whenSave_thenEntityNotUniqueException() {
		Subject subject1 = Subject.builder().id(1).name("TestName").build();
		Subject subject2 = Subject.builder().id(2).name("TestName").build();
		when(subjectDao.findByName(subject1.getName())).thenReturn(Optional.of(subject2));
		Exception exception = assertThrows(EntityNotUniqueException.class, () -> {
			subjectService.save(subject1);
		});

		assertEquals("Subject with name TestName is already exists!", exception.getMessage());
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
