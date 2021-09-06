package com.foxminded.university.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.foxminded.university.dao.LectureTimeDao;
import com.foxminded.university.model.LectureTime;

@Service
public class LectureTimeService {

	private LectureTimeDao lectureTimeDao;

	public List<LectureTime> findAll() {
		return lectureTimeDao.findAll();
	}

	public LectureTime findById(int id) {
		return lectureTimeDao.findById(id);
	}

	public void save(LectureTime lectureTime) {
		lectureTimeDao.save(lectureTime);
	}

	public void deleteById(int id) {
		lectureTimeDao.deleteById(id);
	}
}
