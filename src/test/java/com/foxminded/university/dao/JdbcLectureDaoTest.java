package com.foxminded.university.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.jdbc.JdbcTestUtils.countRowsInTable;
import static org.springframework.test.jdbc.JdbcTestUtils.countRowsInTableWhere;

import java.time.LocalDate;
import java.time.LocalTime;
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
import com.foxminded.university.dao.jdbc.JdbcLectureDao;
import com.foxminded.university.model.Audience;
import com.foxminded.university.model.Cathedra;
import com.foxminded.university.model.Degree;
import com.foxminded.university.model.Gender;
import com.foxminded.university.model.Group;
import com.foxminded.university.model.Lecture;
import com.foxminded.university.model.LectureTime;
import com.foxminded.university.model.Subject;
import com.foxminded.university.model.Teacher;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = TestConfig.class)
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
public class JdbcLectureDaoTest {

	private final static String TABLE_NAME = "lectures";
	@Autowired
	private JdbcTemplate template;
	@Autowired
	private JdbcLectureDao lectureDao;

	@Test
	void whenFindAll_thenAllExistingLecturesFound() {
		int expected = countRowsInTable(template, TABLE_NAME);
		int actual = lectureDao.findAll().size();

		assertEquals(expected, actual);
	}

	@Test
	void givenExistingLecture_whenFindById_thenLectureFound() {
		Cathedra cathedra = Cathedra.builder().id(1).name("Fantastic Cathedra").build();
		Audience audience = Audience.builder().id(1).cathedra(cathedra).room(1).capacity(10).build();
		Group group = Group.builder().id(1).cathedra(cathedra).name("Killers").build();
		Subject subject = Subject.builder()
				.id(1)
				.cathedra(cathedra)
				.name("Weapon Tactics")
				.description("Learning how to use heavy weapon and guerrilla tactics")
				.build();
		Teacher teacher = Teacher.builder()
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
				.subjects(Arrays.asList(subject))
				.build();
		LectureTime time = LectureTime.builder()
				.id(1)
				.start(LocalTime.of(8, 0))
				.end(LocalTime.of(9, 30))
				.build();
		Optional<Lecture> expected = Optional.of(Lecture.builder()
				.id(1)
				.group(Arrays.asList(group))
				.cathedra(cathedra)
				.subject(subject)
				.date(LocalDate.of(2021, 4, 4))
				.time(time)
				.audience(audience)
				.teacher(teacher)
				.build());
		Optional<Lecture> actual = lectureDao.findById(1);

		assertEquals(expected, actual);
	}

	@Test
	void givenPageable_whenFindPaginatedLectures_thenLecturesFound() {
		Cathedra cathedra = Cathedra.builder().id(1).name("Fantastic Cathedra").build();
		Audience audience = Audience.builder().id(1).cathedra(cathedra).room(1).capacity(10).build();
		Group group = Group.builder().id(1).cathedra(cathedra).name("Killers").build();
		Subject subject = Subject.builder()
				.id(1)
				.cathedra(cathedra)
				.name("Weapon Tactics")
				.description("Learning how to use heavy weapon and guerrilla tactics")
				.build();
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
				.subjects(Arrays.asList(subject))
				.build();
		LectureTime time = LectureTime.builder()
				.id(1)
				.start(LocalTime.of(8, 0))
				.end(LocalTime.of(9, 30))
				.build();
		List<Lecture> lectures = Arrays.asList(Lecture.builder()
				.id(1)
				.group(Arrays.asList(group))
				.cathedra(cathedra)
				.subject(subject)
				.date(LocalDate.of(2021, 4, 4))
				.time(time)
				.audience(audience)
				.teacher(teacher)
				.build());
		Page<Lecture> expected = new PageImpl<>(lectures, PageRequest.of(0, 1), 11);
		Page<Lecture> actual = lectureDao.findPaginatedLectures(PageRequest.of(0, 1));

		assertEquals(expected, actual);
	}

	@Test
	void givenNotExistingLecture_whenFindById_thenReturnEmptyOptional() {
		assertEquals(lectureDao.findById(100), Optional.empty());
	}

	@Test
	void givenNewLecture_whenSaveLecture_thenAllExistingLecturesFound() {
		int expected = countRowsInTable(template, TABLE_NAME) + 1;
		lectureDao.save(
				Lecture.builder().cathedra(Cathedra.builder()
						.id(1).build())
						.subject(Subject.builder().id(1).build())
						.date(LocalDate.of(2021, 4, 4))
						.time(LectureTime.builder().id(1).build())
						.audience(Audience.builder().id(1).build())
						.teacher(Teacher.builder().id(1).build())
						.build());

		assertEquals(expected, countRowsInTable(template, TABLE_NAME));
	}

