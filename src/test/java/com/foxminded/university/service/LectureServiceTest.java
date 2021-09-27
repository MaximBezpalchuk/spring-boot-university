package com.foxminded.university.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import com.foxminded.university.dao.jdbc.JdbcHolidayDao;
import com.foxminded.university.dao.jdbc.JdbcLectureDao;
import com.foxminded.university.dao.jdbc.JdbcStudentDao;
import com.foxminded.university.dao.jdbc.JdbcVacationDao;
import com.foxminded.university.exception.EntityNotFoundException;
import com.foxminded.university.exception.EntityNotUniqueException;
import com.foxminded.university.exception.LectureInAfterHoursException;
import com.foxminded.university.exception.LectureInOccupiedAudienceException;
import com.foxminded.university.exception.LectureInSmallAudienceException;
import com.foxminded.university.exception.LectureOnHolidayException;
import com.foxminded.university.exception.LectureOnSundayException;
import com.foxminded.university.exception.LectureWithBusyTeacherException;
import com.foxminded.university.exception.LectureWithNotCompetentTeacherException;
import com.foxminded.university.exception.LectureWithTeacherInVacationException;
import com.foxminded.university.model.Audience;
import com.foxminded.university.model.Cathedra;
import com.foxminded.university.model.Group;
import com.foxminded.university.model.Holiday;
import com.foxminded.university.model.Lecture;
import com.foxminded.university.model.LectureTime;
import com.foxminded.university.model.Student;
import com.foxminded.university.model.Subject;
import com.foxminded.university.model.Teacher;
import com.foxminded.university.model.Vacation;

@ExtendWith(MockitoExtension.class)
public class LectureServiceTest {

	@Mock
	private JdbcLectureDao lectureDao;
	@Mock
	private JdbcVacationDao vacationDao;
	@Mock
	private JdbcHolidayDao holidayDao;
	@Mock
	private JdbcStudentDao studentDao;
	@InjectMocks
	private LectureService lectureService;
	
	@BeforeEach
	void setUp() {
		ReflectionTestUtils.setField(lectureService, "startWorkingDay", 8);
		ReflectionTestUtils.setField(lectureService, "endWorkingDay", 22);
	}

	@Test
	void givenListOfLectures_whenFindAll_thenAllExistingLecturesFound() {
		Lecture lecture1 = Lecture.builder().id(1).build();
		List<Lecture> expected = Arrays.asList(lecture1);
		when(lectureDao.findAll()).thenReturn(expected);
		List<Lecture> actual = lectureService.findAll();

		assertEquals(expected, actual);
	}

	@Test
	void givenExistingLecture_whenFindById_thenLectureFound() {
		Optional<Lecture> expected = Optional.of(Lecture.builder().id(1).build());
		when(lectureDao.findById(1)).thenReturn(expected);
		Lecture actual = lectureService.findById(1);

		assertEquals(expected.get(), actual);
	}
	
	@Test
	void givenExistingLecture_whenFindById_thenEntityNotFoundException() {
		when(lectureDao.findById(10)).thenReturn(Optional.empty());
		Exception exception = assertThrows(EntityNotFoundException.class, () -> {
			lectureService.findById(10);
		});

		assertEquals("Can`t find any lecture with specified id!", exception.getMessage());
	}

	@Test
	void givenNewLecture_whenSave_thenSaved() {
		Cathedra cathedra = Cathedra.builder().id(1).name("Fantastic Cathedra").build();
		Teacher teacher = Teacher.builder()
				.id(1)
				.subjects(Arrays.asList(Subject.builder().id(1).cathedra(cathedra).build()))
				.build();
		Lecture lecture = Lecture.builder()
				.audience(Audience.builder().capacity(10).build())
				.date(LocalDate.of(2021, 9, 8))
				.time(LectureTime.builder()
				.start(LocalTime.of(9, 0))
				.end(LocalTime.of(10, 0)).build())
				.teacher(teacher)
				.subject(Subject.builder().id(1).cathedra(cathedra).build()).build();
		when(lectureDao.findByAudienceDateAndLectureTime(lecture.getAudience(), lecture.getDate(), lecture.getTime()))
				.thenReturn(Optional.of(lecture));
		lectureService.save(lecture);
		
		verify(lectureDao).save(lecture);
	}
	
