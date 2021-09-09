package com.foxminded.university.dao;

import java.time.LocalDate;
import java.util.List;

import com.foxminded.university.model.Student;

public interface StudentDao extends GenericDao<Student> {

	Student findByFullNameAndBirthDate(String firstName, String lastName, LocalDate birthDate);

	List<Student> findByGroupName(String groupName);
}
