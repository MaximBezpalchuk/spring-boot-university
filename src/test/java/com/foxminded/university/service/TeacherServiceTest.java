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

import com.foxminded.university.dao.jdbc.JdbcTeacherDao;
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
	void givenExistingTeacher_whenFindById_thenTeacherFound() {
		Teacher expected = Teacher.builder().id(1).build();
		when(teacherDao.findById(1)).thenReturn(expected);
		Teacher actual = teacherService.findById(1);

		assertEquals(expected, actual);
	}

	@Test
	void givenNewTeacher_whenSave_thenSaved() {
		Teacher teacher = Teacher.builder().id(1).build();
		String output = teacherService.save(teacher);

		assertEquals("Teacher added!", output);
	}

	@Test
	void givenExistingTeacher_whenSave_thenSaved() {
		Teacher teacher = Teacher.builder().id(1).build();
		when(teacherDao.findByFullNameAndBirthDate(teacher.getFirstName(), teacher.getLastName(),
				teacher.getBirthDate())).thenReturn(teacher);
		String output = teacherService.save(teacher);

		assertEquals("Teacher updated!", output);
	}

	@Test
	void givenExistingTeacherId_whenDelete_thenDeleted() {
		teacherService.deleteById(1);

		verify(teacherDao).deleteById(1);
	}
}
