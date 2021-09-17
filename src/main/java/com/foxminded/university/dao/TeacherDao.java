package com.foxminded.university.dao;

import java.time.LocalDate;

import com.foxminded.university.model.Teacher;

public interface TeacherDao extends GenericDao<Teacher> {

	Teacher findByFullNameAndBirthDate(String firstName, String lastName, LocalDate birthDate);
}
