package com.foxminded.university.service;

import com.foxminded.university.dao.GroupRepository;
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

    private final GroupRepository groupRepository;

    public GroupService(GroupRepository groupRepository) {
        this.groupRepository = groupRepository;
    }

    public List<Group> findAll() {
        logger.debug("Find all groups");
        return groupRepository.findAll();
    }

    public Group findById(int id) {
        logger.debug("Find group by id {}", id);
        return groupRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Can`t find any group with id: " + id));
    }

    public Group findByName(String name) {
        logger.debug("Find group by name {}", name);
        return groupRepository.findByName(name)
                .orElseThrow(() -> new EntityNotFoundException("Can`t find any group with name: " + name));
    }

    public Group save(Group group) {
        logger.debug("Save group");
        uniqueCheck(group);

        return groupRepository.save(group);
    }

    public void delete(int id) {
        logger.debug("Delete group with id: {}", id);
        groupRepository.deleteById(id);
    }

    private void uniqueCheck(Group group) {
        logger.debug("Check group is unique");
        Optional<Group> existingGroup = groupRepository.findByName(group.getName());
        if (existingGroup.isPresent() && (existingGroup.get().getId() != group.getId())) {
            throw new EntityNotUniqueException("Group with name " + group.getName() + " is already exists!");
        }
    }
}
