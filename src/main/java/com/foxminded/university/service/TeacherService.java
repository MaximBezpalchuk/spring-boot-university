package com.foxminded.university.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.foxminded.university.dao.TeacherDao;
import com.foxminded.university.dao.jdbc.JdbcTeacherDao;
import com.foxminded.university.model.Teacher;

@Service
public class TeacherService {

	private TeacherDao teacherDao;

	public TeacherService(JdbcTeacherDao teacherDao) {
		this.teacherDao = teacherDao;
	}

	public List<Teacher> findAll() {
		return teacherDao.findAll();
	}

	public Teacher findById(int id) {
		return teacherDao.findById(id);
	}

	public void save(Teacher teacher) {
		teacherDao.save(teacher);
	}

	public void deleteById(int id) {
		teacherDao.deleteById(id);
	}
}