	@Test
	void givenExistingLecture_whenSave_thenSaved() {
		Cathedra cathedra = Cathedra.builder().id(1).name("Fantastic Cathedra").build();
		Teacher teacher = Teacher.builder()
				.id(1)
				.subjects(Arrays.asList(Subject.builder().id(1).cathedra(cathedra).build()))
				.build();
		Audience audience = Audience.builder().id(1).capacity(100).build();
		LocalDate date = LocalDate.of(2021, 9, 8);
		LectureTime lectureTime = LectureTime.builder().start(LocalTime.of(9, 0)).end(LocalTime.of(10, 0)).build();
		Lecture lecture = Lecture.builder()
				.id(1)
				.date(date)
				.audience(audience)
				.time(lectureTime)
				.teacher(teacher)
				.subject(Subject.builder().id(1).cathedra(cathedra).build()).build();
		when(lectureDao.findByAudienceDateAndLectureTime(audience, date, lectureTime)).thenReturn(Optional.of(lecture));
		lectureService.save(lecture);
		
		verify(lectureDao).save(lecture);
	}
	
	@Test
	void givenNotUniqueLecture_whenSave_thenEntityNotUniqueException() {
		Cathedra cathedra = Cathedra.builder().id(1).name("Fantastic Cathedra").build();
		Teacher teacher = Teacher.builder()
				.id(1)
				.subjects(Arrays.asList(Subject.builder().id(1).cathedra(cathedra).build()))
				.build();
		Audience audience = Audience.builder().id(1).build();
		LocalDate date = LocalDate.of(2021, 9, 8);
		LectureTime lectureTime = LectureTime.builder().start(LocalTime.of(9, 0)).end(LocalTime.of(10, 0)).build();
		Lecture lecture1 = Lecture.builder()
				.id(1)
				.date(date)
				.audience(audience)
				.time(lectureTime)
				.teacher(teacher)
				.subject(Subject.builder().id(1).cathedra(cathedra).build()).build();
		Lecture lecture2 = Lecture.builder()
				.id(10)
				.date(date)
				.audience(audience)
				.time(lectureTime)
				.teacher(teacher)
				.subject(Subject.builder().id(1).cathedra(cathedra).build()).build();
		when(lectureDao.findByTeacherAudienceDateAndLectureTime(lecture1.getTeacher(), lecture1.getAudience(),
				lecture1.getDate(), lecture1.getTime())).thenReturn(Optional.of(lecture2));
		Exception exception = assertThrows(EntityNotUniqueException.class, () -> {
				lectureService.save(lecture1);
			});

		assertEquals("Lecture with same audience, date and lecture time is already exists!", exception.getMessage());
	}
	
	@Test
	void givenLectureInSunday_whenSave_thenLectureOnSundayException() {
		Lecture lecture = Lecture.builder()
				.id(1)
				.date(LocalDate.of(2021, 9, 5))
				.build();
		Exception exception = assertThrows(LectureOnSundayException.class, () -> {
			lectureService.save(lecture);
		});

	assertEquals("Lecture can`t be on sunday!", exception.getMessage());
	}
	
	@Test
	void givenLectureInNotSupportedTime_whenSave_thenLectureInAfterHoursException() {
		Lecture lecture = Lecture.builder()
				.id(1)
				.date(LocalDate.of(2021, 9, 6))
				.time(LectureTime.builder().id(1).start(LocalTime.of(7, 0)).end(LocalTime.of(10, 0)).build())
				.build();
		Exception exception = assertThrows(LectureInAfterHoursException.class, () -> {
			lectureService.save(lecture);
		});

	assertEquals("Lecture can`t be in after hours!", exception.getMessage());
	}
	
	@Test
	void givenLectureWithBusyTeacher_whenSave_thenLectureWithBusyTeacherException() {
		Lecture lecture = Lecture.builder()
				.id(1)
				.date(LocalDate.of(2021, 9, 6))
				.time(LectureTime.builder().id(1).start(LocalTime.of(9, 0)).end(LocalTime.of(10, 0)).build())
				.teacher(Teacher.builder().build())
				.build();
		Lecture lecture2 = Lecture.builder()
				.id(2)
				.build();
		when(lectureDao.findLecturesByTeacherDateAndTime(lecture.getTeacher(), lecture.getDate(), lecture.getTime())).thenReturn(Arrays.asList(lecture2));
		Exception exception = assertThrows(LectureWithBusyTeacherException.class, () -> {
			lectureService.save(lecture);
		});
		
		assertEquals("Teacher is on another lecture this time!", exception.getMessage());
	}
	
