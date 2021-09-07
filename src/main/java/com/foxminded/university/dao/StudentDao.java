package com.foxminded.university.dao;

import java.time.LocalDate;

import com.foxminded.university.model.Student;

public interface StudentDao extends GenericDao<Student> {

	Student findByFullNameAndBirthDate(String firstName, String lastName, LocalDate birthDate);
}
