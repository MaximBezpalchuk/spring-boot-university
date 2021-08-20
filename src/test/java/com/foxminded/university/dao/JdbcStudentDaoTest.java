package com.foxminded.university.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.jdbc.JdbcTestUtils.countRowsInTable;
import static org.springframework.test.jdbc.JdbcTestUtils.countRowsInTableWhere;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.foxminded.university.model.Student;
import com.foxminded.university.config.SpringTestConfig;
import com.foxminded.university.dao.jdbc.JdbcStudentDao;
import com.foxminded.university.model.Gender;
import com.foxminded.university.model.Group;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = SpringTestConfig.class)
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
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
		Student expected = Student.builder()
				.firstName("Petr")
				.lastName("Orlov")
				.address("Empty Street 8")
				.gender(Gender.MALE)
				.birthDate(LocalDate.of(1994, 3, 3))
				.phone("888005353535")
				.email("1@owl.com")
				.postalCode("999")
				.education("General secondary education")
				.group(actual.getGroup())
				.id(1)
				.build();

		assertEquals(expected, actual);
	}

	@Test
	void givenNotExistingStudent_whenFindById_thenIncorrestResultSize() {
		Exception exception = assertThrows(EmptyResultDataAccessException.class, () -> {
			studentDao.findById(100);
		});
		String expectedMessage = "Incorrect result size";
		String actualMessage = exception.getMessage();

		assertTrue(actualMessage.contains(expectedMessage));
	}

	@Test
	void givenNewStudent_whenSaveStudent_thenAllExistingStudentsFound() {
		int expected = countRowsInTable(template, TABLE_NAME) + 1;
		Student student = Student.builder()
				.firstName("Petr123")
				.lastName("Orlov123")
				.address("Empty Street 8")
				.gender(Gender.MALE)
				.birthDate(LocalDate.of(1994, 3, 3))
				.phone("888005353535")
				.email("1@owl.com")
				.postalCode("999")
				.education("General secondary education")
				.group(Group.builder().id(1).build())
				.build();
		studentDao.save(student);

		assertEquals(expected, countRowsInTable(template, TABLE_NAME));
	}

	@Test
	void givenExitstingStudent_whenSaveWithChanges_thenChangesApplied() {
		Student expected = Student.builder()
		.id(1)
		.firstName("Test Name")
		.lastName("Orlov123")
		.address("Empty Street 812312")
		.gender(Gender.MALE)
		.birthDate(LocalDate.of(1994, 3, 21))
		.phone("888005353535321123")
		.email("1123@owl.com")
		.postalCode("999123123")
		.education("General secondary education12321")
		.group(Group.builder().id(1).build())
		.build();
		studentDao.save(expected);
		
		assertEquals(1, countRowsInTableWhere(template, TABLE_NAME, 
				"id = 1 "
				+ "AND first_name = 'Test Name' "
				+ "AND last_name = 'Orlov123' "
				+ "AND address = 'Empty Street 812312' "
				+ "AND gender = 'MALE' "
				+ "AND birth_date = '1994-03-21' "
				+ "AND phone = '888005353535321123' "
				+ "AND email = '1123@owl.com' "
				+ "AND postal_code = '999123123' "
				+ "AND education = 'General secondary education12321'"
				+ "AND group_id = 1"));
	}

	@Test
	void whenDeleteExistingStudent_thenAllExistingStudentsFound() {
		int expected = countRowsInTable(template, TABLE_NAME) - 1;
		studentDao.deleteById(3);

		assertEquals(expected, countRowsInTable(template, TABLE_NAME));
	}
}
