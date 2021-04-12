package com.foxminded.university;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.foxminded.university.model.Audience;
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

public class Formatter {

	private <T> int getMaxFieldLength(List<T> list, Function<T, String> getter) {
		return list.stream().map(getter).mapToInt(String::length).max().orElse(0);
	}

	public String formatLectureList(List<Lecture> lectures) {
		AtomicInteger index = new AtomicInteger();
		int subjectNameFieldLength = getMaxFieldLength(lectures, lecture -> lecture.getSubject().getName());
		int lectureAudienceFieldLength = getMaxFieldLength(lectures,
				lecture -> String.valueOf(lecture.getAudience().getRoom()));
		int teacherNameFieldLength = getMaxFieldLength(lectures, lecture -> lecture.getTeacher().getFirstName())
				+ getMaxFieldLength(lectures, lecture -> lecture.getTeacher().getLastName()) + 1;

		return lectures.stream()
				.map(lecture -> String.format(
						"%-3s Date: %s | Subject: %-" + subjectNameFieldLength + "s | Audience: %"
								+ lectureAudienceFieldLength + "d | Teacher: %-" + teacherNameFieldLength + "s |",
						index.incrementAndGet() + ".", lecture.getDate(), lecture.getSubject().getName(),
						lecture.getAudience().getRoom(),
						lecture.getTeacher().getFirstName() + " " + lecture.getTeacher().getLastName()))
				.collect(Collectors.joining(System.lineSeparator()));
	}

	public String formatGroupList(List<Group> groups) {
		AtomicInteger index = new AtomicInteger();
		int nameFieldLength = getMaxFieldLength(groups, Group::getName);

		return groups.stream().map(group -> String.format("%-3s %-" + nameFieldLength + "s",
				index.incrementAndGet() + ".", group.getName())).collect(Collectors.joining(System.lineSeparator()));
	}

	public String formatSubjectList(List<Subject> subjects) {
		AtomicInteger index = new AtomicInteger();
		int nameFieldLength = getMaxFieldLength(subjects, Subject::getName);
		int descriptionFieldLength = getMaxFieldLength(subjects, Subject::getDescription);

		return subjects.stream()
				.map(subject -> String.format(
						"%-3s Name: %-" + nameFieldLength + "s | " + "Description: %-" + descriptionFieldLength + "s",
						index.incrementAndGet() + ".", subject.getName(), subject.getDescription()))
				.collect(Collectors.joining(System.lineSeparator()));
	}

	public String formatTeacherList(List<Teacher> teachers) {
		AtomicInteger index = new AtomicInteger();
		int nameFieldLength = getMaxFieldLength(teachers, Teacher::getFirstName)
				+ getMaxFieldLength(teachers, Teacher::getLastName) + 1;

		return teachers.stream()
				.map(teacher -> String.format("%-3s Name: %-" + nameFieldLength + "s", index.incrementAndGet() + ".",
						teacher.getLastName() + " " + teacher.getFirstName()))
				.collect(Collectors.joining(System.lineSeparator()));
	}

	public String formatAudienceList(List<Audience> audiences) {
		AtomicInteger index = new AtomicInteger();
		int roomFieldLength = getMaxFieldLength(audiences, audience -> String.valueOf(audience.getRoom()));
		int capacityFieldLength = getMaxFieldLength(audiences, audience -> String.valueOf(audience.getCapacity()));

		return audiences.stream()
				.map(audience -> String.format(
						"%-3s Audience: %-" + roomFieldLength + "d | Capacity: %-" + capacityFieldLength + "d",
						index.incrementAndGet() + ".", audience.getRoom(), audience.getCapacity()))
				.collect(Collectors.joining(System.lineSeparator()));
	}

	public String formatStudentList(List<Student> students) {
		AtomicInteger index = new AtomicInteger();
		int nameFieldLength = getMaxFieldLength(students, Student::getFirstName)
				+ getMaxFieldLength(students, Student::getLastName) + 1;
		int groupNameFieldLength = getMaxFieldLength(students, student -> student.getGroup().getName());

		return students.stream()
				.map(student -> String.format(
						"%-3s Name: %-" + nameFieldLength + "s | Group: %-" + groupNameFieldLength + "s",
						index.incrementAndGet() + ".", student.getLastName() + " " + student.getFirstName(),
						student.getGroup().getName()))
				.collect(Collectors.joining(System.lineSeparator()));
	}

	public String formatHolidayList(List<Holiday> holidays) {
		AtomicInteger index = new AtomicInteger();
		int nameFieldLength = getMaxFieldLength(holidays, Holiday::getName);

		return holidays.stream()
				.map(holiday -> String.format("%-3s Name: %-" + nameFieldLength + "s | Date: %s",
						index.incrementAndGet() + ".", holiday.getName(), holiday.getDate()))
				.collect(Collectors.joining(System.lineSeparator()));
	}

	public String formatVacationList(List<Vacation> vacations) {
		AtomicInteger index = new AtomicInteger();

		return vacations.stream().map(vacation -> String.format("%-3s Dates: %s to %s", index.incrementAndGet() + ".",
				vacation.getStart(), vacation.getEnd())).collect(Collectors.joining(System.lineSeparator()));
	}

	public String formatLectureTimesList(List<LectureTime> lecturesTime) {
		AtomicInteger index = new AtomicInteger();

		return lecturesTime.stream()
				.map(lectureTime -> String.format("%-3s Time: %s to %s", index.incrementAndGet() + ".",
						lectureTime.getStart(), lectureTime.getEnd()))
				.collect(Collectors.joining(System.lineSeparator()));
	}

	public String getGenderString() {
		return Stream.of(Gender.values()).map(genderType -> String.format("%s ", genderType.name()))
				.collect(Collectors.joining());
	}

	public String getDegreeString() {
		return Stream.of(Degree.values()).map(degreeType -> String.format("%s ", degreeType.name()))
				.collect(Collectors.joining());
	}
}
