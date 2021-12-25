package com.foxminded.university.service;

import com.foxminded.university.config.UniversityConfigProperties;
import com.foxminded.university.dao.HolidayRepository;
import com.foxminded.university.dao.LectureRepository;
import com.foxminded.university.dao.StudentRepository;
import com.foxminded.university.dao.VacationRepository;
import com.foxminded.university.exception.*;
import com.foxminded.university.model.Group;
import com.foxminded.university.model.Lecture;
import com.foxminded.university.model.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class LectureService {

    private static final Logger logger = LoggerFactory.getLogger(LectureService.class);

    private final LectureRepository lectureRepository;
    private final VacationRepository vacationRepository;
    private final HolidayRepository holidayRepository;
    private final StudentRepository studentRepository;
    private final StudentService studentService;
    private final TeacherService teacherService;
    private UniversityConfigProperties universityConfig;

    public LectureService(LectureRepository lectureRepository, VacationRepository vacationRepository, HolidayRepository holidayRepository,
                          StudentRepository studentRepository, StudentService studentService, TeacherService teacherService, UniversityConfigProperties universityConfig) {
        this.lectureRepository = lectureRepository;
        this.vacationRepository = vacationRepository;
        this.holidayRepository = holidayRepository;
        this.studentRepository = studentRepository;
        this.studentService = studentService;
        this.teacherService = teacherService;
        this.universityConfig = universityConfig;
    }

    public List<Lecture> findAll() {
        logger.debug("Find all lectures");
        return lectureRepository.findAll();
    }

    public Page<Lecture> findAll(final Pageable pageable) {
        logger.debug("Find all lectures paginated");
        return lectureRepository.findAll(pageable);
    }

    public Lecture findById(int id) {
        logger.debug("Find lecture by id {}", id);
        return lectureRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Can`t find any lecture with id: " + id));
    }

    public void save(Lecture lecture) {
        logger.debug("Save lecture");
        uniqueCheck(lecture);
        sundayCheck(lecture);
        afterHoursCheck(lecture);
        teacherBusyCheck(lecture);
        teacherInVacationCheck(lecture);
        holidayCheck(lecture);
        teacherCompetentWithSubjectCheck(lecture);
        enoughAudienceCapacityCheck(lecture);
        audienceOccupiedCheck(lecture);
        lectureRepository.save(lecture);
    }

    public void delete(Lecture lecture) {
        logger.debug("Delete lecture with id: {}", lecture.getId());
        lectureRepository.delete(lecture);
    }

    private void uniqueCheck(Lecture lecture) {
        logger.debug("Check lecture is unique");
        Optional<Lecture> existingLecture = lectureRepository.findByTeacherAndAudienceAndDateAndTime(lecture.getTeacher(),
            lecture.getAudience(), lecture.getDate(), lecture.getTime());
        if (existingLecture.isPresent() && (existingLecture.get().getId() != lecture.getId())) {
            throw new EntityNotUniqueException("Lecture with audience number " + lecture.getAudience().getRoom()
                + ", date " + lecture.getDate() + " and lecture time " + lecture.getTime().getStart() + " - "
                + lecture.getTime().getEnd() + " is already exists!");
        }
    }

    private void sundayCheck(Lecture lecture) {
        logger.debug("Check lecture is on sunday");
        if (lecture.getDate().getDayOfWeek().equals(DayOfWeek.SUNDAY)) {
            throw new SundayException("Lecture can`t be on sunday! Specified date is: " + lecture.getDate());
        }
    }

    private void afterHoursCheck(Lecture lecture) {
        logger.debug("Check lecture is after hours");
        if (lecture.getTime().getEnd().getHour() > universityConfig.getEndWorkingDay()
            || lecture.getTime().getStart().getHour() < universityConfig.getStartWorkingDay()) {
            throw new AfterHoursException("Lecture can`t be in after hours! Specified period is: "
                + lecture.getTime().getStart() + " - " + lecture.getTime().getEnd());
        }
    }

    private void teacherBusyCheck(Lecture lecture) {
        logger.debug("Check teacher for this lecture is busy");
        if (lectureRepository.findLecturesByTeacherAndDateAndTime(lecture.getTeacher(), lecture.getDate(), lecture.getTime())
            .stream().filter(lec -> lec.getId() != lecture.getId()).findAny().isPresent()) {
            throw new BusyTeacherException("Teacher is on another lecture this time! Teacher is: "
                + lecture.getTeacher().getFirstName() + " " + lecture.getTeacher().getLastName());
        }
    }

    private void teacherInVacationCheck(Lecture lecture) {
        logger.debug("Check teacher for this lecture is in vacation");
        if (!vacationRepository.findByDateInPeriodAndTeacher(lecture.getDate(), lecture.getTeacher()).isEmpty()) {
            throw new TeacherInVacationException("Teacher is in vacation this date! Date is: " + lecture.getDate());
        }
    }

    private void holidayCheck(Lecture lecture) {
        logger.debug("Check lecture is in holiday time");
        if (!holidayRepository.findByDate(lecture.getDate()).isEmpty()) {
            throw new HolidayException("Lecture can`t be on holiday! Date is: " + lecture.getDate());
        }
    }

    private void teacherCompetentWithSubjectCheck(Lecture lecture) {
        logger.debug("Check teacher for subject");
        if (!lecture.getTeacher().getSubjects().stream().map(Subject::getName).anyMatch(name -> lecture.getSubject().getName().equals(name))) {
            throw new NotCompetentTeacherException("Teacher " + lecture.getTeacher().getFirstName() + " "
                + lecture.getTeacher().getLastName() + " can`t educate" + lecture.getSubject().getName() + "!");
        }
    }

    private void enoughAudienceCapacityCheck(Lecture lecture) {
        logger.debug("Check audience size");
        Integer studentsOnLectureCount = lecture.getGroups().stream().map(Group::getId).map(studentRepository::findByGroupId)
            .mapToInt(List::size).sum();
        if (studentsOnLectureCount >= lecture.getAudience().getCapacity()) {
            throw new AudienceOverflowException("Student count (" + studentsOnLectureCount
                + ") more than audience capacity (" + lecture.getAudience().getCapacity() + ")!");
        }
    }

    private void audienceOccupiedCheck(Lecture lecture) {
        logger.debug("Check audience is occupied");
        Optional<Lecture> existingLecture = lectureRepository.findByAudienceAndDateAndTime(lecture.getAudience(),
            lecture.getDate(), lecture.getTime());
        if (existingLecture.isPresent() && existingLecture.get().getId() != lecture.getId()) {
            throw new OccupiedAudienceException(
                "Audience " + lecture.getAudience().getRoom() + " is already occupied!");
        }
    }

    public List<Lecture> findByStudentIdAndPeriod(int id, LocalDate start, LocalDate end) {
        logger.debug("Find all lectures by student id and period");
        return lectureRepository.findByGroupsContainingAndDateGreaterThanEqualAndDateLessThanEqual(studentService.findById(id).getGroup(), start, end);
    }

    public List<Lecture> findByTeacherIdAndPeriod(int id, LocalDate start, LocalDate end) {
        logger.debug("Find all lectures by teacher and period");
        return lectureRepository.findByTeacherAndDateGreaterThanEqualAndDateLessThanEqual(teacherService.findById(id), start, end);
    }
}
