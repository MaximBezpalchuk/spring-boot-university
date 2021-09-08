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

import com.foxminded.university.dao.jdbc.JdbcStudentDao;
import com.foxminded.university.model.Student;

@ExtendWith(MockitoExtension.class)
public class StudentServiceTest {

	@Mock
	private JdbcStudentDao studentDao;
	@InjectMocks
	private StudentService studentService;

	@Test
	void givenListOfStudents_whenFindAll_thenAllExistingStudentsFound() {
		Student student1 = Student.builder().id(1).build();
		List<Student> expected = Arrays.asList(student1);
		when(studentDao.findAll()).thenReturn(expected);
		List<Student> actual = studentService.findAll();

		assertEquals(expected, actual);
	}

	@Test
	void givenExistingStudent_whenFindById_thenStudentFound() {
		Student expected = Student.builder().id(1).build();
		when(studentDao.findById(1)).thenReturn(expected);
		Student actual = studentService.findById(1);

		assertEquals(expected, actual);
	}

	@Test
	void givenNewStudent_whenSave_thenSaved() {
		Student student = Student.builder().id(1).build();
		String output = studentService.save(student);

		assertEquals("Student added!", output);
	}

	@Test
	void givenExistingStudent_whenSave_thenSaved() {
		Student student = Student.builder().id(1).build();
		when(studentDao.findByFullNameAndBirthDate(student.getFirstName(), student.getLastName(),
				student.getBirthDate())).thenReturn(student);
		String output = studentService.save(student);

		assertEquals("Student updated!", output);
	}

	@Test
	void givenExistingStudentId_whenDelete_thenDeleted() {
		studentService.deleteById(1);

		verify(studentDao).deleteById(1);
	}
}
