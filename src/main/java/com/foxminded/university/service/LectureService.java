package com.foxminded.university.service;

import java.time.DayOfWeek;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.foxminded.university.dao.HolidayDao;
import com.foxminded.university.dao.LectureDao;
import com.foxminded.university.dao.StudentDao;
import com.foxminded.university.dao.VacationDao;
import com.foxminded.university.dao.jdbc.JdbcHolidayDao;
import com.foxminded.university.dao.jdbc.JdbcLectureDao;
import com.foxminded.university.dao.jdbc.JdbcStudentDao;
import com.foxminded.university.dao.jdbc.JdbcVacationDao;
import com.foxminded.university.model.Lecture;
import com.foxminded.university.model.Vacation;

@Service
public class LectureService {

	private LectureDao lectureDao;
	private VacationDao vacationDao;
	private HolidayDao holidayDao;
	private StudentDao studentDao;
	@Value("startWorkingDay")
	private int startWorkingDay;
	@Value("endWorkingDay")
	private int endWorkingDay;

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
				&& !isHoliday(lecture) && isTeacherCompetentWithSubject(lecture) && isEnoughAudienceCapacity(lecture)
				&& !isAudienceOccupied(lecture) && isUnique(lecture)) {
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
		return lecture.getTime().getEnd().getHour() > startWorkingDay || lecture.getTime().getStart().getHour() < endWorkingDay;
	}

	private boolean isTeacherBusy(Lecture lecture) {
		List<Lecture> lecturesWithSameTeacherThisTime = lectureDao
				.findLecturesByTeacherDateAndTime(lecture.getTeacher(), lecture.getDate(), lecture.getTime());
		boolean isSameLecture = lecturesWithSameTeacherThisTime.stream().filter(lec -> lec.getId() != lecture.getId())
				.findAny().isPresent();

		return !lecturesWithSameTeacherThisTime.isEmpty() && isSameLecture;
	}

	private boolean isTeacherInVacation(Lecture lecture) {
		List<Vacation> teacherVacations = vacationDao.findByDateInPeriodAndTeacher(lecture.getDate(),
				lecture.getTeacher());

		return !teacherVacations.isEmpty();
	}

	private boolean isHoliday(Lecture lecture) {
		return !holidayDao.findByDate(lecture.getDate()).isEmpty();
	}

	private boolean isTeacherCompetentWithSubject(Lecture lecture) {
		return lecture.getTeacher().getSubjects().contains(lecture.getSubject());
	}

	private boolean isEnoughAudienceCapacity(Lecture lecture) {
		Integer studentsOnLectureCount = lecture.getGroups().stream()
				.map(group -> studentDao.findByGroupId(group.getId())).mapToInt(List::size).sum();

		return studentsOnLectureCount == 0 || (studentsOnLectureCount <= lecture.getAudience().getCapacity());
	}

	private boolean isAudienceOccupied(Lecture lecture) {
		Lecture existingLecture = lectureDao.findByAudienceDateAndLectureTime(lecture.getAudience(), lecture.getDate(),
				lecture.getTime());

		return existingLecture != null && existingLecture.getId() != lecture.getId();
	}
}
