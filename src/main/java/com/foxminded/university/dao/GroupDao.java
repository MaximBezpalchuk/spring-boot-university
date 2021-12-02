package com.foxminded.university.dao;

import com.foxminded.university.model.Group;

import java.util.Optional;

public interface GroupDao extends GenericDao<Group> {

    Optional<Group> findByName(String name);
}
