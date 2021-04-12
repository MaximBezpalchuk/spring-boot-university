package com.foxminded.university;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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

public class DataCreator {

	private Cathedra cathedra = new Cathedra();
	private List<Student> students = new ArrayList<>();
	private List<Teacher> teachers = new ArrayList<>();
	private List<Group> groups = new ArrayList<>();
	private List<Subject> subjects = new ArrayList<>();
	private List<Lecture> lectures = new ArrayList<>();
	private List<Audience> audiences = new ArrayList<>();
	private List<Holiday> holidays = new ArrayList<>();
	private List<LectureTime> lectureTimes = new ArrayList<>();

	public Cathedra createData() {
		cathedra.setGroups(groups);
		cathedra.setLectures(lectures);
		cathedra.setTeachers(teachers);
		cathedra.setHolidays(holidays);
		cathedra.setSubjects(subjects);
		cathedra.setAudiences(audiences);
		cathedra.setLectureTimes(lectureTimes);
		createStudents();
		createGroups();
		createTeachers();
		createSubjects();
		assertSubjectsToTeachers();
		createAudiences();
		createLectureTimes();
		createLectures();
		assertGroupsToLectures();
		createHolidays();
		return cathedra;
	}

	private void createStudents() {
		students.add(new Student("Petr", "Orlov", "888005353535", "Empty Street 8", "1@owl.com", Gender.MALE, "999",
				"General secondary education", LocalDate.of(1994, 3, 3)));
		students.add(new Student("Oleg", "Krasnov", "2247582", "Empty Street 8-2", "2@owl.com", Gender.MALE, "999",
				"General secondary education", LocalDate.of(1994, 5, 13)));
		students.add(new Student("Margot", "Robbie", "9999999999", "Holywood Street 1", "3@owl.com", Gender.FEMALE, "254826",
				"General tecnical education", LocalDate.of(1990, 2, 7)));
		students.add(new Student("Kim", "Cattrall", "(312)-555-0690", "Virtual Reality Capsule no 2", "4@owl.com",
				Gender.FEMALE, "12345", "College education", LocalDate.of(1956, 8, 21)));
		students.add(new Student("Thomas", "Anderson", "(312)-555-5555", "Virtual Reality Capsule no 3", "5@owl.com",
				Gender.MALE, "12345", "College education", LocalDate.of(1962, 3, 11)));
	}

	private void createGroups() {
		Group group1 = new Group("Killers", cathedra);
		List<Student> students1 = new ArrayList<>();
		students1.add(students.get(0));
		students.get(0).setGroup(group1);
		students1.add(students.get(1));
		students.get(1).setGroup(group1);
		students1.add(students.get(2));
		students.get(2).setGroup(group1);
		group1.setStudents(students1);
		groups.add(group1);
		Group group2 = new Group("Mages", cathedra);
		List<Student> students2 = new ArrayList<>();
		students2.add(students.get(3));
		students.get(3).setGroup(group2);
		students2.add(students.get(4));
		students.get(4).setGroup(group2);
		group2.setStudents(students2);
		groups.add(group2);
	}

	private void createTeachers() {
		Teacher first = new Teacher("Daniel", "Morpheus", "1", "Virtual Reality Capsule no 1", "1@bigowl.com", Gender.MALE,
				"12345", "Higher education", LocalDate.of(1970, 01, 01), cathedra);
		first.setDegree(Degree.PROFESSOR);
		teachers.add(first);
		Teacher second = new Teacher("Bane", "Smith", "1", "Virtual Reality", "0@bigowl.com", Gender.MALE, "none", "none",
				LocalDate.of(1970, 01, 01), cathedra);
		second.setDegree(Degree.PROFESSOR);
		teachers.add(second);
		assertVacations();
	}

	private void createSubjects() {
		subjects.add(new Subject("Weapon Tactics", "Learning how to use heavy weapon and guerrilla tactics"));
		subjects.add(new Subject("Wandless Magic", "Learning how to use spells without magic wand"));
		subjects.add(
				new Subject("Universal language", "Learning the universal language established by the Federation"));
	}

	private void assertSubjectsToTeachers() {
		List<Subject> subjectsT1 = new ArrayList<>();
		subjectsT1.add(subjects.get(0));
		teachers.get(0).setSubjects(subjectsT1);
		List<Subject> subjectsT2 = new ArrayList<>();
		subjectsT2.add(subjects.get(1));
		subjectsT2.add(subjects.get(2));
		teachers.get(1).setSubjects(subjectsT2);
		updateTeachersInSubjects();
	}

	private void updateTeachersInSubjects() {
		List<Teacher> teacherS1 = teachers.stream().filter(teacher -> teacher.getSubjects().contains(subjects.get(0)))
				.collect(Collectors.toList());
		subjects.get(0).setTeachers(teacherS1);
		List<Teacher> teacherS2 = teachers.stream().filter(teacher -> teacher.getSubjects().contains(subjects.get(1)))
				.collect(Collectors.toList());
		subjects.get(1).setTeachers(teacherS2);
		List<Teacher> teacherS3 = teachers.stream().filter(teacher -> teacher.getSubjects().contains(subjects.get(2)))
				.collect(Collectors.toList());
		subjects.get(2).setTeachers(teacherS3);
	}

