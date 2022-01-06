package com.foxminded.university.dao;

import com.foxminded.university.model.Teacher;
import com.foxminded.university.model.Vacation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;
import java.util.Optional;

@Repository
public interface VacationRepository extends JpaRepository<Vacation, Integer> {

    List<Vacation> findByTeacherId(int id);

    Optional<Vacation> findByStartAndEndAndTeacher(LocalDate start, LocalDate end, Teacher teacher);

    //find teacher vacations, that have vacations this date
    List<Vacation> findByTeacherAndStartGreaterThanEqualAndEndLessThanEqual(Teacher teacher, LocalDate date, LocalDate sameDate);

    List<Vacation> findByTeacherAndStartBetween(Teacher teacher, LocalDate startOfYear, LocalDate endOfYear);

    Page<Vacation> findAllByTeacherId(Pageable pageable, int id);

    default List<Vacation> findByTeacherAndYear(Teacher teacher, int year) {
        return findByTeacherAndStartBetween(teacher, LocalDate.of(year, Month.JANUARY, 1), LocalDate.of(year, Month.DECEMBER, 31));
    }
}
