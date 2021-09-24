package com.foxminded.university.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.foxminded.university.dao.jdbc.JdbcTeacherDao;
import com.foxminded.university.exception.EntityNotFoundException;
import com.foxminded.university.exception.EntityNotUniqueException;
import com.foxminded.university.model.Teacher;

@ExtendWith(MockitoExtension.class)
public class TeacherServiceTest {

	@Mock
	private JdbcTeacherDao teacherDao;
	@InjectMocks
	private TeacherService teacherService;

	@Test
	void givenListOfTeachers_whenFindAll_thenAllExistingTeachersFound() {
		Teacher teacher1 = Teacher.builder().id(1).build();
		List<Teacher> expected = Arrays.asList(teacher1);
		when(teacherDao.findAll()).thenReturn(expected);
		List<Teacher> actual = teacherService.findAll();

		assertEquals(expected, actual);
	}

	@Test
	void givenExistingTeacher_whenFindById_thenTeacherFound() throws EntityNotFoundException {
		Optional<Teacher> expected = Optional.of(Teacher.builder().id(1).build());
		when(teacherDao.findById(1)).thenReturn(expected);
		Teacher actual = teacherService.findById(1);

		assertEquals(expected.get(), actual);
	}

	@Test
	void givenNewTeacher_whenSave_thenSaved() throws Exception {
		Teacher teacher = Teacher.builder().id(1).build();
		teacherService.save(teacher);

		verify(teacherDao).save(teacher);
	}

	@Test
	void givenExistingTeacher_whenSave_thenSaved() throws Exception {
		Teacher teacher = Teacher.builder().id(1)
				.firstName("TestFirstName")
				.lastName("TestLastName")
				.birthDate(LocalDate.of(1920, 2, 12))
				.build();
		when(teacherDao.findByFullNameAndBirthDate(teacher.getFirstName(), teacher.getLastName(),
				teacher.getBirthDate())).thenReturn(Optional.of(teacher));
		teacherService.save(teacher);

		verify(teacherDao).save(teacher);
	}

	@Test
	void givenExistingTeacherId_whenDelete_thenDeleted() {
		teacherService.deleteById(1);

		verify(teacherDao).deleteById(1);
	}
	
	@Test
	void givenNotUniqueTeacher_whenSave_thenEntityNotUniqueException() {
		Teacher teacher1 = Teacher.builder().id(1)
				.firstName("TestFirstName")
				.lastName("TestLastName")
				.birthDate(LocalDate.of(1920, 2, 12))
				.build();
		Teacher teacher2 = Teacher.builder().id(10)
				.firstName("TestFirstName")
				.lastName("TestLastName")
				.birthDate(LocalDate.of(1920, 2, 12))
				.build();
		when(teacherDao.findByFullNameAndBirthDate(teacher1.getFirstName(), teacher1.getLastName(),
				teacher1.getBirthDate())).thenReturn(Optional.of(teacher2));
		Exception exception = assertThrows(EntityNotUniqueException.class, () -> {
			teacherService.save(teacher1);
			});

		assertEquals("Teacher with same first name, last name and birth date is already exists!", exception.getMessage());
	}
}
