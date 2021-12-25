package com.foxminded.university.dao;

import com.foxminded.university.model.Audience;
import com.foxminded.university.model.Cathedra;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@Transactional
public class AudienceRepositoryTest {

    @Autowired
    private EntityManager entityManager;
    @Autowired
    private AudienceRepository audienceRepository;

    @Test
    void whenFindAll_thenAllExistingAudiencesFound() {
        int expected = (int) (long) entityManager.createQuery("SELECT COUNT(a) FROM Audience a").getSingleResult();
        List<Audience> actual = audienceRepository.findAll();

        assertEquals(actual.size(), expected);
    }

    @Test
    void whenFindById_thenAllExistingAudiencesFound() {
        Optional<Audience> expected = Optional.of(Audience.builder()
            .id(1)
            .room(1)
            .capacity(10)
            .cathedra(entityManager.find(Cathedra.class, 1))
            .build());
        Optional<Audience> actual = audienceRepository.findById(1);

        assertEquals(expected, actual);

    }

    @Test
    void givenNotExistingAudience_whenFindById_thenReturnEmptyOptional() {
        assertEquals(audienceRepository.findById(100), Optional.empty());
    }

    @Test
    void givenNewAudience_whenSaveAudience_thenAllExistingAudiencesFound() {
        Audience expected = Audience.builder()
            .room(100)
            .capacity(100)
            .cathedra(entityManager.find(Cathedra.class, 1))
            .build();
        audienceRepository.save(expected);
        Audience actual = entityManager.find(Audience.class, 4);

        assertEquals(expected, actual);
    }

    @Test
    void givenExistingAudience_whenSaveWithChanges_thenChangesApplied() {
        Audience expected = entityManager.find(Audience.class, 1);
        expected.setRoom(12345);
        audienceRepository.save(expected);
        Audience actual = entityManager.find(Audience.class, 1);

        assertEquals(expected, actual);
    }

    @Test
    void whenDeleteExistingAudience_thenAudienceDeleted() {
        audienceRepository.delete(Audience.builder().id(2).build());
        Audience actual = entityManager.find(Audience.class, 2);

        assertNull(actual);
    }

    @Test
    void givenRoom_whenFindByRoom_thenAudienceFound() {
        Optional<Audience> expected = Optional.of(Audience.builder()
            .id(1)
            .room(1)
            .capacity(10)
            .cathedra(entityManager.find(Cathedra.class, 1))
            .build());
        Optional<Audience> actual = audienceRepository.findByRoom(1);

        assertEquals(expected, actual);
    }
}
