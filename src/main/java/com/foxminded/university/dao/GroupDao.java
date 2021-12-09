package com.foxminded.university.dao;

import com.foxminded.university.model.Group;

import java.util.List;
import java.util.Optional;

public interface GroupDao extends GenericDao<Group> {

    List<Group> findByLectureId(int id);

    Optional<Group> findByName(String name);

}
