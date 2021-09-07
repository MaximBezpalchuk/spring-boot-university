package com.foxminded.university.dao;

import java.util.List;

import com.foxminded.university.model.Audience;
import com.foxminded.university.model.Lecture;
import com.foxminded.university.model.LectureTime;
import com.foxminded.university.model.Teacher;

public interface LectureDao extends GenericDao<Lecture> {

	Lecture findByAudienceAndLectureTime(Audience audience, LectureTime lectureTime);

	List<Lecture> findLecturesByTeacher(Teacher teacher);
}
