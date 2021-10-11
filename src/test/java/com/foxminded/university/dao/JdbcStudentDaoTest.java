package com.foxminded.university.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.jdbc.JdbcTestUtils.countRowsInTable;
import static org.springframework.test.jdbc.JdbcTestUtils.countRowsInTableWhere;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;

import com.foxminded.university.model.Student;
import com.foxminded.university.config.SpringTestConfig;
import com.foxminded.university.dao.jdbc.JdbcStudentDao;
import com.foxminded.university.model.Cathedra;
import com.foxminded.university.model.Gender;
import com.foxminded.university.model.Group;

@ExtendWith(SpringExtension.class)
@WebAppConfiguration
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
		Optional<Student> actual = studentDao.findById(1);
		Optional<Student> expected = Optional.of(Student.builder()
				.firstName("Petr")
				.lastName("Orlov")
				.address("Empty Street 8")
				.gender(Gender.MALE)
				.birthDate(LocalDate.of(1994, 3, 3))
				.phone("888005353535")
				.email("1@owl.com")
				.postalCode("999")
				.education("General secondary education")
				.group(actual.get().getGroup())
				.id(1)
				.build());

		assertEquals(expected, actual);
	}

	@Test
	void givenNotExistingStudent_whenFindById_thenReturnEmptyOptional() {
		assertEquals(studentDao.findById(100), Optional.empty());
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
	
	@Test
	void givenFirstNameAndLastNameAndBirthDate_whenFindByFullNameAndBirthDate_thenStudentFound() {
		Optional<Student> expected = Optional.of(Student.builder()
				.firstName("Petr")
				.lastName("Orlov")
				.address("Empty Street 8")
				.gender(Gender.MALE)
				.birthDate(LocalDate.of(1994, 3, 3))
				.phone("888005353535")
				.email("1@owl.com")
				.postalCode("999")
				.education("General secondary education")
				.group(Group.builder().id(1).name("Killers").cathedra(Cathedra.builder().id(1).name("Fantastic Cathedra").build()).build())
				.id(1)
				.build());
		Optional<Student> actual = studentDao.findByFullNameAndBirthDate(expected.get().getFirstName(),
				expected.get().getLastName(), expected.get().getBirthDate());

		assertEquals(expected, actual);
	}
	
	@Test
	void givenGroupName_whenFindByGroupId_thenStudentsFound() {
		Student student1 = Student.builder()
				.firstName("Kim")
				.lastName("Cattrall")
				.address("Virtual Reality Capsule no 2")
				.gender(Gender.FEMALE)
				.birthDate(LocalDate.of(1956, 8, 21))
				.phone("312-555-0690:00")
				.email("4@owl.com")
				.postalCode("12345")
				.education("College education")
				.group(Group.builder().id(2).name("Mages").cathedra(Cathedra.builder().id(1).name("Fantastic Cathedra").build()).build())
				.id(4)
				.build();
		Student student2 = Student.builder()
				.firstName("Thomas")
				.lastName("Anderson")
				.address("Virtual Reality Capsule no 3")
				.gender(Gender.MALE)
				.birthDate(LocalDate.of(1962, 3, 11))
				.phone("312-555-5555")
				.email("5@owl.com")
				.postalCode("12345")
				.education("College education")
				.group(Group.builder().id(2).name("Mages").cathedra(Cathedra.builder().id(1).name("Fantastic Cathedra").build()).build())
				.id(5)
				.build();
		List<Student> expected = Arrays.asList(student1, student2);
		List<Student> actual = studentDao.findByGroupId(2);
		
		assertEquals(expected, actual);
	}
}
