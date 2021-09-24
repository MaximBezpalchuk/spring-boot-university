package com.foxminded.university.dao;

import java.time.LocalDate;
import java.util.Optional;

import com.foxminded.university.model.Teacher;

public interface TeacherDao extends GenericDao<Teacher> {

	Optional<Teacher> findByFullNameAndBirthDate(String firstName, String lastName, LocalDate birthDate);
}