	private void assertVacations() {
		List<Vacation> vacationsT1 = new ArrayList<>();
		vacationsT1.add(new Vacation(LocalDate.of(2021, 1, 15), LocalDate.of(2021, 1, 29)));
		vacationsT1.add(new Vacation(LocalDate.of(2021, 6, 15), LocalDate.of(2021, 6, 29)));
		teachers.get(0).setVacations(vacationsT1);
		List<Vacation> vacationsT2 = new ArrayList<>();
		vacationsT2.add(new Vacation(LocalDate.of(2021, 3, 15), LocalDate.of(2021, 3, 29)));
		vacationsT2.add(new Vacation(LocalDate.of(2021, 7, 15), LocalDate.of(2021, 7, 29)));
		teachers.get(1).setVacations(vacationsT2);
	}

	public void createLectureTimes() {
		lectureTimes.add(new LectureTime(LocalTime.of(8, 0), LocalTime.of(9, 30)));
		lectureTimes.add(new LectureTime(LocalTime.of(9, 40), LocalTime.of(11, 10)));
		lectureTimes.add(new LectureTime(LocalTime.of(11, 20), LocalTime.of(12, 50)));
		lectureTimes.add(new LectureTime(LocalTime.of(13, 20), LocalTime.of(14, 50)));
		lectureTimes.add(new LectureTime(LocalTime.of(15, 00), LocalTime.of(16, 30)));
		lectureTimes.add(new LectureTime(LocalTime.of(16, 40), LocalTime.of(18, 10)));
		lectureTimes.add(new LectureTime(LocalTime.of(18, 20), LocalTime.of(19, 50)));
		lectureTimes.add(new LectureTime(LocalTime.of(20, 00), LocalTime.of(21, 30)));
	}

	private void createAudiences() {
		audiences.add(new Audience(1, 10));
		audiences.add(new Audience(2, 30));
		audiences.add(new Audience(3, 10));
	}

	private void createLectures() {
		// Monday - wt and ul - only killers
		lectures.add(new Lecture(subjects.get(0), LocalDate.of(2021, 4, 4), lectureTimes.get(0), audiences.get(0),
				teachers.get(0)));
		lectures.add(new Lecture(subjects.get(0), LocalDate.of(2021, 4, 4), lectureTimes.get(1), audiences.get(0),
				teachers.get(0)));
		lectures.add(new Lecture(subjects.get(2), LocalDate.of(2021, 4, 4), lectureTimes.get(2), audiences.get(2),
				teachers.get(1)));
		lectures.add(new Lecture(subjects.get(2), LocalDate.of(2021, 4, 4), lectureTimes.get(3), audiences.get(2),
				teachers.get(1)));
		// Wednesday - wm - only mages
		lectures.add(new Lecture(subjects.get(1), LocalDate.of(2021, 4, 6), lectureTimes.get(1), audiences.get(1),
				teachers.get(1)));
		lectures.add(new Lecture(subjects.get(1), LocalDate.of(2021, 4, 6), lectureTimes.get(2), audiences.get(1),
				teachers.get(1)));
		lectures.add(new Lecture(subjects.get(1), LocalDate.of(2021, 4, 6), lectureTimes.get(3), audiences.get(1),
				teachers.get(1)));
		// Friday - ul for mages, wt for killers
		lectures.add(new Lecture(subjects.get(2), LocalDate.of(2021, 4, 8), lectureTimes.get(1), audiences.get(2),
				teachers.get(1)));
		lectures.add(new Lecture(subjects.get(2), LocalDate.of(2021, 4, 8), lectureTimes.get(2), audiences.get(2),
				teachers.get(1)));
		lectures.add(new Lecture(subjects.get(0), LocalDate.of(2021, 4, 8), lectureTimes.get(3), audiences.get(0),
				teachers.get(0)));
		lectures.add(new Lecture(subjects.get(0), LocalDate.of(2021, 4, 8), lectureTimes.get(4), audiences.get(0),
				teachers.get(0)));
	}

	private void assertGroupsToLectures() {
		List<Group> wtGroups = new ArrayList<>();
		wtGroups.add(groups.get(0));
		lectures.get(0).setGroups(wtGroups);
		lectures.get(1).setGroups(wtGroups);
		lectures.get(9).setGroups(wtGroups);
		lectures.get(10).setGroups(wtGroups);
		List<Group> ulGroups = new ArrayList<>();
		ulGroups.add(groups.get(0));
		ulGroups.add(groups.get(1));
		lectures.get(2).setGroups(ulGroups);
		lectures.get(3).setGroups(ulGroups);
		lectures.get(7).setGroups(ulGroups);
		lectures.get(8).setGroups(ulGroups);
		List<Group> wmGroups = new ArrayList<>();
		wmGroups.add(groups.get(1));
		lectures.get(4).setGroups(wmGroups);
		lectures.get(5).setGroups(wmGroups);
		lectures.get(6).setGroups(wmGroups);
	}

	private void createHolidays() {
		holidays.add(new Holiday("Christmas", LocalDate.of(2021, 12, 25)));
		holidays.add(new Holiday("Thanksgiving", LocalDate.of(2021, 11, 22)));
		holidays.add(new Holiday("Decoration Day", LocalDate.of(2021, 5, 31)));
		holidays.add(new Holiday("Independence Day", LocalDate.of(2021, 7, 4)));
		holidays.add(new Holiday("Labor Day", LocalDate.of(2021, 9, 6)));
		holidays.add(new Holiday("New Year", LocalDate.of(2021, 1, 1)));
	}
}
