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

	public String save(Teacher teacher) {
		Teacher existingTeacher = teacherDao.findByFullNameAndBirthDate(teacher.getFirstName(), teacher.getLastName(),
				teacher.getBirthDate());
		if (existingTeacher == null) {
			teacherDao.save(teacher);
			return "Teacher added!";
		} else if (existingTeacher.getId() == teacher.getId()) {
			teacherDao.save(teacher);
			return "Teacher updated!";
		}

		return "Teacher with such full name and birthday already exists!";
	}

	public void deleteById(int id) {
		teacherDao.deleteById(id);
	}
}
