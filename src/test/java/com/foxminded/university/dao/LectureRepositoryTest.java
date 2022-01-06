package com.foxminded.university.dao;

import com.foxminded.university.model.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@Transactional
public class LectureRepositoryTest {

    @Autowired
    private EntityManager entityManager;
    @Autowired
    private LectureRepository lectureRepository;

    @Test
    void whenFindAll_thenAllExistingLecturesFound() {
        int expected = (int) (long) entityManager.createQuery("SELECT COUNT(l) FROM Lecture l").getSingleResult();
        List<Lecture> actual = lectureRepository.findAll();

        assertEquals(actual.size(), expected);
    }

    @Test
    void givenExistingLecture_whenFindById_thenLectureFound() {
        Cathedra cathedra = entityManager.find(Cathedra.class, 1);
        Audience audience = entityManager.find(Audience.class, 1);
        Group group = entityManager.find(Group.class, 1);
        Subject subject = entityManager.find(Subject.class, 1);
        Teacher teacher = entityManager.find(Teacher.class, 1);
        LectureTime time = entityManager.find(LectureTime.class, 1);
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
        Optional<Lecture> actual = lectureRepository.findById(1);

        assertEquals(expected, actual);
    }

    @Test
    void givenPageable_whenFindPaginatedLectures_thenLecturesFound() {
        Cathedra cathedra = entityManager.find(Cathedra.class, 1);
        Audience audience = entityManager.find(Audience.class, 1);
        Group group = entityManager.find(Group.class, 1);
        Subject subject = entityManager.find(Subject.class, 1);
        Teacher teacher = entityManager.find(Teacher.class, 1);
        LectureTime time = entityManager.find(LectureTime.class, 1);
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
        Page<Lecture> actual = lectureRepository.findAll(PageRequest.of(0, 1));

        assertEquals(expected, actual);
    }

    @Test
    void givenNotExistingLecture_whenFindById_thenReturnEmptyOptional() {
        assertEquals(lectureRepository.findById(100), Optional.empty());
    }

    @Test
    void givenNewLecture_whenSaveLecture_thenAllExistingLecturesFound() {
        Cathedra cathedra = entityManager.find(Cathedra.class, 1);
        Audience audience = entityManager.find(Audience.class, 1);
        Group group = entityManager.find(Group.class, 1);
        Subject subject = entityManager.find(Subject.class, 1);
        Teacher teacher = entityManager.find(Teacher.class, 2);
        LectureTime time = entityManager.find(LectureTime.class, 8);
        Lecture expected = Lecture.builder()
            .group(Arrays.asList(group))
            .cathedra(cathedra)
            .subject(subject)
            .date(LocalDate.of(2021, 4, 4))
            .time(time)
            .audience(audience)
            .teacher(teacher)
            .build();
        lectureRepository.save(expected);
        Lecture actual = entityManager.find(Lecture.class, 12);

        assertEquals(expected, actual);
    }

    @Test
    void givenExistingLecture_whenSaveWithChanges_thenChangesApplied() {
        Lecture expected = entityManager.find(Lecture.class, 1);
        expected.setDate(LocalDate.of(2021, 12, 12));
        lectureRepository.save(expected);
        Lecture actual = entityManager.find(Lecture.class, 1);

        assertEquals(expected, actual);
    }

    @Test
    void whenDeleteExistingLecture_thenLectureDeleted() {
        lectureRepository.delete(Lecture.builder().id(2).build());
        Lecture actual = entityManager.find(Lecture.class, 2);

        assertNull(actual);
    }

    @Test
    void givenAudienceAndDate_whenFindByAudienceAndDate_thenLectureFound() {
        Cathedra cathedra = entityManager.find(Cathedra.class, 1);
        Audience audience = entityManager.find(Audience.class, 1);
        Group group = entityManager.find(Group.class, 1);
        Subject subject = entityManager.find(Subject.class, 1);
        Teacher teacher = entityManager.find(Teacher.class, 1);
        LectureTime time = entityManager.find(LectureTime.class, 1);
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
        Optional<Lecture> actual = lectureRepository.findByAudienceAndDateAndTime(Audience.builder().id(1).build(),
            LocalDate.of(2021, 4, 4),
            LectureTime.builder().id(1).build());

        assertEquals(expected, actual);
    }

    @Test
    void givenAudienceDateAndLectureTime_whenFindByAudienceDateAndLectureTime_thenLectureFound() {
        Cathedra cathedra = entityManager.find(Cathedra.class, 1);
        Audience audience = entityManager.find(Audience.class, 1);
        Group group = entityManager.find(Group.class, 1);
        Subject subject = entityManager.find(Subject.class, 1);
        Teacher teacher = entityManager.find(Teacher.class, 1);
        LectureTime time = entityManager.find(LectureTime.class, 1);
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
        Optional<Lecture> actual = lectureRepository.findByAudienceAndDateAndTime(Audience.builder().id(1).build(),
            LocalDate.of(2021, 4, 4), LectureTime.builder().id(1).build());

        assertEquals(expected, actual);
    }

    @Test
    void givenStudentId_whenFindByStudentId_thenLecturesFound() {
        List<Lecture> actual = lectureRepository.findByGroupsContainingAndDateGreaterThanEqualAndDateLessThanEqual(Group.builder().id(1).build(),
            LocalDate.of(2021, 4, 4), LocalDate.of(2021, 4, 8));

        assertEquals(8, actual.size());
    }

    @Test
    void givenTeacherId_whenFindByTeacherId_thenLecturesFound() {
        List<Lecture> actual = lectureRepository.findByTeacherAndDateGreaterThanEqualAndDateLessThanEqual(Teacher.builder().id(2).build(),
            LocalDate.of(2021, 4, 4), LocalDate.of(2021, 4, 8));

        assertEquals(7, actual.size());
    }
}
