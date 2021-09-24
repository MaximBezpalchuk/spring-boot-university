package com.foxminded.university.dao;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import com.foxminded.university.model.Audience;
import com.foxminded.university.model.Lecture;
import com.foxminded.university.model.LectureTime;
import com.foxminded.university.model.Teacher;

public interface LectureDao extends GenericDao<Lecture> {

	Optional<Lecture> findByAudienceDateAndLectureTime(Audience audience, LocalDate date, LectureTime lectureTime);

	List<Lecture> findLecturesByTeacherDateAndTime(Teacher teacher, LocalDate date, LectureTime time);
}
