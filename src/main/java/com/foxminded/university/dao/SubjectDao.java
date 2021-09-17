package com.foxminded.university.dao;

import java.util.List;

import com.foxminded.university.model.Subject;

public interface SubjectDao extends GenericDao<Subject> {

	List<Subject> findByTeacherId(int id);

	Subject findByName(String name);
}
