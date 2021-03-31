package com.foxminded.university;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.foxminded.university.model.Cathedra;
import com.foxminded.university.model.Group;
import com.foxminded.university.model.Student;
import com.foxminded.university.model.Subject;
import com.foxminded.university.model.Teacher;
import com.foxminded.university.model.Vacation;

public class DataCreator {

	private Cathedra cathedra;
	private List<Student> students;
	private List<Teacher> teachers;
	private List<Group> groups;
	private List<Subject> subjects;
	
	public void createData() {
		Cathedra cathedra = new Cathedra();
		createStudents();
		createGroups();
		createTeachers();
		createSubjects();
		assertSubjectsToTeachers();
		
	}

	//TODO: make cathedra into constructors
	private void createStudents() {
		Student student1 = new Student("Petr", "Orlov", "888005353535", "Empty Street 8", "male", "999",
				"General secondary education", LocalDate.of(1994, 12, 03));
		Student student2 = new Student("Oleg", "Krasnov", "2247582", "Empty Street 8-2", "male", "999",
				"General secondary education", LocalDate.of(1994, 5, 13));
		Student student3 = new Student("Margot", "Robbie", "9999999999", "Holywood Street 1", "female", "254826",
				"General tecnical education", LocalDate.of(1990, 07, 2));
		Student student4 = new Student("Kim", "Cattrall", "(312)-555-0690", "Virtual Reality Capsule no 2", "female",
				"12345", "College education", LocalDate.of(1956, 21, 8));
		Student student5 = new Student("Thomas", "Anderson", "(312)-555-5555", "Virtual Reality Capsule no 3", "male",
				"12345", "College education", LocalDate.of(1962, 11, 3));
		students = new ArrayList<>();
		students.add(student1);
		students.add(student2);
		students.add(student3);
		students.add(student4);
		students.add(student5);
	}
	
	private void createGroups() {
		Group group1 = new Group("First group");
		group1.setCathedra(cathedra);
		List<Student> students1 = new ArrayList<>();
		students1.add(students.get(0));
		students1.add(students.get(1));
		students1.add(students.get(2));
		group1.setStudents(students1);
		Group group2 = new Group("Second group");
		group2.setCathedra(cathedra);
		List<Student> students2 = new ArrayList<>();
		students2.add(students.get(3));
		students2.add(students.get(4));
		group2.setStudents(students2);
	}

	private void createTeachers() {
		Teacher teacher1 = new Teacher("Daniel", "Morpheus", "1", "Virtual Reality Capsule no 1", "male", "12345",
				"Higher education", LocalDate.of(1970, 01, 01));
		teacher1.setCathedra(cathedra);
		Teacher teacher2 = new Teacher("Bane", "Smith", "1", "Virtual Reality", "male", "none", "none",
				LocalDate.of(1970, 01, 01));
		teacher2.setCathedra(cathedra);
		teachers = new ArrayList<>();
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
		teacherS2.add(teachers.get(1));
		teacherS2.add(teachers.get(2));
		subjects.get(1).setTeachers(teacherS2);
	}
	
	
	private void assertVacations() {
		Vacation vacation1T1 = new Vacation("First", LocalDate.of(2021, 1, 15), LocalDate.of(2021, 1, 29));
		Vacation vacation2T1 = new Vacation("Second", LocalDate.of(2021, 6, 15), LocalDate.of(2021, 6, 29));
		List<Vacation> vacationsT1 = new ArrayList<>();
		vacationsT1.add(vacation1T1);
		vacationsT1.add(vacation2T1);
		teachers.get(0).setVacations(vacationsT1);
		
		Vacation vacation1T2 = new Vacation("First", LocalDate.of(2021, 2, 15), LocalDate.of(2021, 2, 29));
		Vacation vacation2T2 = new Vacation("Second", LocalDate.of(2021, 7, 15), LocalDate.of(2021, 7, 29));
		List<Vacation> vacationsT2 = new ArrayList<>();
		vacationsT2.add(vacation1T2);
		vacationsT2.add(vacation2T2);
		teachers.get(1).setVacations(vacationsT2);
	}

}
