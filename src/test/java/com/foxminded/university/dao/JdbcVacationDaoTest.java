package com.foxminded.university.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.jdbc.JdbcTestUtils.countRowsInTable;
import static org.springframework.test.jdbc.JdbcTestUtils.countRowsInTableWhere;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.foxminded.university.config.TestConfig;
import com.foxminded.university.dao.jdbc.JdbcVacationDao;
import com.foxminded.university.model.Cathedra;
import com.foxminded.university.model.Degree;
import com.foxminded.university.model.Gender;
import com.foxminded.university.model.Subject;
import com.foxminded.university.model.Teacher;
import com.foxminded.university.model.Vacation;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = TestConfig.class)
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
public class JdbcVacationDaoTest {

	private final static String TABLE_NAME = "vacations";
	@Autowired
	private JdbcTemplate template;
	@Autowired
	private JdbcVacationDao vacationDao;

	@Test
	void whenFindAll_thenAllExistingVacationsFound() {
		int expected = countRowsInTable(template, TABLE_NAME);
		int actual = vacationDao.findAll().size();

		assertEquals(expected, actual);
	}
	
	@Test
	void givenPageable_whenFindPaginatedVacations_thenVacationsFound() {
		Page<Vacation> actual = vacationDao.findPaginatedVacationsByTeacherId(PageRequest.of(0,1), 1);
		List<Vacation> vacations = List.of(Vacation.builder()
				.id(1)
				.start(LocalDate.of(2021, 1, 15))
				.end(LocalDate.of(2021, 1, 29))
				.teacher(actual.getContent().get(0).getTeacher())
				.build());
		Page<Vacation> expected = new PageImpl<>(vacations, PageRequest.of(0, 1), 2);
		
		assertEquals(expected, actual);
	}

	@Test
	void givenExistingVacation_whenFindById_thenVacationFound() {
		Optional<Vacation> actual = vacationDao.findById(1);
		Optional<Vacation> expected = Optional.of(Vacation.builder()
				.id(1)
				.start(LocalDate.of(2021, 1, 15))
				.end(LocalDate.of(2021, 1, 29))
				.teacher(actual.get().getTeacher())
				.build());

		assertEquals(expected, actual);
	}

	@Test
	void givenNotExistingVacation_whenFindById_thenReturnEmptyOptional() {
		assertEquals(vacationDao.findById(100), Optional.empty());
	}

	@Test
	void givenNewVacation_whenSaveVacation_thenAllExistingVacationsFound() {
		int expected = countRowsInTable(template, TABLE_NAME) + 1;
		vacationDao.save(Vacation.builder()
				.start(LocalDate.of(2021, 1, 31))
				.end(LocalDate.of(2021, 3, 29))
				.teacher(Teacher.builder().id(1).build())
				.build());

		assertEquals(expected, countRowsInTable(template, TABLE_NAME));
	}

	@Test
	void givenExitstingVacation_whenChange_thenChangesApplied() {
		Vacation expected = Vacation.builder()
				.id(1)
				.start(LocalDate.of(2021, 1, 1))
				.end(LocalDate.of(2021, 1, 1))
				.teacher(Teacher.builder().id(2).build())
				.build();
		vacationDao.save(expected);
		
		assertEquals(1, countRowsInTableWhere(template, TABLE_NAME, 
				"id = 1 "
				+ "AND teacher_id = 2"
				+ "AND start = '2021-01-01'"
				+ "AND finish = '2021-01-01'"));
	}

	@Test
	void whenDeleteExistingVacation_thenAllExistingVacationsFound() {
		int expected = countRowsInTable(template, TABLE_NAME) - 1;
		vacationDao.deleteById(1);

		assertEquals(expected, countRowsInTable(template, TABLE_NAME));
	}

	@Test
	void givenExistingVacation_whenFindByTeacherId_thenVacationFound() {
		List<Vacation> expected = new ArrayList<>();
		List<Vacation> actual = vacationDao.findByTeacherId(1);
		Vacation vacation1 = Vacation.builder()
				.id(1)
				.start(LocalDate.of(2021, 1, 15))
				.end(LocalDate.of(2021, 1, 29))
				.teacher(actual.get(0).getTeacher())
				.build();
		Vacation vacation2 = Vacation.builder()
				.id(2)
				.start(LocalDate.of(2021, 6, 15))
				.end(LocalDate.of(2021, 6, 29))
				.teacher(actual.get(0).getTeacher())
				.build();
		expected.add(vacation1);
		expected.add(vacation2);

		assertEquals(expected, actual);
	}
	
	@Test
	void givenStartAndEndAndTeacher_whenFindByPeriodAndTeacher_thenVacationFound() {
		Cathedra cathedra = Cathedra.builder().id(1).name("Fantastic Cathedra").build();
		Teacher teacher = Teacher.builder()
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
				.build();
		List<Subject> subjects = new ArrayList<>();
		Subject subject = Subject.builder().cathedra(cathedra).name("Weapon Tactics")
				.description("Learning how to use heavy weapon and guerrilla tactics").id(1).build();
		subjects.add(subject);
		teacher.setSubjects(subjects);
		
		Optional<Vacation> expected = Optional.of(Vacation.builder()
				.id(1)
				.start(LocalDate.of(2021, 1, 15))
				.end(LocalDate.of(2021, 1, 29))
				.teacher(teacher)
				.build());
		Optional<Vacation> actual = vacationDao.findByPeriodAndTeacher(expected.get().getStart(),
				expected.get().getEnd(), teacher);

		assertEquals(expected, actual);
	}
	
	@Test
	void givenTeacherAndYear_whenFindByTeacherAndYear_thenVacationFound() {
		Cathedra cathedra = Cathedra.builder().id(1).name("Fantastic Cathedra").build();
		Teacher teacher = Teacher.builder()
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
				.build();
		List<Subject> subjects = new ArrayList<>();
		Subject subject = Subject.builder().cathedra(cathedra).name("Weapon Tactics")
				.description("Learning how to use heavy weapon and guerrilla tactics").id(1).build();
		subjects.add(subject);
		teacher.setSubjects(subjects);
		
		Vacation vacation1 = Vacation.builder()
				.id(1)
				.start(LocalDate.of(2021, 1, 15))
				.end(LocalDate.of(2021, 1, 29))
				.teacher(teacher)
				.build();
		Vacation vacation2 = Vacation.builder()
				.id(2)
				.start(LocalDate.of(2021, 6, 15))
				.end(LocalDate.of(2021, 6, 29))
				.teacher(teacher)
				.build();
		List<Vacation> actual = vacationDao.findByTeacherIdAndYear(1, 2021);

		assertEquals(Arrays.asList(vacation1, vacation2), actual);
	}
}
