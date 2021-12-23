package com.foxminded.university.dao;

import com.foxminded.university.model.Teacher;
import com.foxminded.university.model.Vacation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface VacationDao extends JpaRepository<Vacation, Integer> {

    List<Vacation> findByTeacherId(int id);

    Optional<Vacation> findByPeriodAndTeacher(LocalDate start, LocalDate end, Teacher teacher);

    List<Vacation> findByDateInPeriodAndTeacher(LocalDate date, Teacher teacher);

    List<Vacation> findByTeacherIdAndYear(int id, int year);

    Page<Vacation> findAllByTeacherId(Pageable pageable, int id);
}
