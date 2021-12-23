package com.foxminded.university.dao;

import com.foxminded.university.model.Subject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SubjectDao extends JpaRepository<Subject, Integer> {

    Optional<Subject> findByName(String name);

    Page<Subject> findAll(Pageable pageable);
}
