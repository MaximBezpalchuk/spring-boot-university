package com.foxminded.university.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.foxminded.university.dao.GroupDao;
import com.foxminded.university.dao.jdbc.JdbcGroupDao;
import com.foxminded.university.model.Group;

@Service
public class GroupService {

	private GroupDao groupDao;

	public GroupService(JdbcGroupDao groupDao) {
		this.groupDao = groupDao;
	}

	public List<Group> findAll() {
		return groupDao.findAll();
	}

	public Group findById(int id) {
		return groupDao.findById(id);
	}

	public String save(Group group) {
		Group existingGroup = groupDao.findByName(group.getName());
		if (existingGroup == null) {
			groupDao.save(group);
			return "Group added!";
		} else if (existingGroup.getId() == group.getId()) {
			groupDao.save(group);
			return "Group updated!";
		}

		return "Unusual error";
	}

	public void deleteById(int id) {
		groupDao.deleteById(id);
	}

	public List<Group> findByLectureId(int id) {
		return groupDao.findByLectureId(id);
	}
}
