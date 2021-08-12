package com.foxminded.university.dao;

import java.util.List;

import com.foxminded.university.model.Subject;

public interface GenericSubjectDao extends GenericDao<Subject> {

	List<Subject> findByTeacherId(int id);
}
