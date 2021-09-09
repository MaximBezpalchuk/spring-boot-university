package com.foxminded.university.service;

import java.time.Duration;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.foxminded.university.dao.LectureTimeDao;
import com.foxminded.university.dao.jdbc.JdbcLectureTimeDao;
import com.foxminded.university.model.LectureTime;

@Service
public class LectureTimeService {

	private LectureTimeDao lectureTimeDao;
	@Value("${lectureTimeDuration}")
	private int lectureTimeDuration;

	public LectureTimeService(JdbcLectureTimeDao lectureTimeDao) {
		this.lectureTimeDao = lectureTimeDao;
	}

	public List<LectureTime> findAll() {
		return lectureTimeDao.findAll();
	}

	public LectureTime findById(int id) {
		return lectureTimeDao.findById(id);
	}

	public void save(LectureTime lectureTime) {
		if (isUnique(lectureTime) && isTimeCorrect(lectureTime) && isDurationMoreThanThirtyMin(lectureTime)) {
			lectureTimeDao.save(lectureTime);
		}
	}

	public void deleteById(int id) {
		lectureTimeDao.deleteById(id);
	}

	private boolean isUnique(LectureTime lectureTime) {
		LectureTime existingLectureTime = lectureTimeDao.findByPeriod(lectureTime.getStart(), lectureTime.getEnd());

		return existingLectureTime == null || (existingLectureTime.getId() == lectureTime.getId());
	}

	private boolean isTimeCorrect(LectureTime lectureTime) {
		return lectureTime.getStart().isBefore(lectureTime.getEnd());
	}

	private boolean isDurationMoreThanThirtyMin(LectureTime lectureTime) {
		return Duration.between(lectureTime.getStart(), lectureTime.getEnd()).toMinutes() > lectureTimeDuration;
	}
}
