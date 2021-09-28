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
import com.foxminded.university.exception.ServiceLayerException;
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
		return lectureTimeDao.findById(id).orElseThrow(
				() -> new EntityNotFoundException("Can`t find any lecture time with specified id!", "Id is: " + id));
	}

	public void save(LectureTime lectureTime) throws ServiceLayerException {
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
			throw new EntityNotUniqueException("Lecture time with same start and end times is already exists!",
					"Lecture time start: " + lectureTime.getStart(), "Lecture time end: " + lectureTime.getEnd());
		}
	}

	private void timeCorrectCheck(LectureTime lectureTime) throws LectureTimeNotCorrectException {
		logger.debug("Check that start time is after end time");
		if (!lectureTime.getStart().isBefore(lectureTime.getEnd())) {
			throw new LectureTimeNotCorrectException("Lecture time`s start can`t be after lecture time`s end!",
					"Lecture time start: " + lectureTime.getStart(), "Lecture time end: " + lectureTime.getEnd());
		}
	}

	private void durationMoreThanChosenTimeCheck(LectureTime lectureTime)
			throws LectureTimeDurationMoreThanChosenTimeException {
		logger.debug("Check that duration is more than min lecture duration");
		long durationInMinutes = Duration.between(lectureTime.getStart(), lectureTime.getEnd()).toMinutes();
		if (durationInMinutes <= minLectureDurationInMinutes) {
			throw new LectureTimeDurationMoreThanChosenTimeException("Duration is less than min lecture duration!",
					"Duration is: " + durationInMinutes + "minutes", "Min is: " + minLectureDurationInMinutes);
		}
	}
}
