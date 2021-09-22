package com.foxminded.university.service;

import java.time.DayOfWeek;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import com.foxminded.university.exception.DaoException;
import com.foxminded.university.model.Group;
import com.foxminded.university.model.Lecture;

@Service
public class LectureService {

	private static final Logger logger = LoggerFactory.getLogger(LectureService.class);

	private LectureDao lectureDao;
	private VacationDao vacationDao;
	private HolidayDao holidayDao;
	private StudentDao studentDao;
	@Value("${startWorkingDay}")
	private int startWorkingDay;
	@Value("${endWorkingDay}")
	private int endWorkingDay;

	public LectureService(JdbcLectureDao lectureDao, JdbcVacationDao vacationDao, JdbcHolidayDao holidayDao,
			JdbcStudentDao studentDao) {
		this.lectureDao = lectureDao;
		this.vacationDao = vacationDao;
		this.holidayDao = holidayDao;
		this.studentDao = studentDao;
	}

	public List<Lecture> findAll() {
		logger.debug("Find all lectures");
		return lectureDao.findAll();
	}

	public Lecture findById(int id) {
		logger.debug("Find lecture by id {}", id);
		try {
			return lectureDao.findById(id);
		} catch (DaoException e) {
			logger.error("Cannot find lecture with id: {}", id, e);
			return null;
		}
	}

	public void save(Lecture lecture) {
		logger.debug("Save lecture");
		if (!isSunday(lecture) && !isAfterHours(lecture) && !isTeacherBusy(lecture) && !isTeacherInVacation(lecture)
				&& !isHoliday(lecture) && isTeacherCompetentWithSubject(lecture) && isEnoughAudienceCapacity(lecture)
				&& !isAudienceOccupied(lecture) && isUnique(lecture)) {
			lectureDao.save(lecture);
		}
	}

	public void deleteById(int id) {
		logger.debug("Delete lecture by id: {}", id);
		lectureDao.deleteById(id);
	}

	private boolean isUnique(Lecture lecture) {
		logger.debug("Check lecture is unique");
		try {
			Lecture existingLecture = lectureDao.findByAudienceDateAndLectureTime(lecture.getAudience(),
					lecture.getDate(), lecture.getTime());

			return existingLecture == null || (existingLecture.getId() == lecture.getId());
		} catch (DaoException e) {
			logger.error("Holiday with same audience: {}, date: {} and lecture time: {} is already exists",
					lecture.getAudience(), lecture.getDate(), lecture.getTime());
			return false;
		}
	}

	private boolean isSunday(Lecture lecture) {
		logger.debug("Check lecture is on sunday");
		return lecture.getDate().getDayOfWeek().equals(DayOfWeek.SUNDAY);
	}

	private boolean isAfterHours(Lecture lecture) {
		logger.debug("Check lecture is after hours");
		return lecture.getTime().getEnd().getHour() > endWorkingDay
				|| lecture.getTime().getStart().getHour() < startWorkingDay;
	}

	private boolean isTeacherBusy(Lecture lecture) {
		logger.debug("Check teacher for this lecture is busy");
		return lectureDao.findLecturesByTeacherDateAndTime(lecture.getTeacher(), lecture.getDate(), lecture.getTime())
				.stream().filter(lec -> lec.getId() != lecture.getId()).findAny().isPresent();
	}

	private boolean isTeacherInVacation(Lecture lecture) {
		logger.debug("Check teacher for this lecture is in vacation");
		return !vacationDao.findByDateInPeriodAndTeacher(lecture.getDate(), lecture.getTeacher()).isEmpty();
	}

	private boolean isHoliday(Lecture lecture) {
		logger.debug("Check lecture is in holiday time");
		return !holidayDao.findByDate(lecture.getDate()).isEmpty();
	}

	private boolean isTeacherCompetentWithSubject(Lecture lecture) {
		logger.debug("Check teacher for subject");
		return lecture.getTeacher().getSubjects().contains(lecture.getSubject());
	}

	private boolean isEnoughAudienceCapacity(Lecture lecture) {
		logger.debug("Check audience size");
		Integer studentsOnLectureCount = lecture.getGroups().stream().map(Group::getId).map(studentDao::findByGroupId)
				.mapToInt(List::size).sum();

		return (studentsOnLectureCount <= lecture.getAudience().getCapacity());
	}

	private boolean isAudienceOccupied(Lecture lecture) {
		logger.debug("Check audience is occupied");
		Lecture existingLecture = lectureDao.findByAudienceDateAndLectureTime(lecture.getAudience(), lecture.getDate(),
				lecture.getTime());

		return existingLecture != null && existingLecture.getId() != lecture.getId();
	}
}
