package com.foxminded.university.dao;

import java.util.List;
import java.util.Optional;

import com.foxminded.university.model.Subject;

public interface SubjectDao extends GenericDao<Subject> {

	List<Subject> findByTeacherId(int id);

	Optional<Subject> findByName(String name);
}
