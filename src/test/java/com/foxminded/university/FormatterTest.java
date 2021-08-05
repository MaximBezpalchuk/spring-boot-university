package com.foxminded.university;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.foxminded.university.model.Audience;
import com.foxminded.university.model.Cathedra;
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
	private Cathedra cathedra = new Cathedra("Fantastic Cathedra");

	@Test
	void formatLectureListTest() {
		List<Lecture> lectures = new ArrayList<>();
		Subject subject = new Subject(cathedra, "TestSubject", "Desc");
		LocalDate date = LocalDate.of(2020, 1, 1);
		LectureTime time = new LectureTime(LocalTime.of(10, 20), LocalTime.of(10, 21));
		Audience audience = new Audience(1, 5);
		Teacher teacher = new Teacher("Amigo", "Bueno", "999", "Puerto Rico", "dot@dot.com", Gender.FEMALE, "123", "none",
				LocalDate.of(1999, 1, 1), cathedra);
		lectures.add(new Lecture(cathedra, subject, date, time, audience, teacher));

		assertEquals("1.  Date: 2020-01-01 | Subject: TestSubject | Audience: 1 | Teacher: Amigo Bueno |",
				formatter.formatLectureList(lectures));
	}

	@Test
	void formatGroupListTest() {
		List<Group> groups = new ArrayList<>();
		Group group = new Group("name", cathedra);
		groups.add(group);
		assertEquals("1.  name", formatter.formatGroupList(groups));
	}

	@Test
	void formatSubjectListTest() {
		List<Subject> subjects = new ArrayList<>();
		Subject subject = new Subject(cathedra, "TestSubject", "Desc");
		subjects.add(subject);
		assertEquals("1.  Name: TestSubject | Description: Desc", formatter.formatSubjectList(subjects));
	}

	@Test
	void formatTeacherListTest() {
		List<Teacher> teachers = new ArrayList<>();
		teachers.add(new Teacher("Amigo", "Bueno", "999", "Puerto Rico", "dot@dot.com", Gender.FEMALE, "123", "none",
				LocalDate.of(1999, 1, 1), cathedra));
		assertEquals("1.  Name: Bueno Amigo", formatter.formatTeacherList(teachers));
	}

	@Test
	void formatAudienceListTest() {
		Audience audience1 = new Audience(3, 5);
		Audience audience2 = new Audience(1, 5);
		List<Audience> audiences = new ArrayList<>();
		audiences.add(audience1);
		audiences.add(audience2);
		assertEquals("1.  Audience: 3 | Capacity: 5" + System.lineSeparator() + "2.  Audience: 1 | Capacity: 5",
				formatter.formatAudienceList(audiences));
	}

	@Test
	void formatStudentListTest() {
		List<Student> students = new ArrayList<>();
		Group group = new Group("name", cathedra);
		Student student = new Student("Amigo", "Bueno", "999", "Puerto Rico", "dot@dot.com", Gender.FEMALE, "123", "none",
				LocalDate.of(1999, 1, 1));
		student.setGroup(group);
		students.add(student);
		assertEquals("1.  Name: Bueno Amigo | Group: name", formatter.formatStudentList(students));
	}

	@Test
	void formatHolidayListTest() {
		List<Holiday> holidays = new ArrayList<>();
		holidays.add(new Holiday("Test", LocalDate.of(1999, 1, 1)));
		assertEquals("1.  Name: Test | Date: 1999-01-01", formatter.formatHolidayList(holidays));
	}

	@Test
	void formatVacationListTest() {
		List<Vacation> vacations = new ArrayList<>();
		vacations.add(new Vacation(LocalDate.of(1999, 1, 1), LocalDate.of(2000, 1, 1), null));
		assertEquals("1.  Dates: 1999-01-01 to 2000-01-01", formatter.formatVacationList(vacations));
	}

	@Test
	void formatLectureTimesListTest() {
		List<LectureTime> lectureTimes = new ArrayList<>();
		lectureTimes.add(new LectureTime(LocalTime.of(10, 20), LocalTime.of(10, 21)));
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
