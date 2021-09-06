package com.foxminded.university.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.foxminded.university.dao.StudentDao;
import com.foxminded.university.model.Student;

@Service
public class StudentService {

	private StudentDao studentDao;

	public List<Student> findAll() {
		return studentDao.findAll();
	}

	public Student findById(int id) {
		return studentDao.findById(id);
	}

	public void save(Student student) {
		studentDao.save(student);
	}

	public void deleteById(int id) {
		studentDao.deleteById(id);
	}
}
