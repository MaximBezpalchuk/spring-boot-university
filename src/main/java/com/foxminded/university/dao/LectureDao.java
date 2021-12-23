package com.foxminded.university.dao;

import com.foxminded.university.model.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface LectureDao extends JpaRepository<Lecture, Integer> {

    Optional<Lecture> findByAudienceDateAndLectureTime(Audience audience, LocalDate date, LectureTime lectureTime);

    Optional<Lecture> findByTeacherAudienceDateAndLectureTime(Teacher teacher, Audience audience, LocalDate date,
                                                              LectureTime lectureTime);

    List<Lecture> findLecturesByTeacherDateAndTime(Teacher teacher, LocalDate date, LectureTime time);

    Page<Lecture> findAll(Pageable pageable);

    @Query(nativeQuery = true)
    List<Lecture> findLecturesByStudentAndPeriod(Student student, LocalDate start, LocalDate end);

    List<Lecture> findLecturesByTeacherAndPeriod(Teacher teacher, LocalDate start, LocalDate end);
}
