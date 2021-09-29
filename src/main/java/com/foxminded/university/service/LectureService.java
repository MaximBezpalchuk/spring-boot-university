package com.foxminded.university.service;

import java.time.DayOfWeek;
import java.util.List;
import java.util.Optional;

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
import com.foxminded.university.exception.EntityNotFoundException;
import com.foxminded.university.exception.EntityNotUniqueException;
import com.foxminded.university.exception.LectureInAfterHoursException;
import com.foxminded.university.exception.LectureInOccupiedAudienceException;
import com.foxminded.university.exception.LectureInSmallAudienceException;
import com.foxminded.university.exception.LectureOnHolidayException;
import com.foxminded.university.exception.LectureOnSundayException;
import com.foxminded.university.exception.LectureWithBusyTeacherException;
import com.foxminded.university.exception.LectureWithNotCompetentTeacherException;
import com.foxminded.university.exception.LectureWithTeacherInVacationException;
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
		return lectureDao.findById(id)
				.orElseThrow(() -> new EntityNotFoundException("Can`t find any lecture with id: " + id));
	}

	public void save(Lecture lecture) {
		logger.debug("Save lecture");
		uniqueCheck(lecture);
		sundayCheck(lecture);
		afterHoursCheck(lecture);
		teacherBusyCheck(lecture);
		teacherInVacationCheck(lecture);
		holidayCheck(lecture);
		teacherCompetentWithSubjectCheck(lecture);
		enoughAudienceCapacityCheck(lecture);
		audienceOccupiedCheck(lecture);
		lectureDao.save(lecture);
	}

	public void deleteById(int id) {
		logger.debug("Delete lecture by id: {}", id);
		lectureDao.deleteById(id);
	}

	private void uniqueCheck(Lecture lecture) {
		logger.debug("Check lecture is unique");
		Optional<Lecture> existingLecture = lectureDao.findByTeacherAudienceDateAndLectureTime(lecture.getTeacher(),
				lecture.getAudience(), lecture.getDate(), lecture.getTime());
		if (!existingLecture.isEmpty() && (existingLecture.get().getId() != lecture.getId())) {
			throw new EntityNotUniqueException("Lecture with audience number " + lecture.getAudience().getRoom()
					+ ", date " + lecture.getDate() + " and lecture time " + lecture.getTime().getStart() + " - "
					+ lecture.getTime().getEnd() + " is already exists!");
		}
	}

	private void sundayCheck(Lecture lecture) {
		logger.debug("Check lecture is on sunday");
		if (lecture.getDate().getDayOfWeek().equals(DayOfWeek.SUNDAY)) {
			throw new LectureOnSundayException("Lecture can`t be on sunday! Specified date is: " + lecture.getDate());
		}
	}

	private void afterHoursCheck(Lecture lecture) {
		logger.debug("Check lecture is after hours");
		if (lecture.getTime().getEnd().getHour() > endWorkingDay
				|| lecture.getTime().getStart().getHour() < startWorkingDay) {
			throw new LectureInAfterHoursException("Lecture can`t be in after hours! Specified period is: "
					+ lecture.getTime().getStart() + " - " + lecture.getTime().getEnd());
		}
	}

	private void teacherBusyCheck(Lecture lecture) {
		logger.debug("Check teacher for this lecture is busy");
		if (lectureDao.findLecturesByTeacherDateAndTime(lecture.getTeacher(), lecture.getDate(), lecture.getTime())
				.stream().filter(lec -> lec.getId() != lecture.getId()).findAny().isPresent()) {
			throw new LectureWithBusyTeacherException("Teacher is on another lecture this time! Teacher is: "
					+ lecture.getTeacher().getFirstName() + " " + lecture.getTeacher().getLastName());
		}
	}

	private void teacherInVacationCheck(Lecture lecture) {
		logger.debug("Check teacher for this lecture is in vacation");
		if (!vacationDao.findByDateInPeriodAndTeacher(lecture.getDate(), lecture.getTeacher()).isEmpty()) {
			throw new LectureWithTeacherInVacationException(
					"Teacher is in vacation this date! Date is: " + lecture.getDate());
		}
	}

	private void holidayCheck(Lecture lecture) {
		logger.debug("Check lecture is in holiday time");
		if (!holidayDao.findByDate(lecture.getDate()).isEmpty()) {
			throw new LectureOnHolidayException("Lecture can`t be on holiday! Date is: " + lecture.getDate());
		}
	}

	private void teacherCompetentWithSubjectCheck(Lecture lecture) {
		logger.debug("Check teacher for subject");
		if (!lecture.getTeacher().getSubjects().contains(lecture.getSubject())) {
			throw new LectureWithNotCompetentTeacherException("Teacher " + lecture.getTeacher().getFirstName() + " "
					+ lecture.getTeacher().getLastName() + " can`t educate" + lecture.getSubject().getName() + "!");
		}
	}

	private void enoughAudienceCapacityCheck(Lecture lecture) {
		logger.debug("Check audience size");
		Integer studentsOnLectureCount = lecture.getGroups().stream().map(Group::getId).map(studentDao::findByGroupId)
				.mapToInt(List::size).sum();
		if (studentsOnLectureCount >= lecture.getAudience().getCapacity()) {
			throw new LectureInSmallAudienceException("Student count (" + studentsOnLectureCount
					+ ") more than audience capacity (" + lecture.getAudience().getCapacity() + ")!");
		}
	}

	private void audienceOccupiedCheck(Lecture lecture) {
		logger.debug("Check audience is occupied");
		Optional<Lecture> existingLecture = lectureDao.findByAudienceDateAndLectureTime(lecture.getAudience(),
				lecture.getDate(), lecture.getTime());
		if (!existingLecture.isEmpty() && existingLecture.get().getId() != lecture.getId()) {
			throw new LectureInOccupiedAudienceException(
					"Audience " + lecture.getAudience().getRoom() + " is already occupied!");
		}
	}
}
