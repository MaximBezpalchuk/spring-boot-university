package com.foxminded.university.dao.hibernate;

import com.foxminded.university.dao.LectureDao;
import com.foxminded.university.model.*;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@Transactional
public class HibernateLectureDaoTest {

    @Autowired
    private SessionFactory sessionFactory;
    @Autowired
    private LectureDao lectureDao;

    @Test
    void whenFindAll_thenAllExistingLecturesFound() {
        int expected = (int) (long) sessionFactory.getCurrentSession().createQuery("SELECT COUNT(l) FROM Lecture l").getSingleResult();
        List<Lecture> actual = lectureDao.findAll();

        assertEquals(actual.size(), expected);
    }

    @Test
    void givenExistingLecture_whenFindById_thenLectureFound() {
        Cathedra cathedra = sessionFactory.getCurrentSession().get(Cathedra.class, 1);
        Audience audience = sessionFactory.getCurrentSession().get(Audience.class, 1);
        Group group = sessionFactory.getCurrentSession().get(Group.class, 1);
        Subject subject = sessionFactory.getCurrentSession().get(Subject.class, 1);
        Teacher teacher = sessionFactory.getCurrentSession().get(Teacher.class, 1);
        LectureTime time = sessionFactory.getCurrentSession().get(LectureTime.class, 1);
        Optional<Lecture> expected = Optional.of(Lecture.builder()
            .id(1)
            .group(Arrays.asList(group))
            .cathedra(cathedra)
            .subject(subject)
            .date(LocalDate.of(2021, 4, 4))
            .time(time)
            .audience(audience)
            .teacher(teacher)
            .build());
        Optional<Lecture> actual = lectureDao.findById(1);

        assertEquals(expected, actual);
    }

    @Test
    void givenPageable_whenFindPaginatedLectures_thenLecturesFound() {
        Cathedra cathedra = sessionFactory.getCurrentSession().get(Cathedra.class, 1);
        Audience audience = sessionFactory.getCurrentSession().get(Audience.class, 1);
        Group group = sessionFactory.getCurrentSession().get(Group.class, 1);
        Subject subject = sessionFactory.getCurrentSession().get(Subject.class, 1);
        Teacher teacher = sessionFactory.getCurrentSession().get(Teacher.class, 1);
        LectureTime time = sessionFactory.getCurrentSession().get(LectureTime.class, 1);
        List<Lecture> lectures = Arrays.asList(Lecture.builder()
            .id(1)
            .group(Arrays.asList(group))
            .cathedra(cathedra)
            .subject(subject)
            .date(LocalDate.of(2021, 4, 4))
            .time(time)
            .audience(audience)
            .teacher(teacher)
            .build());
        Page<Lecture> expected = new PageImpl<>(lectures, PageRequest.of(0, 1), 11);
        Page<Lecture> actual = lectureDao.findAll(PageRequest.of(0, 1));

        assertEquals(expected, actual);
    }

    @Test
    void givenNotExistingLecture_whenFindById_thenReturnEmptyOptional() {
        assertEquals(lectureDao.findById(100), Optional.empty());
    }

    @Test
    void givenNewLecture_whenSaveLecture_thenAllExistingLecturesFound() {
        Cathedra cathedra = sessionFactory.getCurrentSession().get(Cathedra.class, 1);
        Audience audience = sessionFactory.getCurrentSession().get(Audience.class, 1);
        Group group = sessionFactory.getCurrentSession().get(Group.class, 1);
        Subject subject = sessionFactory.getCurrentSession().get(Subject.class, 1);
        Teacher teacher = sessionFactory.getCurrentSession().get(Teacher.class, 2);
        LectureTime time = sessionFactory.getCurrentSession().get(LectureTime.class, 8);
        Lecture expected = Lecture.builder()
            .group(Arrays.asList(group))
            .cathedra(cathedra)
            .subject(subject)
            .date(LocalDate.of(2021, 4, 4))
            .time(time)
            .audience(audience)
            .teacher(teacher)
            .build();
        lectureDao.save(expected);
        Lecture actual = sessionFactory.getCurrentSession().get(Lecture.class, 12);

        assertEquals(expected, actual);
    }

