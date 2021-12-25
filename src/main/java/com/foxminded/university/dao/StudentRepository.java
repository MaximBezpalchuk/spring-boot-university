package com.foxminded.university.dao;

import com.foxminded.university.model.Student;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, Integer> {

    Optional<Student> findByFullNameAndBirthDate(@Param("first_name") String firstName, @Param("last_name") String lastName, @Param("birth_date") LocalDate birthDate);

    List<Student> findByGroupId(int id);

    Page<Student> findAll(Pageable pageable);
}
