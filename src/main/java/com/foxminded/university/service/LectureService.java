package com.foxminded.university.service;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.foxminded.university.dao.LectureDao;
import com.foxminded.university.dao.jdbc.JdbcHolidayDao;
import com.foxminded.university.dao.jdbc.JdbcLectureDao;
import com.foxminded.university.dao.jdbc.JdbcStudentDao;
import com.foxminded.university.dao.jdbc.JdbcVacationDao;
import com.foxminded.university.model.Holiday;
import com.foxminded.university.model.Lecture;
import com.foxminded.university.model.Student;
import com.foxminded.university.model.Vacation;

@Service
public class LectureService {

	private LectureDao lectureDao;
	private JdbcVacationDao vacationDao;
	private JdbcHolidayDao holidayDao;
	private JdbcStudentDao studentDao;

	public LectureService(JdbcLectureDao lectureDao, JdbcVacationDao vacationDao, JdbcHolidayDao holidayDao,
			JdbcStudentDao studentDao) {
		this.lectureDao = lectureDao;
		this.vacationDao = vacationDao;
		this.holidayDao = holidayDao;
		this.studentDao = studentDao;
	}

	public List<Lecture> findAll() {
		return lectureDao.findAll();
	}

	public Lecture findById(int id) {
		return lectureDao.findById(id);
	}

	public String save(Lecture lecture) {

		if (lecture.getDate().getDayOfWeek().equals(DayOfWeek.SUNDAY)) {
			return "Lecture cant be in sunday";
		} else if (lecture.getTime().getEnd().getHour() > 22 || lecture.getTime().getStart().getHour() < 8) {
			return "Lecture must start after 8 and end before 22";
		}

		List<Lecture> lecturesWithSameTeacher = lectureDao.findLecturesByTeacher(lecture.getTeacher());
		if (!lecturesWithSameTeacher.isEmpty()) {
			Lecture lectureInSameTime = lecturesWithSameTeacher.stream()
					.filter(lec -> lec.getDate().isEqual(lecture.getDate()))
					.filter(lec -> lec.getTime().equals(lecture.getTime())).findAny().orElse(null);
			if (lectureInSameTime != null) {
				return "Teacher is on other lecture at this time";
			}
		}

		List<Vacation> teacherVacations = vacationDao.findByTeacherId(lecture.getTeacher().getId());
		if (!teacherVacations.isEmpty()) {
			Vacation vacation = teacherVacations.stream().filter(
					vac -> (!(lecture.getDate().isBefore(vac.getStart()) || lecture.getDate().isAfter(vac.getEnd()))))
					.findAny().orElse(null);
			if (vacation != null) {
				return "Teacher have vacation at this time";
			}
		}

		List<Holiday> holidays = holidayDao.findAll();
		if (!holidays.isEmpty()) {
			Holiday holiday = holidays.stream().filter(hol -> lecture.getDate().equals(hol.getDate())).findAny()
					.orElse(null);
			if (holiday != null) {
				return "It is holiday time: " + holiday.getName();
			}
		}

		if (!lecture.getTeacher().getSubjects().contains(lecture.getSubject())) {
			return "Teacher can`t teach this subject";
		}

		List<Student> studentsOnLecture = studentDao.findAll().stream()
				.filter(student -> lecture.getGroups().contains(student.getGroup())).collect(Collectors.toList());
		if (!studentsOnLecture.isEmpty() && studentsOnLecture.size() >= lecture.getAudience().getCapacity()) {
			return "Audience have less capacity then student count";
		}

		List<Lecture> lecturesThisDay = lectureDao.findByAudienceAndDate(lecture.getAudience(), lecture.getDate());
		LocalTime start = lecture.getTime().getStart();
		LocalTime end = lecture.getTime().getEnd();
		Lecture lectureWithConcurrentTime = lecturesThisDay.stream()
				.filter(lec -> (start.isAfter(lec.getTime().getStart())
						&& start.isBefore(lec.getTime().getEnd()))
						|| (end.isAfter(lec.getTime().getStart())
								&& end.isBefore(lec.getTime().getEnd()))
						|| start.equals(lec.getTime().getStart())
						|| end.equals(lec.getTime().getEnd()))
				.findAny().orElse(null);
		if (lectureWithConcurrentTime != null) {
			return "Another lecture on this time already exists";
		}

		Lecture existingLecture = lectureDao.findByAudienceDateAndLectureTime(lecture.getAudience(), lecture.getDate(),
				lecture.getTime());
		if (existingLecture == null) {
			lectureDao.save(lecture);
			return "Lecture added!";
		} else if (existingLecture.getId() == lecture.getId()) {
			lectureDao.save(lecture);
			return "Lecture updated!";
		}

		return "Unusual error";
	}

	public void deleteById(int id) {
		lectureDao.deleteById(id);
	}
}
