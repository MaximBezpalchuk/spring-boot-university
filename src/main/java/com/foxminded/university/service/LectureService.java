package com.foxminded.university.service;

import java.time.DayOfWeek;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.foxminded.university.dao.LectureDao;
import com.foxminded.university.dao.jdbc.JdbcHolidayDao;
import com.foxminded.university.dao.jdbc.JdbcLectureDao;
import com.foxminded.university.dao.jdbc.JdbcVacationDao;
import com.foxminded.university.model.Holiday;
import com.foxminded.university.model.Lecture;
import com.foxminded.university.model.Student;
import com.foxminded.university.model.Vacation;

@Service
public class LectureService {

	private LectureDao lectureDao;
	private JdbcVacationDao vacationDao;
	private JdbcHolidayDao holidayDao;

	public LectureService(JdbcLectureDao lectureDao, JdbcVacationDao vacationDao, JdbcHolidayDao holidayDao) {
		this.lectureDao = lectureDao;
	}

	public List<Lecture> findAll() {
		return lectureDao.findAll();
	}

	public Lecture findById(int id) {
		return lectureDao.findById(id);
	}

	public String save(Lecture lecture) {
		Lecture existingLecture = lectureDao.findByAudienceAndLectureTime(lecture.getAudience(), lecture.getTime());

		if (lecture.getDate().getDayOfWeek().equals(DayOfWeek.SUNDAY)) {
			return "Lecture cant be in sunday";
		} else if (lecture.getTime().getEnd().getHour() > 22 && lecture.getTime().getStart().getHour() < 8) {
			return "Lecture must start after 8 and end before 22";
		}

		List<Lecture> lecturesWithSameTeacher = lectureDao.findLecturesByTeacher(lecture.getTeacher());
		if (!lecturesWithSameTeacher.isEmpty()) {
			Lecture lectureInSameTime = lecturesWithSameTeacher.stream()
					.filter(lec -> lec.getDate().isEqual(lecture.getDate()))
					.filter(lec -> lec.getTime().equals(lecture.getTime())).findAny().orElse(null);
			if (lectureInSameTime != null) {
				return "Teacher is on other lecture in this time";
			}
		}
		
		List<Vacation> teacherVacations = vacationDao.findByTeacherId(lecture.getTeacher().getId());
		if (!teacherVacations.isEmpty()) {
			Vacation vacation = teacherVacations.stream().filter(
					vac -> (!(lecture.getDate().isBefore(vac.getStart()) || lecture.getDate().isAfter(vac.getEnd()))))
					.findAny().orElse(null);
			if (vacation != null) {
				return "Teacher have vacation in this time";
			}
		}

		List<Holiday> holidays = holidayDao.findAll();
		if (!holidays.isEmpty()) {
			Holiday holiday = holidays.stream().filter(hol -> lecture.getDate().equals(hol.getDate())).findAny().orElse(null);
			if (holiday != null) {
				return "It is holiday time: " + holiday.getName();
			}
		}
		
		if(!lecture.getTeacher().getSubjects().equals(lecture.getSubject())) {
			return "Teacher can`t teach this subject";
		}
		
		//TODO: get student count to check audience size
		
		List<Student> studentsOnLecture = lecture.getGroups().stream().forEach(group -> group.getId());;

		
		lectureDao.save(lecture);
		return "";
	}

	public void deleteById(int id) {
		lectureDao.deleteById(id);
	}
}
