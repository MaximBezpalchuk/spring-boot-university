package com.foxminded.university.dao;

import com.foxminded.university.model.LectureTime;
import com.foxminded.university.model.Subject;
import com.foxminded.university.model.Teacher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface TeacherRepository extends JpaRepository<Teacher, Integer> {

    Optional<Teacher> findByFirstNameAndLastNameAndBirthDate(String firstName, String lastName, LocalDate birthDate);

    Page<Teacher> findAll(Pageable pageable);

    @Query(nativeQuery = true)
    List<Teacher> findByFreeDateAndSubjectWithCurrentTeacher(@Param("date") LocalDate date, @Param("lecture_time_id") LectureTime time, @Param("subject_id") Subject subject);
}
