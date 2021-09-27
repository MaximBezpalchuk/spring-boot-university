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

	public Lecture findById(int id) throws EntityNotFoundException {
		logger.debug("Find lecture by id {}", id);
		return lectureDao.findById(id).orElseThrow(() -> new EntityNotFoundException("Can`t find any lecture"));
	}

	public void save(Lecture lecture) throws Exception {
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

	private boolean isUnique(Lecture lecture) throws EntityNotUniqueException {
		logger.debug("Check lecture is unique");
		Optional<Lecture> existingLecture = lectureDao.findByTeacherAudienceDateAndLectureTime(lecture.getTeacher(),
				lecture.getAudience(), lecture.getDate(), lecture.getTime());
		if (existingLecture.isEmpty() || (existingLecture.get().getId() == lecture.getId())) {
			return true;
		} else {
			throw new EntityNotUniqueException("Lecture with same audience, date and lecture time is already exists!");
		}
	}

	private boolean isSunday(Lecture lecture) throws LectureOnSundayException {
		logger.debug("Check lecture is on sunday");
		if (lecture.getDate().getDayOfWeek().equals(DayOfWeek.SUNDAY)) {
			throw new LectureOnSundayException("Lecture can`t be on sunday!");
		} else {
			return false;
		}
	}

	private boolean isAfterHours(Lecture lecture) throws LectureInAfterHoursException {
		logger.debug("Check lecture is after hours");
		if (lecture.getTime().getEnd().getHour() > endWorkingDay
				|| lecture.getTime().getStart().getHour() < startWorkingDay) {
			throw new LectureInAfterHoursException("Lecture can`t be in after hours!");
		} else {
			return false;
		}
	}

	private boolean isTeacherBusy(Lecture lecture) throws LectureWithBusyTeacherException {
		logger.debug("Check teacher for this lecture is busy");
		if (lectureDao.findLecturesByTeacherDateAndTime(lecture.getTeacher(), lecture.getDate(), lecture.getTime())
				.stream().filter(lec -> lec.getId() != lecture.getId()).findAny().isPresent()) {
			throw new LectureWithBusyTeacherException("Teacher is on another lecture this time!");
		} else {
			return false;
		}
	}

	private boolean isTeacherInVacation(Lecture lecture) throws LectureWithTeacherInVacationException {
		logger.debug("Check teacher for this lecture is in vacation");
		if (!vacationDao.findByDateInPeriodAndTeacher(lecture.getDate(), lecture.getTeacher()).isEmpty()) {
			throw new LectureWithTeacherInVacationException("Teacher is in vacation this date!");
		} else {
			return false;
		}
	}

	private boolean isHoliday(Lecture lecture) throws LectureOnHolidayException {
		logger.debug("Check lecture is in holiday time");
		if (!holidayDao.findByDate(lecture.getDate()).isEmpty()) {
			throw new LectureOnHolidayException("Lecture can`t be on holiday!");
		} else {
			return false;
		}
	}

	private boolean isTeacherCompetentWithSubject(Lecture lecture) throws LectureWithNotCompetentTeacherException {
		logger.debug("Check teacher for subject");
		if (lecture.getTeacher().getSubjects().contains(lecture.getSubject())) {
			return true;
		} else {
			throw new LectureWithNotCompetentTeacherException("Teacher can`t educate this subject!");
		}
	}

	private boolean isEnoughAudienceCapacity(Lecture lecture) throws LectureInSmallAudienceException {
		logger.debug("Check audience size");
		Integer studentsOnLectureCount = lecture.getGroups().stream().map(Group::getId).map(studentDao::findByGroupId)
				.mapToInt(List::size).sum();
		if (studentsOnLectureCount <= lecture.getAudience().getCapacity()) {
			return true;
		} else {
			throw new LectureInSmallAudienceException("Student count more than audience capacity!");
		}
	}

	private boolean isAudienceOccupied(Lecture lecture) throws LectureInOccupiedAudienceException {
		logger.debug("Check audience is occupied");
		Optional<Lecture> existingLecture = lectureDao.findByAudienceDateAndLectureTime(lecture.getAudience(),
				lecture.getDate(), lecture.getTime());
		if (!existingLecture.isEmpty() && existingLecture.get().getId() != lecture.getId()) {
			throw new LectureInOccupiedAudienceException("This audience is already occupied!");
		} else {
			return false;
		}
	}
}
