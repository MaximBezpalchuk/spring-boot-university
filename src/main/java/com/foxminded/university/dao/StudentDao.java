package com.foxminded.university.dao;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.foxminded.university.model.Student;

public interface StudentDao extends GenericDao<Student> {

	Optional<Student> findByFullNameAndBirthDate(String firstName, String lastName, LocalDate birthDate);

	List<Student> findByGroupId(int id);

	Page<Student> findPaginatedStudents(Pageable pageable);
}
