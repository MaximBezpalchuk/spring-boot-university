package com.foxminded.university.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.foxminded.university.dao.jdbc.JdbcLectureDao;
import com.foxminded.university.model.Lecture;

@Service
public class LectureService {

	private JdbcLectureDao lectureDao;

	public LectureService(JdbcLectureDao lectureDao) {
		this.lectureDao = lectureDao;
	}

	public List<Lecture> findAll() {
		return lectureDao.findAll();
	}

	public Lecture findById(int id) {
		return lectureDao.findById(id);
	}

	public void save(Lecture lecture) {
		lectureDao.save(lecture);
	}

	public void deleteById(int id) {
		lectureDao.deleteById(id);
	}
}