    @Test
    void givenExistingLecture_whenSaveWithChanges_thenChangesApplied() {
        Lecture expected = sessionFactory.getCurrentSession().get(Lecture.class, 1);
        expected.setDate(LocalDate.of(2021, 12, 12));
        lectureDao.save(expected);
        Lecture actual = sessionFactory.getCurrentSession().get(Lecture.class, 1);

        assertEquals(expected, actual);
    }

    @Test
    void whenDeleteExistingLecture_thenLectureDeleted() {
        lectureDao.delete(Lecture.builder().id(2).build());
        Lecture actual = sessionFactory.getCurrentSession().get(Lecture.class, 2);

        assertNull(actual);
    }

    @Test
    void givenAudienceAndDate_whenFindByAudienceAndDate_thenLectureFound() {
        Cathedra cathedra = sessionFactory.getCurrentSession().get(Cathedra.class, 1);
        Audience audience = sessionFactory.getCurrentSession().get(Audience.class, 1);
        Group group = sessionFactory.getCurrentSession().get(Group.class, 1);
        Subject subject = sessionFactory.getCurrentSession().get(Subject.class, 1);
        Teacher teacher = sessionFactory.getCurrentSession().get(Teacher.class, 1);
        LectureTime time = sessionFactory.getCurrentSession().get(LectureTime.class, 1);
        Optional<Lecture> expected = Optional.of(Lecture.builder()
            .id(1)
            .group(Arrays.asList(group))
            .cathedra(cathedra)
            .subject(subject)
            .date(LocalDate.of(2021, 4, 4))
            .time(time)
            .audience(audience)
            .teacher(teacher)
            .build());
        Optional<Lecture> actual = lectureDao.findByAudienceDateAndLectureTime(Audience.builder().id(1).build(),
            LocalDate.of(2021, 4, 4),
            LectureTime.builder().id(1).build());

        assertEquals(expected, actual);
    }

    @Test
    void givenAudienceDateAndLectureTime_whenFindByAudienceDateAndLectureTime_thenLectureFound() {
        Cathedra cathedra = sessionFactory.getCurrentSession().get(Cathedra.class, 1);
        Audience audience = sessionFactory.getCurrentSession().get(Audience.class, 1);
        Group group = sessionFactory.getCurrentSession().get(Group.class, 1);
        Subject subject = sessionFactory.getCurrentSession().get(Subject.class, 1);
        Teacher teacher = sessionFactory.getCurrentSession().get(Teacher.class, 1);
        LectureTime time = sessionFactory.getCurrentSession().get(LectureTime.class, 1);
        Optional<Lecture> expected = Optional.of(Lecture.builder()
            .id(1)
            .group(Arrays.asList(group))
            .cathedra(cathedra)
            .subject(subject)
            .date(LocalDate.of(2021, 4, 4))
            .time(time)
            .audience(audience)
            .teacher(teacher)
            .build());
        Optional<Lecture> actual = lectureDao.findByAudienceDateAndLectureTime(Audience.builder().id(1).build(),
            LocalDate.of(2021, 4, 4), LectureTime.builder().id(1).build());

        assertEquals(expected, actual);
    }

    @Test
    void givenStudentId_whenFindByStudentId_thenLecturesFound() {
        List<Lecture> actual = lectureDao.findLecturesByStudentAndPeriod(Student.builder().id(1).build(),
            LocalDate.of(2021, 4, 4), LocalDate.of(2021, 4, 8));

        assertEquals(8, actual.size());
    }

    @Test
    void givenTeacherId_whenFindByTeacherId_thenLecturesFound() {
        List<Lecture> actual = lectureDao.findLecturesByTeacherAndPeriod(Teacher.builder().id(2).build(),
            LocalDate.of(2021, 4, 4), LocalDate.of(2021, 4, 8));

        assertEquals(7, actual.size());
    }
}
