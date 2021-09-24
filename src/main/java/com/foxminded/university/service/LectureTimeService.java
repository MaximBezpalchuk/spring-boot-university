package com.foxminded.university.service;

import java.time.Duration;
import java.util.List;
import java.util.NoSuchElementException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.foxminded.university.dao.LectureTimeDao;
import com.foxminded.university.dao.jdbc.JdbcLectureTimeDao;
import com.foxminded.university.exception.DaoException;
import com.foxminded.university.exception.EntityNotFoundException;
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
		try {
			return lectureTimeDao.findById(id).orElseThrow();
		} catch (NoSuchElementException e) {
			throw new EntityNotFoundException("Can`t find any lecture time", e);
		}
	}

	public void save(LectureTime lectureTime) {
		logger.debug("Save lecture time");
		if (isUnique(lectureTime) && isTimeCorrect(lectureTime) && isDurationMoreThanChosenTime(lectureTime)) {
			lectureTimeDao.save(lectureTime);
		}
	}

	public void deleteById(int id) {
		logger.debug("Delete lecture time by id: {}", id);
		lectureTimeDao.deleteById(id);
	}

	private boolean isUnique(LectureTime lectureTime) {
		logger.debug("Check lecture time is unique");
		try {
			LectureTime existingLectureTime = lectureTimeDao.findByPeriod(lectureTime.getStart(), lectureTime.getEnd());

			return existingLectureTime == null || (existingLectureTime.getId() == lectureTime.getId());
		} catch (DaoException e) {
			logger.error("Lecture time with same start: {} and end: {} is already exists", lectureTime.getStart(),
					lectureTime.getEnd());
			return false;
		}
	}

	private boolean isTimeCorrect(LectureTime lectureTime) {
		logger.debug("Check that start time is after end time");
		return lectureTime.getStart().isBefore(lectureTime.getEnd());
	}

	private boolean isDurationMoreThanChosenTime(LectureTime lectureTime) {
		logger.debug("Check that duration is more than min lecture duration");
		return Duration.between(lectureTime.getStart(), lectureTime.getEnd())
				.toMinutes() >= minLectureDurationInMinutes;
	}
}
