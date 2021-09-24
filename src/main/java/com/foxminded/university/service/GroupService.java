package com.foxminded.university.service;

import java.util.List;
import java.util.NoSuchElementException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.foxminded.university.dao.GroupDao;
import com.foxminded.university.dao.jdbc.JdbcGroupDao;
import com.foxminded.university.exception.DaoException;
import com.foxminded.university.exception.EntityNotFoundException;
import com.foxminded.university.model.Group;

@Service
public class GroupService {

	private static final Logger logger = LoggerFactory.getLogger(GroupService.class);

	private GroupDao groupDao;

	public GroupService(JdbcGroupDao groupDao) {
		this.groupDao = groupDao;
	}

	public List<Group> findAll() {
		logger.debug("Find all groups");
		return groupDao.findAll();
	}

	public Group findById(int id) throws EntityNotFoundException {
		logger.debug("Find group by id {}", id);
		try {
			return groupDao.findById(id).orElseThrow();
		} catch (NoSuchElementException e) {
			throw new EntityNotFoundException("Can`t find any group", e);
		}
	}

	public void save(Group group) {
		logger.debug("Save group");
		if (isUnique(group)) {
			groupDao.save(group);
		}
	}

	public void deleteById(int id) {
		logger.debug("Delete group by id: {}", id);
		groupDao.deleteById(id);
	}

	public List<Group> findByLectureId(int id) {
		logger.debug("Find groups by lecture id");
		return groupDao.findByLectureId(id);
	}

	private boolean isUnique(Group group) {
		logger.debug("Check group is unique");
		try {
			Group existingGroup = groupDao.findByName(group.getName());

			return existingGroup == null || (existingGroup.getId() == group.getId());
		} catch (DaoException e) {
			logger.error("Group with same name is already exists: {}", group.getName());
			return false;
		}
	}
}
