package com.foxminded.university.dao;

import com.foxminded.university.model.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface LectureRepository extends JpaRepository<Lecture, Integer> {

    Optional<Lecture> findByAudienceDateAndLectureTime(@Param("audience") Audience audience, @Param("date") LocalDate date, @Param("time") LectureTime time);

    Optional<Lecture> findByTeacherAudienceDateAndLectureTime(@Param("teacher") Teacher teacher, @Param("audience") Audience audience, @Param("date") LocalDate date, @Param("time") LectureTime lectureTime);

    List<Lecture> findLecturesByTeacherDateAndTime(@Param("teacher") Teacher teacher, @Param("date") LocalDate date, @Param("time") LectureTime time);

    Page<Lecture> findAll(Pageable pageable);

    List<Lecture> findLecturesByGroupAndPeriod(@Param("group") Group group, @Param("start") LocalDate start, @Param("end") LocalDate end);

    List<Lecture> findLecturesByTeacherAndPeriod(@Param("teacher") Teacher teacher, @Param("start") LocalDate start, @Param("end") LocalDate end);
}
