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

	public void save(Lecture lecture) {
		if (!isSunday(lecture) && !isAfterHours(lecture) && !isTeacherBusy(lecture) && !isTeacherInVacation(lecture)
				&& !isHoliday(lecture) && isTeacherCompetentWithSubject(lecture)
				&& isAudienceCapacityEnoughForStudents(lecture) && !isAudienceOccupied(lecture) && isUnique(lecture)) {
			lectureDao.save(lecture);
		}
	}

	public void deleteById(int id) {
		lectureDao.deleteById(id);
	}

	private boolean isUnique(Lecture lecture) {
		Lecture existingLecture = lectureDao.findByAudienceDateAndLectureTime(lecture.getAudience(), lecture.getDate(),
				lecture.getTime());

		return existingLecture == null || (existingLecture.getId() == lecture.getId());
	}

	private boolean isSunday(Lecture lecture) {
		return lecture.getDate().getDayOfWeek().equals(DayOfWeek.SUNDAY);
	}

	private boolean isAfterHours(Lecture lecture) {
		return lecture.getTime().getEnd().getHour() > 22 || lecture.getTime().getStart().getHour() < 8;
	}

	private boolean isTeacherBusy(Lecture lecture) {
		List<Lecture> lecturesWithSameTeacherThisTime = lectureDao
				.findLecturesByTeacherDateAndTime(lecture.getTeacher(), lecture.getDate(), lecture.getTime());

		return !lecturesWithSameTeacherThisTime.isEmpty();
	}

	private boolean isTeacherInVacation(Lecture lecture) {
		List<Vacation> teacherVacations = vacationDao.findByTeacherId(lecture.getTeacher().getId());
		if (!teacherVacations.isEmpty()) {
			Vacation vacation = teacherVacations.stream().filter(
					vac -> (!(lecture.getDate().isBefore(vac.getStart()) || lecture.getDate().isAfter(vac.getEnd()))))
					.findAny().orElse(null);
			if (vacation != null) {
				return true;
			}
		}

		return false;
	}

	private boolean isHoliday(Lecture lecture) {
		List<Holiday> holidays = holidayDao.findAll();
		if (!holidays.isEmpty()) {
			Holiday holiday = holidays.stream().filter(hol -> lecture.getDate().equals(hol.getDate())).findAny()
					.orElse(null);
			if (holiday != null) {
				return true;
			}
		}

		return false;
	}

	private boolean isTeacherCompetentWithSubject(Lecture lecture) {
		return lecture.getTeacher().getSubjects().contains(lecture.getSubject());
	}

	private boolean isAudienceCapacityEnoughForStudents(Lecture lecture) {
		List<Student> studentsOnLecture = studentDao.findAll().stream()
				.filter(student -> lecture.getGroups().contains(student.getGroup())).collect(Collectors.toList());

		return studentsOnLecture.isEmpty() || (studentsOnLecture.size() <= lecture.getAudience().getCapacity());
	}

	private boolean isAudienceOccupied(Lecture lecture) {
		List<Lecture> lecturesThisDay = lectureDao.findByAudienceAndDate(lecture.getAudience(), lecture.getDate());
		LocalTime start = lecture.getTime().getStart();
		LocalTime end = lecture.getTime().getEnd();
		if (!lecturesThisDay.isEmpty()) {
			Lecture lectureWithConcurrentTime = lecturesThisDay.stream()
					.filter(lec -> (start.isAfter(lec.getTime().getStart()) && start.isBefore(lec.getTime().getEnd()))
							|| (end.isAfter(lec.getTime().getStart()) && end.isBefore(lec.getTime().getEnd()))
							|| start.equals(lec.getTime().getStart()) || end.equals(lec.getTime().getEnd()))
					.findAny().orElse(null);
			return lectureWithConcurrentTime != null;
		}

		return false;
	}
}
