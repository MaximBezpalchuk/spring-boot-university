package com.foxminded.university.service;

import com.foxminded.university.dao.GroupDao;
import com.foxminded.university.exception.EntityNotFoundException;
import com.foxminded.university.exception.EntityNotUniqueException;
import com.foxminded.university.model.Group;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class GroupService {

	private static final Logger logger = LoggerFactory.getLogger(GroupService.class);

	private GroupDao groupDao;

	public GroupService(GroupDao groupDao) {
		this.groupDao = groupDao;
	}

	public List<Group> findAll() {
		logger.debug("Find all groups");
		return groupDao.findAll();
	}

	public Group findById(int id) {
		logger.debug("Find group by id {}", id);
		return groupDao.findById(id)
				.orElseThrow(() -> new EntityNotFoundException("Can`t find any group with id: " + id));
	}

	public void save(Group group) {
		logger.debug("Save group");
		uniqueCheck(group);
		groupDao.save(group);
	}

	public void deleteById(int id) {
		logger.debug("Delete group by id: {}", id);
		groupDao.deleteById(id);
	}

    private void uniqueCheck(Group group) {
        logger.debug("Check group is unique");
        Optional<Group> existingGroup = groupDao.findByName(group.getName());
        if (existingGroup.isPresent() && (existingGroup.get().getId() != group.getId())) {
            throw new EntityNotUniqueException("Group with name " + group.getName() + " is already exists!");
        }
    }
}
