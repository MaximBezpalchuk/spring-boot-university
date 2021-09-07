package com.foxminded.university.dao;

import java.time.LocalTime;

import com.foxminded.university.model.LectureTime;

public interface LectureTimeDao extends GenericDao<LectureTime> {

	LectureTime findByPeriod(LocalTime start, LocalTime end);
}
