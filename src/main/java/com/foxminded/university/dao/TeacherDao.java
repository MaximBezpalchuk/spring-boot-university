package com.foxminded.university.dao;

import com.foxminded.university.model.LectureTime;
import com.foxminded.university.model.Subject;
import com.foxminded.university.model.Teacher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface TeacherDao extends GenericDao<Teacher> {

    Optional<Teacher> findByFullNameAndBirthDate(String firstName, String lastName, LocalDate birthDate);

    Page<Teacher> findPaginatedTeachers(Pageable pageable);

    List<Teacher> findByFreeDateAndSubjectWithCurrentTeacher(LocalDate date, LectureTime time, Subject subject);
}
