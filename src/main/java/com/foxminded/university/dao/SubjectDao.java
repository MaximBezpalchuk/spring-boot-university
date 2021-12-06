package com.foxminded.university.dao;

import com.foxminded.university.model.Subject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface SubjectDao extends GenericDao<Subject> {

    Optional<Subject> findByName(String name);

	Page<Subject> findPaginatedSubjects(Pageable pageable);
}
