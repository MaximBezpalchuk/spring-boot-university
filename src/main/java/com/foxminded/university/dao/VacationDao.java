package com.foxminded.university.dao;

import com.foxminded.university.model.Teacher;
import com.foxminded.university.model.Vacation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface VacationDao extends GenericDao<Vacation> {

    List<Vacation> findByTeacherId(int id);

    Optional<Vacation> findByPeriodAndTeacher(LocalDate start, LocalDate end, Teacher teacher);

    List<Vacation> findByDateInPeriodAndTeacher(LocalDate date, Teacher teacher);

    List<Vacation> findByTeacherIdAndYear(int id, int year);

    Page<Vacation> findPaginatedVacationsByTeacherId(Pageable pageable, int id);
}
