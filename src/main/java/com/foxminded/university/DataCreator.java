package com.foxminded.university;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

public class DataCreator {

	private Cathedra cathedra = new Cathedra();
	private List<Student> students = new ArrayList<>();
	private List<Teacher> teachers = new ArrayList<>();
	private List<Group> groups = new ArrayList<>();
	private List<Subject> subjects = new ArrayList<>();
	private Map<String, Lecture> lectures = new HashMap<>();
	private List<Audience> audiences = new ArrayList<>();
	private List<Holiday> holidays = new ArrayList<>();
	
	public Cathedra createData() {
		cathedra.setGroups(groups);
		cathedra.setLectures(lectures);
		cathedra.setTeachers(teachers);
		cathedra.setHolidays(holidays);
		createStudents();
		createGroups();
		createTeachers();
		createSubjects();
		assertSubjectsToTeachers();
		createAudiences();
		createLectures();
		assertGroupsToLectures();
		createHolidays();
		return cathedra;
	}

	private void createStudents() {
		Student student1 = new Student("Petr", "Orlov", "888005353535", "Empty Street 8", "1@owl.com", "male", "999",
				"General secondary education", LocalDate.of(1994, 3, 3));
		Student student2 = new Student("Oleg", "Krasnov", "2247582", "Empty Street 8-2", "2@owl.com", "male", "999",
				"General secondary education", LocalDate.of(1994, 5, 13));
		Student student3 = new Student("Margot", "Robbie", "9999999999", "Holywood Street 1", "3@owl.com", "female", "254826",
				"General tecnical education", LocalDate.of(1990, 2, 7));
		Student student4 = new Student("Kim", "Cattrall", "(312)-555-0690", "Virtual Reality Capsule no 2", "4@owl.com", "female",
				"12345", "College education", LocalDate.of(1956, 8, 21));
		Student student5 = new Student("Thomas", "Anderson", "(312)-555-5555", "Virtual Reality Capsule no 3", "5@owl.com", "male",
				"12345", "College education", LocalDate.of(1962, 3, 11));
		students.add(student1);
		students.add(student2);
		students.add(student3);
		students.add(student4);
		students.add(student5);
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
		Group group2 = new Group("Mages", cathedra);
		List<Student> students2 = new ArrayList<>();
		students2.add(students.get(3));
		students.get(3).setGroup(group2);
		students2.add(students.get(4));
		students.get(4).setGroup(group2);
		group2.setStudents(students2);
		groups.add(group1);
		groups.add(group2);
	}

	private void createTeachers() {
		Teacher teacher1 = new Teacher("Daniel", "Morpheus", "1", "Virtual Reality Capsule no 1", "1@bigowl.com", "male", "12345",
				"Higher education", LocalDate.of(1970, 01, 01), cathedra);
		Teacher teacher2 = new Teacher("Bane", "Smith", "1", "Virtual Reality", "0@bigowl.com", "male", "none", "none",
				LocalDate.of(1970, 01, 01), cathedra);
		teachers.add(teacher1);
		teachers.add(teacher2);
		assertVacations();
	}

	private void createSubjects() {
		Subject subject1 = new Subject("Weapon Tactics", "Learning how to use heavy weapon and guerrilla tactics");
		Subject subject2 = new Subject("Wandless Magic", "Learning how to use spells without magic wand");
		Subject subject3 = new Subject("Universal language",
				"Learning the universal language established by the Federation");
		subjects.add(subject1);
		subjects.add(subject2);
		subjects.add(subject3);
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
		List<Teacher> teacherS1 = new ArrayList<>();
		teacherS1.add(teachers.get(0));
		subjects.get(0).setTeachers(teacherS1);
		List<Teacher> teacherS2 = new ArrayList<>();
		teacherS2.add(teachers.get(0));
		teacherS2.add(teachers.get(1));
		subjects.get(1).setTeachers(teacherS2);
	}

