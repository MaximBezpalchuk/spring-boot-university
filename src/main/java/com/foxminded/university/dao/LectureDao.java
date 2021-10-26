package com.foxminded.university.dao;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.foxminded.university.model.Audience;
import com.foxminded.university.model.Lecture;
import com.foxminded.university.model.LectureTime;
import com.foxminded.university.model.Teacher;

public interface LectureDao extends GenericDao<Lecture> {

	Optional<Lecture> findByAudienceDateAndLectureTime(Audience audience, LocalDate date, LectureTime lectureTime);

	Optional<Lecture> findByTeacherAudienceDateAndLectureTime(Teacher teacher, Audience audience, LocalDate date,
			LectureTime lectureTime);

	List<Lecture> findLecturesByTeacherDateAndTime(Teacher teacher, LocalDate date, LectureTime time);

	Page<Lecture> findPaginatedLectures(Pageable pageable);

	List<Lecture> findLecturesByStudentId(int id);

	List<Lecture> findLecturesByTeacherId(int id);
}
