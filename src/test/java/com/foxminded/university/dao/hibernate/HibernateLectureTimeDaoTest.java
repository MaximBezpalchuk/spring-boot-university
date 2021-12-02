package com.foxminded.university.dao.hibernate;

import com.foxminded.university.config.TestConfig;
import com.foxminded.university.dao.LectureTimeDao;
import com.foxminded.university.model.LectureTime;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {TestConfig.class})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@Transactional
public class HibernateLectureTimeDaoTest {

    @Autowired
    private SessionFactory sessionFactory;
    @Autowired
    private LectureTimeDao lectureTimeDao;

    @Test
    void whenFindAll_thenAllExistingLectureTimesFound() {
        int expected = (int) (long) sessionFactory.getCurrentSession().createQuery("SELECT COUNT(lt) FROM LectureTime lt").getSingleResult();
        List<LectureTime> actual = lectureTimeDao.findAll();

        assertEquals(actual.size(), expected);
    }

    @Test
    void givenExistingLectureTime_whenFindById_thenLectureTimeFound() {
        Optional<LectureTime> expected = Optional.of(LectureTime.builder()
                .id(1).start(LocalTime.of(8, 0, 0))
                .end(LocalTime.of(9, 30, 0))
                .build());
        Optional<LectureTime> actual = lectureTimeDao.findById(1);

        assertEquals(expected, actual);
    }

    @Test
    void givenNotExistingLectureTime_whenFindById_thenReturnEmptyOptional() {
        assertEquals(lectureTimeDao.findById(100), Optional.empty());
    }

    @Test
    void givenNewLectureTime_whenSaveLectureTime_thenAllExistingLectureTimesFound() {
        LectureTime expected = LectureTime.builder()
                .start(LocalTime.of(21, 0, 0))
                .end(LocalTime.of(22, 30, 0))
                .build();
        lectureTimeDao.save(expected);
        LectureTime actual = sessionFactory.getCurrentSession().get(LectureTime.class, 9);

        assertEquals(expected, actual);
    }

    @Test
    void givenExistingLectureTime_whenSaveWithChanges_thenChangesApplied() {
        LectureTime expected = sessionFactory.getCurrentSession().get(LectureTime.class, 1);
        expected.setStart(LocalTime.of(8, 23));
        lectureTimeDao.save(expected);
        LectureTime actual = sessionFactory.getCurrentSession().get(LectureTime.class, 1);

        assertEquals(expected, actual);
    }

    @Test
    void whenDeleteExistingLectureTime_thenLectureTimeDeleted() {
        lectureTimeDao.deleteById(2);
        LectureTime actual = sessionFactory.getCurrentSession().get(LectureTime.class, 2);

        assertNull(actual);
    }

    @Test
    void givenPeriod_whenFindByPeriod_thenLectureTimeFound() {
        Optional<LectureTime> expected = Optional.of(LectureTime.builder()
                .id(1)
                .start(LocalTime.of(8, 0, 0))
                .end(LocalTime.of(9, 30, 0))
                .build());
        Optional<LectureTime> actual = lectureTimeDao.findByPeriod(expected.get().getStart(), expected.get().getEnd());

        assertEquals(expected, actual);
    }
}
