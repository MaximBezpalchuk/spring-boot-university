package com.foxminded.university.dao;

import com.foxminded.university.model.LectureTime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalTime;
import java.util.Optional;

@Repository
public interface LectureTimeRepository extends JpaRepository<LectureTime, Integer> {

    Optional<LectureTime> findByStartAndEnd(LocalTime start, LocalTime end);
}
