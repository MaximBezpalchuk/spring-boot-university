package com.foxminded.university.service;

import java.time.Duration;
import java.time.LocalTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.foxminded.university.dao.LectureTimeDao;
import com.foxminded.university.dao.jdbc.JdbcLectureTimeDao;
import com.foxminded.university.model.LectureTime;

@Service
public class LectureTimeService {

	private LectureTimeDao lectureTimeDao;

	public LectureTimeService(JdbcLectureTimeDao lectureTimeDao) {
		this.lectureTimeDao = lectureTimeDao;
	}

	public List<LectureTime> findAll() {
		return lectureTimeDao.findAll();
	}

	public LectureTime findById(int id) {
		return lectureTimeDao.findById(id);
	}

	public String save(LectureTime lectureTime) {
		LocalTime start = lectureTime.getStart();
		LocalTime end = lectureTime.getEnd();
		LectureTime existingLectureTime = lectureTimeDao.findByPeriod(start, end);
		if (start.isAfter(end)) {
			return "Lecture can`t start after end time";
		} else if (Duration.between(start, end).toMinutes() < 30) {
			return "Lecture can`t be less than 30 minutes";
		} else if (existingLectureTime == null) {
			lectureTimeDao.save(lectureTime);
			return "Lecture time added!";
		} else if (existingLectureTime.getId() == lectureTime.getId()) {
			lectureTimeDao.save(lectureTime);
			return "Lecture time updated!";
		}

		return "Unusual error";
	}

	public void deleteById(int id) {
		lectureTimeDao.deleteById(id);
	}
}
