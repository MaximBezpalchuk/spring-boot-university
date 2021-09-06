package com.foxminded.university.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.foxminded.university.dao.GroupDao;
import com.foxminded.university.model.Group;

@Service
public class GroupService {

	private GroupDao groupDao;

	public List<Group> findAll() {
		return groupDao.findAll();
	}

	public Group findById(int id) {
		return groupDao.findById(id);
	}

	public void save(Group group) {
		groupDao.save(group);
	}

	public void deleteById(int id) {
		groupDao.deleteById(id);
	}

	public List<Group> findByLectureId(int id) {
		return groupDao.findByLectureId(id);
	}
}