	@Test
	void givenLectureWithTeacherHaveVacation_whenSave_thenLectureWithTeacherInVacationException() {
		Lecture lecture = Lecture.builder()
				.id(1)
				.date(LocalDate.of(2021, 9, 6))
				.time(LectureTime.builder().id(1).start(LocalTime.of(9, 0)).end(LocalTime.of(10, 0)).build())
				.teacher(Teacher.builder().id(1).build())
				.build();
		Vacation vacation = Vacation.builder().start(LocalDate.of(2021, 9, 5)).end(LocalDate.of(2021, 9, 7)).build();
		when(vacationDao.findByDateInPeriodAndTeacher(lecture.getDate(), lecture.getTeacher())).thenReturn(Arrays.asList(vacation));
		Exception exception = assertThrows(LectureWithTeacherInVacationException.class, () -> {
			lectureService.save(lecture);
		});
		
		assertEquals("Teacher is in vacation this date!", exception.getMessage());
	}
	
	@Test
	void givenLectureInHoliday_whenSave_thenLectureOnHolidayException() {
		Lecture lecture = Lecture.builder()
				.id(1)
				.date(LocalDate.of(2021, 9, 6))
				.time(LectureTime.builder().id(1).start(LocalTime.of(9, 0)).end(LocalTime.of(10, 0)).build())
				.teacher(Teacher.builder().id(1).build())
				.build();
		when(holidayDao.findByDate(lecture.getDate())).thenReturn(Arrays.asList(Holiday.builder().date(LocalDate.of(2021, 9, 6)).name("Test").build()));
		Exception exception = assertThrows(LectureOnHolidayException.class, () -> {
			lectureService.save(lecture);
		});
		
		assertEquals("Lecture can`t be on holiday!", exception.getMessage());
	}
	
	@Test
	void givenLectureWithTeacherWhoDidntKnowSubject_whenSave_thenLectureWithNotCompetentTeacherException() {
		Lecture lecture = Lecture.builder()
				.id(1)
				.date(LocalDate.of(2021, 9, 6))
				.time(LectureTime.builder().id(1).start(LocalTime.of(9, 0)).end(LocalTime.of(10, 0)).build())
				.teacher(Teacher.builder().id(1).build())
				.subject(Subject.builder().build())
				.build();
		Exception exception = assertThrows(LectureWithNotCompetentTeacherException.class, () -> {
			lectureService.save(lecture);
		});
		
		assertEquals("Teacher can`t educate this subject!", exception.getMessage());
	}
	
	@Test
	void givenLectureWithMoreStudentsThenAudienceCapacity_whenSave_thenLectureInSmallAudienceException() {
		Subject subject = Subject.builder().build();
		Group group = Group.builder().id(1).build();
		Lecture lecture = Lecture.builder()
				.id(1)
				.date(LocalDate.of(2021, 9, 6))
				.time(LectureTime.builder().id(1).start(LocalTime.of(9, 0)).end(LocalTime.of(10, 0)).build())
				.teacher(Teacher.builder().id(1).subjects(Arrays.asList(subject)).build())
				.subject(subject)
				.group(Arrays.asList(group))
				.audience(Audience.builder().capacity(1).build())
				.build();
		when(studentDao.findByGroupId(1)).thenReturn(Arrays.asList(Student.builder().build(), Student.builder().build()));
		Exception exception = assertThrows(LectureInSmallAudienceException.class, () -> {
			lectureService.save(lecture);
		});
		
		assertEquals("Student count more than audience capacity!", exception.getMessage());
	}
	
	@Test
	void givenLectureWithAudienceInUse_whenSave_thenLectureInOccupiedAudienceException() {
		Subject subject = Subject.builder().build();
		Lecture lecture = Lecture.builder()
				.id(1)
				.date(LocalDate.of(2021, 9, 6))
				.time(LectureTime.builder().id(1).start(LocalTime.of(9, 0)).end(LocalTime.of(10, 0)).build())
				.teacher(Teacher.builder().id(1).subjects(Arrays.asList(subject)).build())
				.subject(subject)
				.audience(Audience.builder().capacity(1).build())
				.build();
		Lecture lecture2 = Lecture.builder().id(3).build();
		when(lectureDao.findByAudienceDateAndLectureTime(lecture.getAudience(), lecture.getDate(), lecture.getTime()))
				.thenReturn(Optional.of(lecture2));
		Exception exception = assertThrows(LectureInOccupiedAudienceException.class, () -> {
			lectureService.save(lecture);
		});
		
		assertEquals("This audience is already occupied!", exception.getMessage());
	}

	@Test
	void givenExistingLectureId_whenDelete_thenDeleted() {
		lectureService.deleteById(1);

		verify(lectureDao).deleteById(1);
	}
}