	@Test
	void givenExitstingLecture_whenSaveWithChanges_thenChangesApplied() {
		Lecture expected = Lecture.builder()
				.id(1)
				.cathedra(Cathedra.builder().id(1).build())
				.subject(Subject.builder().id(2).build())
				.date(LocalDate.of(2021, 4, 5))
				.time(LectureTime.builder().id(2).build())
				.audience(Audience.builder().id(2).build())
				.teacher(Teacher.builder().id(2).build())
				.group(new ArrayList<>()).build();
		lectureDao.save(expected);

		assertEquals(1, countRowsInTableWhere(template, TABLE_NAME,
				"id = 1 "
						+ "AND cathedra_id = 1 "
						+ "AND subject_id = 2 "
						+ "AND date = '2021-04-05' "
						+ "AND lecture_time_id = 2 "
						+ "AND audience_id = 2 "
						+ "AND teacher_id = 2"));

		assertEquals(0, countRowsInTableWhere(template, "lectures_groups", "lecture_id = 1 "));
	}

	@Test
	void whenDeleteExistingLecture_thenAllExistingLecturesFound() {
		int expected = countRowsInTable(template, TABLE_NAME) - 1;
		lectureDao.deleteById(3);

		assertEquals(expected, countRowsInTable(template, TABLE_NAME));
	}

	@Test
	void givenAudienceAndDate_whenFindByAudienceAndDate_thenLectureFound() {
		Cathedra cathedra = Cathedra.builder().id(1).name("Fantastic Cathedra").build();
		Audience audience = Audience.builder().id(1).cathedra(cathedra).room(1).capacity(10).build();
		Group group = Group.builder().id(1).cathedra(cathedra).name("Killers").build();
		Subject subject = Subject.builder()
				.id(1)
				.cathedra(cathedra)
				.name("Weapon Tactics")
				.description("Learning how to use heavy weapon and guerrilla tactics")
				.build();
		Teacher teacher = Teacher.builder()
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
				.subjects(Arrays.asList(subject))
				.build();
		LectureTime time = LectureTime.builder()
				.id(1)
				.start(LocalTime.of(8, 0))
				.end(LocalTime.of(9, 30))
				.build();
		Optional<Lecture> expected = Optional.of(Lecture.builder()
				.id(1)
				.group(Arrays.asList(group))
				.cathedra(cathedra)
				.subject(subject)
				.date(LocalDate.of(2021, 4, 4))
				.time(time)
				.audience(audience)
				.teacher(teacher)
				.build());
		Optional<Lecture> actual = lectureDao.findByAudienceDateAndLectureTime(Audience.builder().id(1).build(),
				LocalDate.of(2021, 4, 4),
				LectureTime.builder().id(1).build());

		assertEquals(expected, actual);
	}

	@Test
	void givenAudienceDateAndLectureTime_whenFindByAudienceDateAndLectureTime_thenLectureFound() {
		Cathedra cathedra = Cathedra.builder().id(1).name("Fantastic Cathedra").build();
		Audience audience = Audience.builder().id(1).cathedra(cathedra).room(1).capacity(10).build();
		Group group = Group.builder().id(1).cathedra(cathedra).name("Killers").build();
		Subject subject = Subject.builder()
				.id(1)
				.cathedra(cathedra)
				.name("Weapon Tactics")
				.description("Learning how to use heavy weapon and guerrilla tactics")
				.build();
		Teacher teacher = Teacher.builder()
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
				.subjects(Arrays.asList(subject))
				.build();
		LectureTime time = LectureTime.builder()
				.id(1)
				.start(LocalTime.of(8, 0))
				.end(LocalTime.of(9, 30))
				.build();
		Optional<Lecture> expected = Optional.of(Lecture.builder()
				.id(1)
				.group(Arrays.asList(group))
				.cathedra(cathedra)
				.subject(subject)
				.date(LocalDate.of(2021, 4, 4))
				.time(time)
				.audience(audience)
				.teacher(teacher)
				.build());
		Optional<Lecture> actual = lectureDao.findByAudienceDateAndLectureTime(Audience.builder().id(1).build(),
				LocalDate.of(2021, 4, 4), LectureTime.builder().id(1).build());

		assertEquals(expected, actual);
	}

	@Test
	void givenStudentId_whenFindByStudentId_thenLecturesFound() {
		List<Lecture> actual = lectureDao.findLecturesByStudentId(1);

		assertEquals(8, actual.size());
	}

	@Test
	void givenTeacherId_whenFindByTeacherId_thenLecturesFound() {
		List<Lecture> actual = lectureDao.findLecturesByTeacherId(2);

		assertEquals(7, actual.size());
	}
}