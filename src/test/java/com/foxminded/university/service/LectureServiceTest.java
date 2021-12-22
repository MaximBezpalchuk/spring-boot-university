package com.foxminded.university.service;

import com.foxminded.university.config.UniversityConfigProperties;
import com.foxminded.university.dao.HolidayDao;
import com.foxminded.university.dao.LectureDao;
import com.foxminded.university.dao.StudentDao;
import com.foxminded.university.dao.VacationDao;
import com.foxminded.university.exception.*;
import com.foxminded.university.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class LectureServiceTest {

    @Mock
    private LectureDao lectureDao;
    @Mock
    private VacationDao vacationDao;
    @Mock
    private HolidayDao holidayDao;
    @Mock
    private StudentDao studentDao;
    @Mock
    private StudentService studentService;
    @Mock
    private TeacherService teacherService;
    @Mock
    private UniversityConfigProperties universityConfig;
    @InjectMocks
    private LectureService lectureService;
    private int startWorkingDay = 6;
    private int endWorkingDay = 22;

    @BeforeEach
    void setWorkingDays(){
        when(universityConfig.getStartWorkingDay()).thenReturn(startWorkingDay);
        when(universityConfig.getEndWorkingDay()).thenReturn(endWorkingDay);
    }

    @Test
    void givenListOfLectures_whenFindAll_thenAllExistingLecturesFound() {
        Lecture lecture1 = Lecture.builder().id(1).build();
        List<Lecture> expected = Arrays.asList(lecture1);
        when(lectureDao.findAll()).thenReturn(expected);
        List<Lecture> actual = lectureService.findAll();

        assertEquals(expected, actual);
    }

    @Test
    void givenPageable_whenFindAll_thenAllPageableLecturesFound() {
        List<Lecture> lectures = Arrays.asList(Lecture.builder().id(1).build());
        Page<Lecture> expected = new PageImpl<>(lectures, PageRequest.of(0, 1), 1);
        when(lectureDao.findPaginatedLectures(isA(Pageable.class))).thenReturn(expected);
        Page<Lecture> actual = lectureService.findAll(PageRequest.of(0, 1));

        assertEquals(expected, actual);
    }

    @Test
    void givenExistingLecture_whenFindById_thenLectureFound() {
        Optional<Lecture> expected = Optional.of(Lecture.builder().id(1).build());
        when(lectureDao.findById(1)).thenReturn(expected);
        Lecture actual = lectureService.findById(1);

        assertEquals(expected.get(), actual);
    }

    @Test
    void givenExistingLecture_whenFindById_thenEntityNotFoundException() {
        when(lectureDao.findById(10)).thenReturn(Optional.empty());
        Exception exception = assertThrows(EntityNotFoundException.class, () -> {
            lectureService.findById(10);
        });

        assertEquals("Can`t find any lecture with id: 10", exception.getMessage());
    }

    @Test
    void givenNewLecture_whenSave_thenSaved() {
        Teacher teacher = Teacher.builder()
            .id(1)
            .subjects(Arrays.asList(Subject.builder().id(1).name("Test name").build()))
            .build();
        Lecture lecture = Lecture.builder()
            .audience(Audience.builder().capacity(10).build())
            .date(LocalDate.of(2021, 9, 8))
            .time(LectureTime.builder()
                .start(LocalTime.of(9, 0))
                .end(LocalTime.of(10, 0)).build())
            .teacher(teacher)
            .subject(Subject.builder().id(1).name("Test name").build()).build();
        lectureService.save(lecture);

        verify(lectureDao).save(lecture);
    }

    @Test
    void givenExistingLecture_whenSave_thenSaved() {
        Teacher teacher = Teacher.builder()
            .id(1)
            .subjects(Arrays.asList(Subject.builder().id(1).name("Test name").build()))
            .build();
        Audience audience = Audience.builder().id(1).capacity(100).build();
        LocalDate date = LocalDate.of(2021, 9, 8);
        LectureTime lectureTime = LectureTime.builder().start(LocalTime.of(9, 0)).end(LocalTime.of(10, 0)).build();
        Lecture lecture = Lecture.builder()
            .id(1)
            .date(date)
            .audience(audience)
            .time(lectureTime)
            .teacher(teacher)
            .subject(Subject.builder().id(1).name("Test name").build()).build();
        lectureService.save(lecture);

        verify(lectureDao).save(lecture);
    }

    @Test
    void givenNotUniqueLecture_whenSave_thenEntityNotUniqueException() {
        Cathedra cathedra = Cathedra.builder().id(1).name("Fantastic Cathedra").build();
        Teacher teacher = Teacher.builder()
            .id(1)
            .subjects(Arrays.asList(Subject.builder().id(1).cathedra(cathedra).build()))
            .build();
        Audience audience = Audience.builder().id(1).room(100).build();
        LocalDate date = LocalDate.of(2021, 9, 8);
        LectureTime lectureTime = LectureTime.builder().start(LocalTime.of(9, 0)).end(LocalTime.of(10, 0)).build();
        Lecture lecture1 = Lecture.builder()
            .id(1)
            .date(date)
            .audience(audience)
            .time(lectureTime)
            .teacher(teacher)
            .subject(Subject.builder().id(1).cathedra(cathedra).build()).build();
        Lecture lecture2 = Lecture.builder()
            .id(10)
            .date(date)
            .audience(audience)
            .time(lectureTime)
            .teacher(teacher)
            .subject(Subject.builder().id(1).cathedra(cathedra).build()).build();
        when(lectureDao.findByTeacherAudienceDateAndLectureTime(lecture1.getTeacher(), lecture1.getAudience(),
            lecture1.getDate(), lecture1.getTime())).thenReturn(Optional.of(lecture2));
        Exception exception = assertThrows(EntityNotUniqueException.class, () -> {
            lectureService.save(lecture1);
        });

        assertEquals(
            "Lecture with audience number 100, date 2021-09-08 and lecture time 09:00 - 10:00 is already exists!",
            exception.getMessage());
    }

    @Test
    void givenLectureInSunday_whenSave_thenLectureOnSundayException() {
        Lecture lecture = Lecture.builder()
            .id(1)
            .date(LocalDate.of(2021, 9, 5))
            .build();
        Exception exception = assertThrows(SundayException.class, () -> {
            lectureService.save(lecture);
        });

        assertEquals("Lecture can`t be on sunday! Specified date is: 2021-09-05", exception.getMessage());
    }

    @Test
    void givenLectureInNotSupportedTime_whenSave_thenLectureInAfterHoursException() {
        Lecture lecture = Lecture.builder()
            .id(1)
            .date(LocalDate.of(2021, 9, 6))
            .time(LectureTime.builder().id(1).start(LocalTime.of(5, 0)).end(LocalTime.of(10, 0)).build())
            .build();
        Exception exception = assertThrows(AfterHoursException.class, () -> {
            lectureService.save(lecture);
        });

        assertEquals("Lecture can`t be in after hours! Specified period is: 05:00 - 10:00", exception.getMessage());
    }

    @Test
    void givenLectureWithBusyTeacher_whenSave_thenLectureWithBusyTeacherException() {
        Lecture lecture = Lecture.builder()
            .id(1)
            .date(LocalDate.of(2021, 9, 6))
            .time(LectureTime.builder().id(1).start(LocalTime.of(9, 0)).end(LocalTime.of(10, 0)).build())
            .teacher(Teacher.builder().firstName("TestFirstName").lastName("TestLastName").build())
            .build();
        Lecture lecture2 = Lecture.builder()
            .id(2)
            .build();
        when(lectureDao.findLecturesByTeacherDateAndTime(lecture.getTeacher(), lecture.getDate(), lecture.getTime()))
            .thenReturn(Arrays.asList(lecture2));
        Exception exception = assertThrows(BusyTeacherException.class, () -> {
            lectureService.save(lecture);
        });

        assertEquals("Teacher is on another lecture this time! Teacher is: TestFirstName TestLastName",
            exception.getMessage());
    }

    @Test
    void givenLectureWithTeacherHaveVacation_whenSave_thenLectureWithTeacherInVacationException() {
        Lecture lecture = Lecture.builder()
            .id(1)
            .date(LocalDate.of(2021, 9, 6))
            .time(LectureTime.builder().id(1).start(LocalTime.of(9, 0)).end(LocalTime.of(10, 0)).build())
            .teacher(Teacher.builder().id(1).build())
            .build();
        Vacation vacation = Vacation.builder().start(LocalDate.of(2021, 9, 5)).end(LocalDate.of(2021, 9, 7)).build();
        when(vacationDao.findByDateInPeriodAndTeacher(lecture.getDate(), lecture.getTeacher()))
            .thenReturn(Arrays.asList(vacation));
        Exception exception = assertThrows(TeacherInVacationException.class, () -> {
            lectureService.save(lecture);
        });

        assertEquals("Teacher is in vacation this date! Date is: 2021-09-06", exception.getMessage());
    }

    @Test
    void givenLectureInHoliday_whenSave_thenLectureOnHolidayException() {
        Lecture lecture = Lecture.builder()
            .id(1)
            .date(LocalDate.of(2021, 9, 6))
            .time(LectureTime.builder().id(1).start(LocalTime.of(9, 0)).end(LocalTime.of(10, 0)).build())
            .teacher(Teacher.builder().id(1).build())
            .build();
        when(holidayDao.findByDate(lecture.getDate()))
            .thenReturn(Arrays.asList(Holiday.builder().date(LocalDate.of(2021, 9, 6)).name("Test").build()));
        Exception exception = assertThrows(HolidayException.class, () -> {
            lectureService.save(lecture);
        });

        assertEquals("Lecture can`t be on holiday! Date is: 2021-09-06", exception.getMessage());
    }

    @Test
    void givenLectureWithTeacherWhoDidntKnowSubject_whenSave_thenLectureWithNotCompetentTeacherException() {
        Lecture lecture = Lecture.builder()
            .id(1)
            .date(LocalDate.of(2021, 9, 6))
            .time(LectureTime.builder().id(1).start(LocalTime.of(9, 0)).end(LocalTime.of(10, 0)).build())
            .teacher(Teacher.builder().id(1).firstName("TestFirstName").lastName("TestLastName").build())
            .subject(Subject.builder().build())
            .build();
        Exception exception = assertThrows(NotCompetentTeacherException.class, () -> {
            lectureService.save(lecture);
        });

        assertEquals("Teacher TestFirstName TestLastName can`t educatenull!", exception.getMessage());
    }

    @Test
    void givenLectureWithMoreStudentsThenAudienceCapacity_whenSave_thenLectureInSmallAudienceException() {
        Subject subject = Subject.builder().name("Test name").build();
        Group group = Group.builder().id(1).build();
        Lecture lecture = Lecture.builder()
            .id(1)
            .date(LocalDate.of(2021, 9, 6))
            .time(LectureTime.builder().id(1).start(LocalTime.of(9, 0)).end(LocalTime.of(10, 0)).build())
            .teacher(Teacher.builder().id(1).subjects(Arrays.asList(subject)).build())
            .subject(subject)
            .group(Arrays.asList(group))
            .audience(Audience.builder().capacity(1).build())
            .build();
        when(studentDao.findByGroupId(1))
            .thenReturn(Arrays.asList(Student.builder().build(), Student.builder().build()));
        Exception exception = assertThrows(AudienceOverflowException.class, () -> {
            lectureService.save(lecture);
        });

        assertEquals("Student count (2) more than audience capacity (1)!", exception.getMessage());
    }

    @Test
    void givenLectureWithAudienceInUse_whenSave_thenLectureInOccupiedAudienceException() {
        Subject subject = Subject.builder().name("Test name").build();
        Lecture lecture = Lecture.builder()
            .id(1)
            .date(LocalDate.of(2021, 9, 6))
            .time(LectureTime.builder().id(1).start(LocalTime.of(9, 0)).end(LocalTime.of(10, 0)).build())
            .teacher(Teacher.builder().id(1).subjects(Arrays.asList(subject)).build())
            .subject(subject)
            .audience(Audience.builder().room(100).capacity(1).build())
            .build();
        Lecture lecture2 = Lecture.builder().id(3).build();
        when(lectureDao.findByAudienceDateAndLectureTime(lecture.getAudience(), lecture.getDate(), lecture.getTime()))
            .thenReturn(Optional.of(lecture2));
        Exception exception = assertThrows(OccupiedAudienceException.class, () -> {
            lectureService.save(lecture);
        });

        assertEquals("Audience 100 is already occupied!", exception.getMessage());
    }

    @Test
    void givenExistingLectureId_whenDelete_thenDeleted() {
        Lecture lecture = Lecture.builder().id(1).build();
        lectureService.delete(lecture);

        verify(lectureDao).delete(lecture);
    }

    @Test
    void givenStudentId_whenFindLecturesByStudentId_thenAllExistingLecturesFound() {
        Lecture lecture1 = Lecture.builder().id(1).build();
        List<Lecture> expected = Arrays.asList(lecture1);
        Student student = Student.builder().id(1).build();
        when(studentService.findById(1)).thenReturn(student);
        when(lectureDao.findLecturesByStudentAndPeriod(student, LocalDate.of(2021, 4, 4), LocalDate.of(2021, 4, 8)))
            .thenReturn(expected);
        List<Lecture> actual = lectureService.findByStudentIdAndPeriod(1, LocalDate.of(2021, 4, 4),
            LocalDate.of(2021, 4, 8));

        assertEquals(expected, actual);
    }

    @Test
    void givenTeacherId_whenFindLecturesByTeacherId_thenAllExistingLecturesFound() {
        Lecture lecture1 = Lecture.builder().id(1).build();
        List<Lecture> expected = Arrays.asList(lecture1);
        Teacher teacher = Teacher.builder().id(1).build();
        when(teacherService.findById(1)).thenReturn(teacher);
        when(lectureDao.findLecturesByTeacherAndPeriod(teacher, LocalDate.of(2021, 4, 4), LocalDate.of(2021, 4, 8)))
            .thenReturn(expected);
        List<Lecture> actual = lectureService.findByTeacherIdAndPeriod(1, LocalDate.of(2021, 4, 4),
            LocalDate.of(2021, 4, 8));

        assertEquals(expected, actual);
    }
}
