package com.foxminded.university.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.jdbc.JdbcTestUtils.countRowsInTableWhere;
import static org.springframework.test.jdbc.JdbcTestUtils.countRowsInTable;


import java.time.LocalDate;
import java.util.ArrayList;
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

import com.foxminded.university.config.SpringTestConfig;
import com.foxminded.university.dao.jdbc.JdbcTeacherDao;
import com.foxminded.university.model.Cathedra;
import com.foxminded.university.model.Degree;
import com.foxminded.university.model.Gender;
import com.foxminded.university.model.Subject;
import com.foxminded.university.model.Teacher;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = SpringTestConfig.class)
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
public class JdbcTeacherDaoTest {

	private final static String TABLE_NAME = "teachers";
	@Autowired
	private JdbcTemplate template;
	@Autowired
	private JdbcTeacherDao teacherDao;

	@Test
	void whenFindAll_thenAllExistingTeachersFound() {
		int expected = countRowsInTable(template, TABLE_NAME);
		int actual = teacherDao.findAll().size();

		assertEquals(expected, actual);
	}

	@Test
	void givenExistingTeacher_whenFindById_thenTeacherFound() {
		Optional<Teacher> actual = teacherDao.findById(1);
		Optional<Teacher> expected = Optional.of(Teacher.builder()
				.firstName("Daniel")
				.lastName("Morpheus")
				.address("Virtual Reality Capsule no 1")
				.gender(Gender.MALE)
				.birthDate(LocalDate.of(1970, 1, 1))
				.cathedra(Cathedra.builder().id(1).name("Fantastic Cathedra").build())
				.degree(Degree.PROFESSOR)
				.phone("1")
				.email("1@bigowl.com")
				.postalCode("12345")
				.education("Higher education")
				.id(1)
				.build());
		List<Subject> subjects = new ArrayList<>();
		Subject subject = Subject.builder()
				.cathedra(Cathedra.builder().id(1).name("Fantastic Cathedra").build())
				.name("Weapon Tactics")
				.description("Learning how to use heavy weapon and guerrilla tactics")
				.id(1)
				.build();
		subjects.add(subject);
		expected.get().setSubjects(subjects);

		assertEquals(expected, actual);
	}

	@Test
	void givenNotExistingTeacher_whenFindById_thenReturnEmptyOptional() {
		assertEquals(teacherDao.findById(100), Optional.empty());
	}

	@Test
	void givenNewTeacher_whenSaveTeacher_thenAllExistingTeachersFound() {
		int expected = countRowsInTable(template, TABLE_NAME) + 1;
		Teacher teacher = Teacher.builder()
				.firstName("Test")
				.lastName("Test")
				.address("Virtual Reality Capsule no 1")
				.gender(Gender.MALE)
				.birthDate(LocalDate.of(1970, 1, 1))
				.cathedra(Cathedra.builder().id(1).build())
				.degree(Degree.PROFESSOR)
				.phone("1")
				.email("1@bigowl.com")
				.postalCode("12345")
				.education("Higher education")
				.build();
		List<Subject> subjects = new ArrayList<>();
		Subject subject = Subject.builder()
				.cathedra(Cathedra.builder().id(1).build())
				.name("Weapon Tactics")
				.description("Learning how to use heavy weapon and guerrilla tactics")
				.id(1)
				.build();
		subjects.add(subject);
		teacher.setSubjects(subjects);
		teacherDao.save(teacher);

		assertEquals(expected, countRowsInTable(template, TABLE_NAME));
	}

	@Test
	void givenExitstingTeacher_whenSaveWithChanges_thenChangesApplied() {
		Teacher expected = Teacher.builder()
				.id(1)
				.firstName("TestName")
				.lastName("Test")
				.address("Virtual Reality Capsule no 2")
				.gender(Gender.MALE)
				.birthDate(LocalDate.of(1970, 1, 1))
				.cathedra(Cathedra.builder().id(1).build())
				.degree(Degree.PROFESSOR)
				.phone("1")
				.email("123@bigowl.com")
				.postalCode("1234567")
				.education("Higher education123")
				.build();
		List<Subject> subjects = new ArrayList<>();
		Subject subject = Subject.builder()
				.cathedra(Cathedra.builder()
						.id(1)
						.name("Fantastic Cathedra")
						.build())
				.name("Weapon Tactics")
				.description("Learning how to use heavy weapon and guerrilla tactics")
				.id(1)
				.build();
		subjects.add(subject);
		expected.setSubjects(subjects);
		teacherDao.save(expected);

		assertEquals(1, countRowsInTableWhere(template, TABLE_NAME,
				"id = 1 "
				+ "AND first_name = 'TestName' "
				+ "AND last_name = 'Test' "
				+ "AND address = 'Virtual Reality Capsule no 2' "
				+ "AND gender = 'MALE' "
				+ "AND birth_date = '1970-01-01' "
				+ "AND cathedra_id = 1 "
				+ "AND degree = 'PROFESSOR' "
				+ "AND phone = '1' "
				+ "AND email = '123@bigowl.com' "
				+ "AND postal_code = '1234567' AND education = 'Higher education123'"));
		
		assertEquals(1, countRowsInTableWhere(template, "subjects_teachers", "teacher_id = 1"));
	}

	@Test
	void whenDeleteExistingTeacher_thenAllExistingTeachersFound() {
		int expected = countRowsInTable(template, TABLE_NAME) - 1;
		teacherDao.deleteById(1);

		assertEquals(expected, countRowsInTable(template, TABLE_NAME));
	}

	@Test
	void givenExitstingTeacher_whenUpdateSubjects_thenAllExistingTeachersFound() {
		int expected = countRowsInTable(template, "subjects_teachers") + 1;
		Teacher teacher = teacherDao.findById(1).get();
		teacher.getSubjects().add(Subject.builder()
				.cathedra(teacher.getCathedra())
				.name("Wandless Magic")
				.description("Learning how to use spells without magic wand")
				.id(2)
				.build());
		teacherDao.save(teacher);
		int actual = countRowsInTable(template, "subjects_teachers");

		assertEquals(expected, actual);
	}
	
	@Test
	void givenFirstNameAndLastNameAndBirthDate_whenFindByFullNameAndBirthDate_thenTeacherFound() {
		Cathedra cathedra = Cathedra.builder().id(1).name("Fantastic Cathedra").build();
		Optional<Teacher> expected = Optional.of(Teacher.builder()
				.firstName("Daniel")
				.lastName("Morpheus")
				.address("Virtual Reality Capsule no 1")
				.gender(Gender.MALE)
				.birthDate(LocalDate.of(1970, 1, 1))
				.cathedra(cathedra)
				.degree(Degree.PROFESSOR)
				.phone("1")
				.email("1@bigowl.com")
				.postalCode("12345")
				.education("Higher education")
				.id(1)
				.build());
		List<Subject> subjects = new ArrayList<>();
		Subject subject = Subject.builder().cathedra(cathedra).name("Weapon Tactics")
				.description("Learning how to use heavy weapon and guerrilla tactics").id(1).build();
		subjects.add(subject);
		expected.get().setSubjects(subjects);
		Optional<Teacher> actual = teacherDao.findByFullNameAndBirthDate(expected.get().getFirstName(),
				expected.get().getLastName(), expected.get().getBirthDate());

		assertEquals(expected, actual);
	}
}
