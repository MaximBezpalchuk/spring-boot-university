package com.foxminded.university.dao.hibernate;

import com.foxminded.university.config.TestConfig;
import com.foxminded.university.dao.AudienceDao;
import com.foxminded.university.model.Audience;
import com.foxminded.university.model.Cathedra;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {TestConfig.class})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@Transactional
public class HibernateAudienceDaoTest {

    @Autowired
    private SessionFactory sessionFactory;
    @Autowired
    private AudienceDao audienceDao;

    @Test
    void whenFindAll_thenAllExistingAudiencesFound() {
        int expected = (int) (long) sessionFactory.getCurrentSession().createQuery("SELECT COUNT(a) FROM Audience a").getSingleResult();
        List<Audience> actual = audienceDao.findAll();

        assertEquals(actual.size(), expected);
    }

    @Test
    void whenFindById_thenAllExistingAudiencesFound() {
        Optional<Audience> expected = Optional.of(Audience.builder()
                .id(1)
                .room(1)
                .capacity(10)
                .cathedra(sessionFactory.getCurrentSession().get(Cathedra.class, 1))
                .build());
        Optional<Audience> actual = audienceDao.findById(1);

        assertEquals(expected, actual);

    }

    @Test
    void givenNotExistingAudience_whenFindById_thenReturnEmptyOptional() {
        assertEquals(audienceDao.findById(100), Optional.empty());
    }

    @Test
    void givenNewAudience_whenSaveAudience_thenAllExistingAudiencesFound() {
        Audience expected = Audience.builder()
                .room(100)
                .capacity(100)
                .cathedra(sessionFactory.getCurrentSession().get(Cathedra.class, 1))
                .build();
        audienceDao.save(expected);
        Audience actual = sessionFactory.getCurrentSession().get(Audience.class, 4);

        assertEquals(expected, actual);
    }

    @Test
    void givenExistingAudience_whenSaveWithChanges_thenChangesApplied() {
        Audience expected = sessionFactory.getCurrentSession().get(Audience.class, 1);
        expected.setRoom(12345);
        audienceDao.save(expected);
        Audience actual = sessionFactory.getCurrentSession().get(Audience.class, 1);

        assertEquals(expected, actual);
    }

    @Test
    void whenDeleteExistingAudience_thenAudienceDeleted() {
        audienceDao.delete(Audience.builder().id(2).build());
        Audience actual = sessionFactory.getCurrentSession().get(Audience.class, 2);

        assertNull(actual);
    }

    @Test
    void givenRoom_whenFindByRoom_thenAudienceFound() {
        Optional<Audience> expected = Optional.of(Audience.builder()
                .id(1)
                .room(1)
                .capacity(10)
                .cathedra(sessionFactory.getCurrentSession().get(Cathedra.class, 1))
                .build());
        Optional<Audience> actual = audienceDao.findByRoom(1);

        assertEquals(expected, actual);

    }
}
