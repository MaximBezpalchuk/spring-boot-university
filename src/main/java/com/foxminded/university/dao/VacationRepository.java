package com.foxminded.university.dao;

import com.foxminded.university.model.Teacher;
import com.foxminded.university.model.Vacation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface VacationRepository extends JpaRepository<Vacation, Integer> {

    List<Vacation> findByTeacherId(int id);

    Optional<Vacation> findByPeriodAndTeacher(@Param("start") LocalDate start, @Param("end") LocalDate end, @Param("teacher") Teacher teacher);

    List<Vacation> findByDateInPeriodAndTeacher(@Param("date") LocalDate date, @Param("teacher") Teacher teacher);

    List<Vacation> findByTeacherIdAndYear(@Param("teacher_id") int id, @Param("year") int year);

    Page<Vacation> findAllByTeacherId(Pageable pageable, int id);
}
