package com.foxminded.university.dao;

import com.foxminded.university.model.LectureTime;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@Transactional
public class LectureTimeRepositoryTest {

    @Autowired
    private EntityManager entityManager;
    @Autowired
    private LectureTimeRepository lectureTimeRepository;

    @Test
    void whenFindAll_thenAllExistingLectureTimesFound() {
        int expected = (int) (long) entityManager.createQuery("SELECT COUNT(lt) FROM LectureTime lt").getSingleResult();
        List<LectureTime> actual = lectureTimeRepository.findAll();

        assertEquals(actual.size(), expected);
    }

    @Test
    void givenExistingLectureTime_whenFindById_thenLectureTimeFound() {
        Optional<LectureTime> expected = Optional.of(LectureTime.builder()
            .id(1).start(LocalTime.of(8, 0, 0))
            .end(LocalTime.of(9, 30, 0))
            .build());
        Optional<LectureTime> actual = lectureTimeRepository.findById(1);

        assertEquals(expected, actual);
    }

    @Test
    void givenNotExistingLectureTime_whenFindById_thenReturnEmptyOptional() {
        assertEquals(lectureTimeRepository.findById(100), Optional.empty());
    }

    @Test
    void givenNewLectureTime_whenSaveLectureTime_thenAllExistingLectureTimesFound() {
        LectureTime expected = LectureTime.builder()
            .start(LocalTime.of(21, 0, 0))
            .end(LocalTime.of(22, 30, 0))
            .build();
        lectureTimeRepository.save(expected);
        LectureTime actual = entityManager.find(LectureTime.class, 9);

        assertEquals(expected, actual);
    }

    @Test
    void givenExistingLectureTime_whenSaveWithChanges_thenChangesApplied() {
        LectureTime expected = entityManager.find(LectureTime.class, 1);
        expected.setStart(LocalTime.of(8, 23));
        lectureTimeRepository.save(expected);
        LectureTime actual = entityManager.find(LectureTime.class, 1);

        assertEquals(expected, actual);
    }

    @Test
    void whenDeleteExistingLectureTime_thenLectureTimeDeleted() {
        lectureTimeRepository.delete(LectureTime.builder().id(2).build());
        LectureTime actual = entityManager.find(LectureTime.class, 2);

        assertNull(actual);
    }

    @Test
    void givenPeriod_whenFindByPeriod_thenLectureTimeFound() {
        Optional<LectureTime> expected = Optional.of(LectureTime.builder()
            .id(1)
            .start(LocalTime.of(8, 0, 0))
            .end(LocalTime.of(9, 30, 0))
            .build());
        Optional<LectureTime> actual = lectureTimeRepository.findByStartAndEnd(expected.get().getStart(), expected.get().getEnd());

        assertEquals(expected, actual);
    }
}