	private void assertVacations() {
		Vacation vacation1T1 = new Vacation("First", LocalDate.of(2021, 1, 15), LocalDate.of(2021, 1, 29));
		Vacation vacation2T1 = new Vacation("Second", LocalDate.of(2021, 6, 15), LocalDate.of(2021, 6, 29));
		List<Vacation> vacationsT1 = new ArrayList<>();
		vacationsT1.add(vacation1T1);
		vacationsT1.add(vacation2T1);
		teachers.get(0).setVacations(vacationsT1);

		Vacation vacation1T2 = new Vacation("First", LocalDate.of(2021, 3, 15), LocalDate.of(2021, 3, 29));
		Vacation vacation2T2 = new Vacation("Second", LocalDate.of(2021, 7, 15), LocalDate.of(2021, 7, 29));
		List<Vacation> vacationsT2 = new ArrayList<>();
		vacationsT2.add(vacation1T2);
		vacationsT2.add(vacation2T2);
		teachers.get(1).setVacations(vacationsT2);
	}

	private LectureTime createLectureTime(int lessonNumber) {
		LectureTime time = new LectureTime();
		switch (lessonNumber) {
		case 1:
			time.setStart(LocalTime.of(8, 0));
			time.setEnd(LocalTime.of(9, 30));
			break;

		case 2:
			time.setStart(LocalTime.of(9, 40));
			time.setEnd(LocalTime.of(11, 10));
			break;

		case 3:
			time.setStart(LocalTime.of(11, 20));
			time.setEnd(LocalTime.of(12, 50));
			break;

		case 4:
			time.setStart(LocalTime.of(13, 20));
			time.setEnd(LocalTime.of(14, 50));
			break;

		case 5:
			time.setStart(LocalTime.of(15, 00));
			time.setEnd(LocalTime.of(16, 30));
			break;

		case 6:
			time.setStart(LocalTime.of(16, 40));
			time.setEnd(LocalTime.of(18, 10));
			break;

		case 7:
			time.setStart(LocalTime.of(18, 20));
			time.setEnd(LocalTime.of(19, 50));
			break;

		case 8:
			time.setStart(LocalTime.of(20, 00));
			time.setEnd(LocalTime.of(21, 30));
			break;
		}

		return time;
	}

	private void createAudiences() {
		Audience room1 = new Audience(1, 10);
		Audience room2 = new Audience(2, 30);
		Audience room3 = new Audience(3, 10);
		audiences.add(room1);
		audiences.add(room2);
		audiences.add(room3);
	}
	
	private void createLectures() {
		//Monday - wt and ul - only killers
		Lecture wt1 = new Lecture(subjects.get(0), LocalDate.of(2021, 4, 4), createLectureTime(1), audiences.get(0), teachers.get(0));
		Lecture wt2 = new Lecture(subjects.get(0), LocalDate.of(2021, 4, 4), createLectureTime(2), audiences.get(0), teachers.get(0));
		Lecture ul1 = new Lecture(subjects.get(2), LocalDate.of(2021, 4, 4), createLectureTime(3), audiences.get(2), teachers.get(1));
		Lecture ul2 = new Lecture(subjects.get(2), LocalDate.of(2021, 4, 4), createLectureTime(4), audiences.get(2), teachers.get(1));
		//Wednesday - wm - only mages
		Lecture wm1 = new Lecture(subjects.get(1), LocalDate.of(2021, 4, 6), createLectureTime(2), audiences.get(1), teachers.get(1));
		Lecture wm2 = new Lecture(subjects.get(1), LocalDate.of(2021, 4, 6), createLectureTime(3), audiences.get(1), teachers.get(1));
		Lecture wm3 = new Lecture(subjects.get(1), LocalDate.of(2021, 4, 6), createLectureTime(4), audiences.get(1), teachers.get(1));
		//Friday - ul for mages, wt for killers
		Lecture ul3 = new Lecture(subjects.get(2), LocalDate.of(2021, 4, 8), createLectureTime(2), audiences.get(2), teachers.get(1));
		Lecture ul4 = new Lecture(subjects.get(2), LocalDate.of(2021, 4, 8), createLectureTime(3), audiences.get(2), teachers.get(1));
		Lecture wt3 = new Lecture(subjects.get(0), LocalDate.of(2021, 4, 8), createLectureTime(4), audiences.get(0), teachers.get(0));
		Lecture wt4 = new Lecture(subjects.get(0), LocalDate.of(2021, 4, 8), createLectureTime(5), audiences.get(0), teachers.get(0));
		
		lectures.put("wt1", wt1);
		lectures.put("wt2",wt2);
		lectures.put("ul1",ul1);
		lectures.put("ul2",ul2);
		lectures.put("wm1",wm1);
		lectures.put("wm2",wm2);
		lectures.put("wm3",wm3);
		lectures.put("ul3",ul3);
		lectures.put("ul4",ul4);
		lectures.put("wt3",wt3);
		lectures.put("wt4",wt4);
	}
	
