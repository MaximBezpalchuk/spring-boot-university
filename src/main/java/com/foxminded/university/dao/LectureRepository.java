package com.foxminded.university.dao;

import com.foxminded.university.model.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface LectureRepository extends JpaRepository<Lecture, Integer> {

    Optional<Lecture> findByAudienceAndDateAndTime(Audience audience, LocalDate date, LectureTime time);

    Optional<Lecture> findByTeacherAndAudienceAndDateAndTime(Teacher teacher, Audience audience, LocalDate date, LectureTime time);

    List<Lecture> findLecturesByTeacherAndDateAndTime(Teacher teacher, LocalDate date, LectureTime time);

    Page<Lecture> findAll(Pageable pageable);

    List<Lecture> findByGroupsContainingAndDateGreaterThanEqualAndDateLessThanEqual(Group group, LocalDate start, LocalDate end);

    List<Lecture> findByTeacherAndDateGreaterThanEqualAndDateLessThanEqual(Teacher teacher, LocalDate start, LocalDate end);
}
