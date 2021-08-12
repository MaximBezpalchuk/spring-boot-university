package com.foxminded.university;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.foxminded.university.model.Audience;
import com.foxminded.university.model.Cathedra;
import com.foxminded.university.model.Degree;
import com.foxminded.university.model.Gender;
import com.foxminded.university.model.Group;
import com.foxminded.university.model.Holiday;
import com.foxminded.university.model.Lecture;
import com.foxminded.university.model.LectureTime;
import com.foxminded.university.model.Student;
import com.foxminded.university.model.Subject;
import com.foxminded.university.model.Teacher;
import com.foxminded.university.model.Vacation;

class FormatterTest {
	private Formatter formatter = new Formatter();
	private Cathedra cathedra = new Cathedra.Builder("Fantastic Cathedra").build();

	@Test
	void formatLectureListTest() {
		List<Lecture> lectures = new ArrayList<>();
		Subject subject = Subject.build(cathedra, "TestSubject", "Desc").build();
		LocalDate date = LocalDate.of(2020, 1, 1);
		LectureTime time = LectureTime.build(LocalTime.of(10, 20), LocalTime.of(10, 21)).build();
		Audience audience = Audience.build(1, 5, cathedra).build();
		Teacher teacher = Teacher
				.build("Amigo", "Bueno", "Puerto Rico", Gender.FEMALE, LocalDate.of(1999, 1, 1), cathedra,
						Degree.PROFESSOR)
				.setPhone("999").setEmail("dot@dot.com").setPostalCode("123").setEducation("none").build();

		lectures.add(Lecture.build(cathedra, subject, date, time, audience, teacher).build());

		assertEquals(
				"1.  Date: 2020-01-01 | Subject: TestSubject | Audience: 1 | Teacher: Amigo Bueno | Lecture start: 10:20, Lecture end: 10:21",
				formatter.formatLectureList(lectures));
	}

	@Test
	void formatGroupListTest() {
		List<Group> groups = new ArrayList<>();
		Group group = new Group.Builder("name", cathedra).build();
		groups.add(group);
		assertEquals("1.  name", formatter.formatGroupList(groups));
	}

	@Test
	void formatSubjectListTest() {
		List<Subject> subjects = new ArrayList<>();
		Subject subject = Subject.build(cathedra, "TestSubject", "Desc").build();
		subjects.add(subject);
		assertEquals("1.  Name: TestSubject | Description: Desc", formatter.formatSubjectList(subjects));
	}

	@Test
	void formatTeacherListTest() {
		List<Teacher> teachers = new ArrayList<>();
		teachers.add(Teacher
				.build("Amigo", "Bueno", "Puerto Rico", Gender.FEMALE, LocalDate.of(1999, 1, 1), cathedra,
						Degree.PROFESSOR)
				.setPhone("999").setEmail("dot@dot.com").setPostalCode("123").setEducation("none").build());
		assertEquals("1.  Name: Bueno Amigo", formatter.formatTeacherList(teachers));
	}

	@Test
	void formatAudienceListTest() {
		Audience audience1 = Audience.build(3, 5, cathedra).build();
		Audience audience2 = Audience.build(1, 5, cathedra).build();
		List<Audience> audiences = new ArrayList<>();
		audiences.add(audience1);
		audiences.add(audience2);
		assertEquals("1.  Audience: 3 | Capacity: 5" + System.lineSeparator() + "2.  Audience: 1 | Capacity: 5",
				formatter.formatAudienceList(audiences));
	}

	@Test
	void formatStudentListTest() {
		List<Student> students = new ArrayList<>();
		Group group = new Group.Builder("name", cathedra).build();

		Student student = Student.build("Amigo", "Bueno", "Puerto Rico", Gender.FEMALE, LocalDate.of(1999, 1, 1))
				.setPhone("999").setEmail("dot@dot.com").setPostalCode("123").setEducation("none").setGroup(group)
				.build();
		students.add(student);
		assertEquals("1.  Name: Bueno Amigo | Group: name", formatter.formatStudentList(students));
	}

	@Test
	void formatHolidayListTest() {
		List<Holiday> holidays = new ArrayList<>();
		holidays.add(Holiday.build("Test", LocalDate.of(1999, 1, 1), cathedra).build());
		assertEquals("1.  Name: Test | Date: 1999-01-01", formatter.formatHolidayList(holidays));
	}

	@Test
	void formatVacationListTest() {
		List<Vacation> vacations = new ArrayList<>();
		vacations.add(Vacation.build(LocalDate.of(1999, 1, 1), LocalDate.of(2000, 1, 1), null).build());
		assertEquals("1.  Dates: 1999-01-01 to 2000-01-01", formatter.formatVacationList(vacations));
	}

	@Test
	void formatLectureTimesListTest() {
		List<LectureTime> lectureTimes = new ArrayList<>();
		lectureTimes.add(LectureTime.build(LocalTime.of(10, 20), LocalTime.of(10, 21)).build());
		assertEquals("1.  Time: 10:20 to 10:21", formatter.formatLectureTimesList(lectureTimes));
	}

	@Test
	void getGenderStringTest() {
		assertEquals("MALE FEMALE ", formatter.getGenderString());
	}

	@Test
	void getDegreeStringTest() {
		assertEquals("ASSISTANT PROFESSOR UNKNOWN ", formatter.getDegreeString());
	}
}