	private void assertGroupsToLectures() {
		List<Group> wtGroups = new ArrayList<>();
		wtGroups.add(groups.get(0));
		lectures.get("wt1").setGroups(wtGroups);
		lectures.get("wt2").setGroups(wtGroups);
		lectures.get("wt3").setGroups(wtGroups);
		lectures.get("wt4").setGroups(wtGroups);
		List<Group> ulGroups = new ArrayList<>();
		ulGroups.add(groups.get(0));
		ulGroups.add(groups.get(1));
		lectures.get("ul1").setGroups(ulGroups);
		lectures.get("ul2").setGroups(ulGroups);
		lectures.get("ul3").setGroups(ulGroups);
		lectures.get("ul4").setGroups(ulGroups);
		List<Group> wmGroups = new ArrayList<>();
		wmGroups.add(groups.get(1));
		lectures.get("wm1").setGroups(wmGroups);
		lectures.get("wm2").setGroups(wmGroups);
		lectures.get("wm3").setGroups(wmGroups);
		assertLecturesToGroups();
	}
	
	private void assertLecturesToGroups() {
		List<Lecture> killersLectures = new ArrayList<>();
		killersLectures.add(lectures.get("wt1"));
		killersLectures.add(lectures.get("wt2"));
		killersLectures.add(lectures.get("wt3"));
		killersLectures.add(lectures.get("wt4"));
		killersLectures.add(lectures.get("ul1"));
		killersLectures.add(lectures.get("ul2"));
		groups.get(0).setLectures(killersLectures);
		List<Lecture> magesLectures = new ArrayList<>();
		magesLectures.add(lectures.get("wm1"));
		magesLectures.add(lectures.get("wm2"));
		magesLectures.add(lectures.get("wm3"));
		magesLectures.add(lectures.get("ul3"));
		magesLectures.add(lectures.get("ul4"));
		groups.get(1).setLectures(magesLectures);
	}
	
	private void createHolidays() {
		Holiday christmas = new Holiday("Christmas", LocalDate.of(2021, 12, 25));
		Holiday thanksgiving = new Holiday("Thanksgiving", LocalDate.of(2021, 11, 22));
		Holiday decorationDay = new Holiday("Decoration Day", LocalDate.of(2021, 5, 31));
		Holiday independenceDay = new Holiday("Independence Day", LocalDate.of(2021, 7, 4));
		Holiday laborDay = new Holiday("Labor Day", LocalDate.of(2021, 9, 6));
		Holiday newYear = new Holiday("New Year", LocalDate.of(2021, 1, 1));
		holidays.add(christmas);
		holidays.add(thanksgiving);
		holidays.add(decorationDay);
		holidays.add(independenceDay);
		holidays.add(laborDay);
		holidays.add(newYear);
	}
}
