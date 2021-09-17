package com.foxminded.university.dao;

import java.time.LocalDate;
import java.util.List;

import com.foxminded.university.model.Teacher;
import com.foxminded.university.model.Vacation;

public interface VacationDao extends GenericDao<Vacation> {

	List<Vacation> findByTeacherId(int id);

	Vacation findByPeriodAndTeacher(LocalDate start, LocalDate end, Teacher teacher);

	List<Vacation> findByDateInPeriodAndTeacher(LocalDate date, Teacher teacher);

	List<Vacation> findByTeacherIdAndYear(int id, int year);
}
