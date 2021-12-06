package com.foxminded.university.dao;

import java.time.LocalTime;
import java.util.Optional;

import com.foxminded.university.model.LectureTime;

public interface LectureTimeDao extends GenericDao<LectureTime> {

	Optional<LectureTime> findByPeriod(LocalTime start, LocalTime end);
}
