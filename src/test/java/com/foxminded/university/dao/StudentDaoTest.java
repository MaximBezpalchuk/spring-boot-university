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
import com.foxminded.university.model.Gender;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = { SpringTestConfig.class })
public class StudentDaoTest {

	private final static String TABLE_NAME = "students";
	@Autowired
	private JdbcTemplate template;
	@Autowired
	private StudentDao studentDao;
	@Autowired
	private GroupDao groupDao;

	@Test
	void whenFindAll_thenAllExistingStudentsFound() {
		int expected = countRowsInTable(template, TABLE_NAME);
		int actual = studentDao.findAll().size();

		assertEquals(expected, actual);
	}

	@Test
	void givenExistingStudent_whenFindById_thenStudentFound() {
		Student expected = Student.builder().setFirstName("Petr").setLastName("Orlov").setPhone("888005353535")
				.setAddress("Empty Street 8").setEmail("1@owl.com").setGender(Gender.MALE).setPostalCode("999")
				.setEducation("General secondary education").setBirthDate(LocalDate.of(1994, 3, 3))
				.setGroup(groupDao.findById(1)).setId(1).build();
		Student actual = studentDao.findById(1);

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
		Student student = Student.builder().setFirstName("Petr123").setLastName("Orlov123").setPhone("888005353535")
				.setAddress("Empty Street 8").setEmail("1@owl.com").setGender(Gender.MALE).setPostalCode("999")
				.setEducation("General secondary education").setBirthDate(LocalDate.of(1994, 3, 3))
				.setGroup(groupDao.findById(1)).build();
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
