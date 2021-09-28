package com.foxminded.university.service;

import java.time.Duration;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.foxminded.university.dao.LectureTimeDao;
import com.foxminded.university.dao.jdbc.JdbcLectureTimeDao;
import com.foxminded.university.exception.EntityNotFoundException;
import com.foxminded.university.exception.EntityNotUniqueException;
import com.foxminded.university.exception.LectureTimeDurationMoreThanChosenTimeException;
import com.foxminded.university.exception.LectureTimeNotCorrectException;
import com.foxminded.university.exception.ServiceException;
import com.foxminded.university.model.LectureTime;

@Service
public class LectureTimeService {

	private static final Logger logger = LoggerFactory.getLogger(LectureTimeService.class);

	private LectureTimeDao lectureTimeDao;
	@Value("${minLectureDurationInMinutes}")
	private int minLectureDurationInMinutes;

	public LectureTimeService(JdbcLectureTimeDao lectureTimeDao) {
		this.lectureTimeDao = lectureTimeDao;
	}

	public List<LectureTime> findAll() {
		logger.debug("Find all lecture times");
		return lectureTimeDao.findAll();
	}

	public LectureTime findById(int id) throws EntityNotFoundException {
		logger.debug("Find lecture time by id {}", id);
		return lectureTimeDao.findById(id)
				.orElseThrow(() -> new EntityNotFoundException("Can`t find any lecture time with id: " + id));
	}

	public void save(LectureTime lectureTime) throws ServiceException {
		logger.debug("Save lecture time");
		uniqueCheck(lectureTime);
		timeCorrectCheck(lectureTime);
		durationMoreThanChosenTimeCheck(lectureTime);
		lectureTimeDao.save(lectureTime);

	}

	public void deleteById(int id) {
		logger.debug("Delete lecture time by id: {}", id);
		lectureTimeDao.deleteById(id);
	}

	private void uniqueCheck(LectureTime lectureTime) throws EntityNotUniqueException {
		logger.debug("Check lecture time is unique");
		Optional<LectureTime> existingLectureTime = lectureTimeDao.findByPeriod(lectureTime.getStart(),
				lectureTime.getEnd());

		if (!existingLectureTime.isEmpty() && (existingLectureTime.get().getId() != lectureTime.getId())) {
			throw new EntityNotUniqueException("Lecture time with start time " + lectureTime.getStart()
					+ " and end time " + lectureTime.getEnd() + " is already exists!");
		}
	}

	private void timeCorrectCheck(LectureTime lectureTime) throws LectureTimeNotCorrectException {
		logger.debug("Check that start time is after end time");
		if (!lectureTime.getStart().isBefore(lectureTime.getEnd())) {
			throw new LectureTimeNotCorrectException("Lecture time`s start (" + lectureTime.getStart()
					+ ") can`t be after lecture time`s end (" + lectureTime.getEnd() + ")!");
		}
	}

	private void durationMoreThanChosenTimeCheck(LectureTime lectureTime)
			throws LectureTimeDurationMoreThanChosenTimeException {
		logger.debug("Check that duration is more than min lecture duration");
		long durationInMinutes = Duration.between(lectureTime.getStart(), lectureTime.getEnd()).toMinutes();
		if (durationInMinutes <= minLectureDurationInMinutes) {
			throw new LectureTimeDurationMoreThanChosenTimeException("Duration " + durationInMinutes
					+ " minutes is less than min lecture duration (" + minLectureDurationInMinutes + " minutes)!");
		}
	}
}
