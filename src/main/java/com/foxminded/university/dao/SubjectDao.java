package com.foxminded.university.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.foxminded.university.model.Subject;

public interface SubjectDao extends GenericDao<Subject> {

	List<Subject> findByTeacherId(int id);

	Optional<Subject> findByName(String name);

	Page<Subject> findPaginatedSubjects(Pageable pageable);
}
