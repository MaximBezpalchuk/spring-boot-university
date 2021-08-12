package com.foxminded.university.dao;

import java.util.List;

import com.foxminded.university.model.Group;

public interface GenericGroupDao extends GenericDao<Group> {

	List<Group> findByLectureId(int id);
}
