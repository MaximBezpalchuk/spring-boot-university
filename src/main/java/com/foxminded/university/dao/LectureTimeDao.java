package com.foxminded.university.dao;

import com.foxminded.university.model.LectureTime;

import java.time.LocalTime;
import java.util.Optional;

public interface LectureTimeDao extends GenericDao<LectureTime> {

    Optional<LectureTime> findByPeriod(LocalTime start, LocalTime end);
}
