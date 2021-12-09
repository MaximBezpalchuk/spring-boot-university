package com.foxminded.university;

import com.foxminded.university.formatter.Formatter;
import com.foxminded.university.model.*;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FormatterTest {
    private final Formatter formatter = new Formatter();
    private final Cathedra cathedra = Cathedra.builder().name("Fantastic Cathedra").build();

    @Test
    void formatLectureListTest() {
        List<Lecture> lectures = new ArrayList<>();
        Subject subject = Subject.builder().cathedra(cathedra).name("TestSubject").description("Desc").build();
        LocalDate date = LocalDate.of(2020, 1, 1);
        LectureTime time = LectureTime.builder().start(LocalTime.of(10, 20)).end(LocalTime.of(10, 21)).build();
        Audience audience = Audience.builder().room(1).capacity(5).cathedra(cathedra).build();
        Teacher teacher = Teacher.builder().firstName("Amigo").lastName("Bueno").address("Puerto Rico")
            .gender(Gender.FEMALE).birthDate(LocalDate.of(1999, 1, 1)).cathedra(cathedra).degree(Degree.PROFESSOR)
            .phone("999").email("dot@dot.com").postalCode("123").education("none").build();

        lectures.add(Lecture.builder().cathedra(cathedra).subject(subject).date(date).time(time).audience(audience)
            .teacher(teacher).build());

        assertEquals(
            "1.  Date: 2020-01-01 | Subject: TestSubject | Audience: 1 | Teacher: Amigo Bueno | Lecture start: 10:20, Lecture end: 10:21",
            formatter.formatLectureList(lectures));
    }

    @Test
    void formatGroupListTest() {
        List<Group> groups = new ArrayList<>();
        Group group = Group.builder().name("name").cathedra(cathedra).build();
        groups.add(group);
        assertEquals("1.  name", formatter.formatGroupList(groups));
    }

    @Test
    void formatSubjectListTest() {
        List<Subject> subjects = new ArrayList<>();
        Subject subject = Subject.builder().cathedra(cathedra).name("TestSubject").description("Desc").build();
        subjects.add(subject);
        assertEquals("1.  Name: TestSubject | Description: Desc", formatter.formatSubjectList(subjects));
    }

    @Test
    void formatTeacherListTest() {
        List<Teacher> teachers = new ArrayList<>();
        teachers.add(Teacher.builder().firstName("Amigo").lastName("Bueno").address("Puerto Rico").gender(Gender.FEMALE)
            .birthDate(LocalDate.of(1999, 1, 1)).cathedra(cathedra).degree(Degree.PROFESSOR).phone("999")
            .email("dot@dot.com").postalCode("123").education("none").build());
        assertEquals("1.  Name: Bueno Amigo", formatter.formatTeacherList(teachers));
    }

    @Test
    void formatAudienceListTest() {
        Audience audience1 = Audience.builder().room(3).capacity(5).cathedra(cathedra).build();
        Audience audience2 = Audience.builder().room(1).capacity(5).cathedra(cathedra).build();
        List<Audience> audiences = new ArrayList<>();
        audiences.add(audience1);
        audiences.add(audience2);
        assertEquals("1.  Audience: 3 | Capacity: 5" + System.lineSeparator() + "2.  Audience: 1 | Capacity: 5",
            formatter.formatAudienceList(audiences));
    }

    @Test
    void formatStudentListTest() {
        List<Student> students = new ArrayList<>();
        Group group = Group.builder().name("name").cathedra(cathedra).build();

        Student student = Student.builder().firstName("Amigo").lastName("Bueno").address("Puerto Rico")
            .gender(Gender.FEMALE).birthDate(LocalDate.of(1999, 1, 1)).phone("999").email("dot@dot.com")
            .postalCode("123").education("none").group(group).build();
        students.add(student);
        assertEquals("1.  Name: Bueno Amigo | Group: name", formatter.formatStudentList(students));
    }

    @Test
    void formatHolidayListTest() {
        List<Holiday> holidays = new ArrayList<>();
        holidays.add(Holiday.builder().name("Test").date(LocalDate.of(1999, 1, 1)).cathedra(cathedra).build());
        assertEquals("1.  Name: Test | Date: 1999-01-01", formatter.formatHolidayList(holidays));
    }

    @Test
    void formatVacationListTest() {
        List<Vacation> vacations = new ArrayList<>();
        vacations.add(
            Vacation.builder().start(LocalDate.of(1999, 1, 1)).end(LocalDate.of(2000, 1, 1)).teacher(null).build());
        assertEquals("1.  Dates: 1999-01-01 to 2000-01-01", formatter.formatVacationList(vacations));
    }

    @Test
    void formatLectureTimesListTest() {
        List<LectureTime> lectureTimes = new ArrayList<>();
        lectureTimes.add(LectureTime.builder().start(LocalTime.of(10, 20)).end(LocalTime.of(10, 21)).build());
        assertEquals("1.  Time: 10:20 to 10:21", formatter.formatLectureTimesList(lectureTimes));
    }

    @Test
    void getGenderStringTest() {
        assertEquals("MALE FEMALE ", formatter.getGenderString());
    }

    @Test
    void getDegreeStringTest() {
        assertEquals("ASSISTANT PROFESSOR UNKNOWN ", formatter.getDegreeString());
    }
}
