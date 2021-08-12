package com.foxminded.university.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.jdbc.JdbcTestUtils.countRowsInTable;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.MethodMode;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.foxminded.university.model.Student;
import com.foxminded.university.config.SpringTestConfig;
import com.foxminded.university.dao.jdbc.JdbcStudentDao;
import com.foxminded.university.model.Gender;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = SpringTestConfig.class)
public class JdbcStudentDaoTest {

	private final static String TABLE_NAME = "students";
	@Autowired
	private JdbcTemplate template;
	@Autowired
	private JdbcStudentDao studentDao;

	@Test
	void whenFindAll_thenAllExistingStudentsFound() {
		int expected = countRowsInTable(template, TABLE_NAME);
		int actual = studentDao.findAll().size();

		assertEquals(expected, actual);
	}

	@Test
	void givenExistingStudent_whenFindById_thenStudentFound() {
		Student actual = studentDao.findById(1);
		Student expected = Student.build("Petr", "Orlov", "Empty Street 8", Gender.MALE, LocalDate.of(1994, 3, 3))
				.phone("888005353535").email("1@owl.com").postalCode("999").education("General secondary education")
				.group(actual.getGroup()).id(1).build();

		assertEquals(expected, actual);
	}

	@Test
	void givenNotExistingStudent_whenFindOne_thenIncorrestResultSize() {
		Exception exception = assertThrows(EmptyResultDataAccessException.class, () -> {
			studentDao.findById(100);
		});
		String expectedMessage = "Incorrect result size";
		String actualMessage = exception.getMessage();

		assertTrue(actualMessage.contains(expectedMessage));
	}

	@DirtiesContext(methodMode = MethodMode.AFTER_METHOD)
	@Test
	void givenNewStudent_whenSaveStudent_thenAllExistingStudentsFound() {
		int expected = countRowsInTable(template, TABLE_NAME) + 1;
		Student actual = studentDao.findById(1);
		Student student = Student.build("Petr123", "Orlov123", "Empty Street 8", Gender.MALE, LocalDate.of(1994, 3, 3))
				.phone("888005353535").email("1@owl.com").postalCode("999").education("General secondary education")
				.group(actual.getGroup()).build();
		studentDao.save(student);

		assertEquals(expected, countRowsInTable(template, TABLE_NAME));
	}

	@DirtiesContext(methodMode = MethodMode.AFTER_METHOD)

	@Test
	void givenExitstingStudent_whenChange_thenAllExistingStudentsFound() {
		int expected = countRowsInTable(template, TABLE_NAME);
		Student student = studentDao.findById(1);
		student.setFirstName("Test Name");
		studentDao.save(student);
		int actual = countRowsInTable(template, TABLE_NAME);

		assertEquals(expected, actual);
	}

	@DirtiesContext(methodMode = MethodMode.AFTER_METHOD)

	@Test
	void whenDeleteExistingStudent_thenAllExistingStudentsFound() {
		int expected = countRowsInTable(template, TABLE_NAME) - 1;
		studentDao.deleteById(3);

		assertEquals(expected, countRowsInTable(template, TABLE_NAME));
	}

}