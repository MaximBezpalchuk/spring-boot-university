package com.foxminded.university;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import com.foxminded.university.model.Audience;
import com.foxminded.university.model.Group;
import com.foxminded.university.model.Holiday;
import com.foxminded.university.model.Lecture;
import com.foxminded.university.model.Student;
import com.foxminded.university.model.Subject;
import com.foxminded.university.model.Teacher;
import com.foxminded.university.model.Vacation;

public class Formatter {

	//.sorted((d1, d2) -> d1.getDate().compareTo(d2.getDate()))
	public String formatLectureList(List<Lecture> lectures) {
		AtomicInteger index = new AtomicInteger();
		return lectures.stream()
				.map(lecture -> String.format("%-3s Date: %10s | Subject: %-18s | Audience: %d | Teacher: %-15s",
						index.incrementAndGet() + ".", lecture.getDate(), lecture.getSubject().getName(),
						lecture.getAudience().getRoom(),
						lecture.getTeacher().getFirstName() + " " + lecture.getTeacher().getLastName()))
				.collect(Collectors.joining(System.lineSeparator()));
	}

	public String formatGroupList(List<Group> groups) {
		AtomicInteger index = new AtomicInteger();
		return groups.stream().map(group -> String.format("%d. %s", index.incrementAndGet(), group.getName()))
				.collect(Collectors.joining(System.lineSeparator()));
	}

	public String formatSubjectList(List<Subject> subjects) {
		AtomicInteger index = new AtomicInteger();
		return subjects.stream()
				.map(subject -> String.format("%d. Name: %-7s | " + "Description: %s", index.incrementAndGet(),
						subject.getName(), subject.getDescription()))
				.collect(Collectors.joining(System.lineSeparator()));
	}

	public String formatTeacherList(List<Teacher> teachers) {
		AtomicInteger index = new AtomicInteger();
		return teachers.stream()
				.map(teacher -> String.format("%d. Name: %-15s", index.incrementAndGet(),
						teacher.getFirstName() + " " + teacher.getLastName()))
				.collect(Collectors.joining(System.lineSeparator()));
	}

	public String formatAudienceList(List<Audience> audiences) {
		AtomicInteger index = new AtomicInteger();
		return audiences.stream()
				.map(audience -> String.format("%d. Audience: %-5d| Capacity: %d", index.incrementAndGet(),
						audience.getRoom(), audience.getCapacity()))
				.collect(Collectors.joining(System.lineSeparator()));
	}

	public String formatStudentList(List<Student> students) {
		AtomicInteger index = new AtomicInteger();
		return students.stream()
				.map(student -> String.format("%d. Name: %-18s| Group: %s", index.incrementAndGet(),
						student.getFirstName() + " " + student.getLastName(), student.getGroup().getName()))
				.collect(Collectors.joining(System.lineSeparator()));
	}

	public String formatHolidayList(List<Holiday> holidays) {
		AtomicInteger index = new AtomicInteger();
		return holidays.stream()
				.map(holiday -> String.format("%d. Name: %-18s| Date: %s", index.incrementAndGet(),
						holiday.getDescription(), holiday.getDate()))
				.collect(Collectors.joining(System.lineSeparator()));
	}

	public String formatVacationList(List<Vacation> vacations) {
		AtomicInteger index = new AtomicInteger();
		return vacations.stream()
				.map(vacation -> String.format("%d. Description: %-18s| Dates: %s to %s", index.incrementAndGet(),
						vacation.getDescription(), vacation.getStart(), vacation.getEnd()))
				.collect(Collectors.joining(System.lineSeparator()));
	}
}
