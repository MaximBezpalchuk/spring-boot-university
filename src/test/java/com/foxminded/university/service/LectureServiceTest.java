package com.foxminded.university.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.foxminded.university.dao.jdbc.JdbcHolidayDao;
import com.foxminded.university.dao.jdbc.JdbcLectureDao;
import com.foxminded.university.dao.jdbc.JdbcStudentDao;
import com.foxminded.university.dao.jdbc.JdbcVacationDao;
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
		Lecture expected = Lecture.builder().id(1).build();
		when(lectureDao.findById(1)).thenReturn(expected);
		Lecture actual = lectureService.findById(1);

		assertEquals(expected, actual);
	}

	@Test
	void givenNewLecture_whenSave_thenSaved() {
		Cathedra cathedra = Cathedra.builder().id(1).name("Fantastic Cathedra").build();
		Teacher teacher = Teacher.builder()
				.id(1)
				.subjects(Arrays.asList(Subject.builder().id(1).cathedra(cathedra).build()))
				.build();
		Lecture lecture = Lecture.builder()
				.id(1)
				.date(LocalDate.of(2021, 9, 8))
				.time(LectureTime.builder()
				.start(LocalTime.of(9, 0))
				.end(LocalTime.of(10, 0)).build())
				.teacher(teacher)
				.subject(Subject.builder().id(1).cathedra(cathedra).build()).build();
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
		Audience audience = Audience.builder().id(1).build();
		LocalDate date = LocalDate.of(2021, 9, 8);
		LectureTime lectureTime = LectureTime.builder().start(LocalTime.of(9, 0)).end(LocalTime.of(10, 0)).build();
		Lecture lecture = Lecture.builder()
				.id(1)
				.date(date)
				.audience(audience)
				.time(lectureTime)
				.teacher(teacher)
				.subject(Subject.builder().id(1).cathedra(cathedra).build()).build();
		when(lectureDao.findByAudienceDateAndLectureTime(audience, date, lectureTime)).thenReturn(lecture);
		lectureService.save(lecture);
		
		verify(lectureDao).save(lecture);
	}
	
	@Test
	void givenLectureInSunday_whenSave_thenNotSaved() {
		Lecture lecture = Lecture.builder()
				.id(1)
				.date(LocalDate.of(2021, 9, 5))
				.build();
		String output = lectureService.save(lecture);
		
		assertEquals("Lecture cant be in sunday", output);
	}
	
	@Test
	void givenLectureInNotSupportedTime_whenSave_thenNotSaved() {
		Lecture lecture = Lecture.builder()
				.id(1)
				.date(LocalDate.of(2021, 9, 6))
				.time(LectureTime.builder().id(1).start(LocalTime.of(7, 0)).end(LocalTime.of(10, 0)).build())
				.build();
		String output = lectureService.save(lecture);
		
		assertEquals("Lecture must start after 8 and end before 22", output);
	}
	
	@Test
	void givenLectureWithBusyTeacher_whenSave_thenNotSaved() {
		Lecture lecture = Lecture.builder()
				.id(1)
				.date(LocalDate.of(2021, 9, 6))
				.time(LectureTime.builder().id(1).start(LocalTime.of(9, 0)).end(LocalTime.of(10, 0)).build())
				.teacher(Teacher.builder().build())
				.build();
		when(lectureDao.findLecturesByTeacher(lecture.getTeacher())).thenReturn(Arrays.asList(lecture));
		String output = lectureService.save(lecture);
		
		assertEquals("Teacher is on other lecture at this time", output);
	}
	
	@Test
	void givenLectureWithTeacherHaveVacation_whenSave_thenNotSaved() {
		Lecture lecture = Lecture.builder()
				.id(1)
				.date(LocalDate.of(2021, 9, 6))
				.time(LectureTime.builder().id(1).start(LocalTime.of(9, 0)).end(LocalTime.of(10, 0)).build())
				.teacher(Teacher.builder().id(1).build())
				.build();
		Vacation vacation = Vacation.builder().start(LocalDate.of(2021, 9, 5)).end(LocalDate.of(2021, 9, 7)).build();
		when(vacationDao.findByTeacherId(lecture.getTeacher().getId())).thenReturn(Arrays.asList(vacation));
		String output = lectureService.save(lecture);
		
		assertEquals("Teacher have vacation at this time", output);
	}
	
	@Test
	void givenLectureInHoliday_whenSave_thenNotSaved() {
		Lecture lecture = Lecture.builder()
				.id(1)
				.date(LocalDate.of(2021, 9, 6))
				.time(LectureTime.builder().id(1).start(LocalTime.of(9, 0)).end(LocalTime.of(10, 0)).build())
				.teacher(Teacher.builder().id(1).build())
				.build();
		when(holidayDao.findAll()).thenReturn(Arrays.asList(Holiday.builder().date(LocalDate.of(2021, 9, 6)).name("Test").build()));
		String output = lectureService.save(lecture);
		
		assertEquals("It is holiday time: Test", output);
	}
	
	@Test
	void givenLectureWithTeacherWhoDidntKnowSubject_whenSave_thenNotSaved() {
		Lecture lecture = Lecture.builder()
				.id(1)
				.date(LocalDate.of(2021, 9, 6))
				.time(LectureTime.builder().id(1).start(LocalTime.of(9, 0)).end(LocalTime.of(10, 0)).build())
				.teacher(Teacher.builder().id(1).build())
				.subject(Subject.builder().build())
				.build();
		String output = lectureService.save(lecture);
		
		assertEquals("Teacher can`t teach this subject", output);
	}
	
	@Test
	void givenLectureWithMoreStudentsThenAudienceCapacity_whenSave_thenNotSaved() {
		Subject subject = Subject.builder().build();
		Group group = Group.builder().build();
		Lecture lecture = Lecture.builder()
				.id(1)
				.date(LocalDate.of(2021, 9, 6))
				.time(LectureTime.builder().id(1).start(LocalTime.of(9, 0)).end(LocalTime.of(10, 0)).build())
				.teacher(Teacher.builder().id(1).subjects(Arrays.asList(subject)).build())
				.subject(subject)
				.group(Arrays.asList(group))
				.audience(Audience.builder().capacity(1).build())
				.build();
		when(studentDao.findAll()).thenReturn(Arrays.asList(Student.builder().group(group).build(), Student.builder().group(group).build()));
		String output = lectureService.save(lecture);
		
		assertEquals("Audience have less capacity then student count", output);
	}
	
	@Test
	void givenLectureWithAudienceInUse_whenSave_thenNotSaved() {
		Subject subject = Subject.builder().build();
		Lecture lecture = Lecture.builder()
				.id(1)
				.date(LocalDate.of(2021, 9, 6))
				.time(LectureTime.builder().id(1).start(LocalTime.of(9, 0)).end(LocalTime.of(10, 0)).build())
				.teacher(Teacher.builder().id(1).subjects(Arrays.asList(subject)).build())
				.subject(subject)
				.audience(Audience.builder().capacity(1).build())
				.build();
		when(lectureDao.findByAudienceAndDate(lecture.getAudience(), lecture.getDate())).thenReturn(Arrays.asList(lecture));
		String output = lectureService.save(lecture);
		
		assertEquals("Another lecture on this time already exists", output);
	}

	@Test
	void givenExistingLectureId_whenDelete_thenDeleted() {
		lectureService.deleteById(1);

		verify(lectureDao).deleteById(1);
	}
}
