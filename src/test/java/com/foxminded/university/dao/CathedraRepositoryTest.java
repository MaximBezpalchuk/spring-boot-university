package com.foxminded.university.dao;

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
public class CathedraRepositoryTest {

    @Autowired
    private EntityManager entityManager;
    @Autowired
    private CathedraRepository cathedraRepository;

    @Test
    void whenFindAll_thenAllExistingCathedrasFound() {
        int expected = (int) (long) entityManager.createQuery("SELECT COUNT(c) FROM Cathedra c").getSingleResult();
        List<Cathedra> actual = cathedraRepository.findAll();

        assertEquals(actual.size(), expected);
    }

    @Test
    void givenExistingCathedra_whenFindById_thenCathedraFound() {
        Optional<Cathedra> expected = Optional.of(Cathedra.builder()
            .id(1)
            .name("Fantastic Cathedra")
            .build());
        Optional<Cathedra> actual = cathedraRepository.findById(1);

        assertEquals(expected, actual);
    }

    @Test
    void givenNotExistingCathedra_whenFindById_thenReturnEmptyOptional() {
        assertEquals(cathedraRepository.findById(100), Optional.empty());
    }

    @Test
    void givenNewCathedra_whenSaveCathedra_thenAllExistingCathedrasFound() {
        Cathedra expected = Cathedra.builder().name("Fantastic Cathedra 2").build();
        cathedraRepository.save(expected);
        Cathedra actual = entityManager.find(Cathedra.class, 2);

        assertEquals(expected, actual);
    }

    @Test
    void givenExistingCathedra_whenSaveWithChanges_thenChangesApplied() {
        Cathedra expected = entityManager.find(Cathedra.class, 1);
        expected.setName("Test name");
        cathedraRepository.save(expected);
        Cathedra actual = entityManager.find(Cathedra.class, 1);

        assertEquals(expected, actual);
    }

    @Test
    void whenDeleteExistingCathedra_thenCathedraDeleted() {
        cathedraRepository.delete(Cathedra.builder().id(1).build());
        Cathedra actual = entityManager.find(Cathedra.class, 1);

        assertNull(actual);
    }

    @Test
    void givenCathedraName_whenFindByName_thenCathedraFound() {
        Optional<Cathedra> expected = Optional.of(Cathedra.builder()
            .id(1)
            .name("Fantastic Cathedra")
            .build());
        Optional<Cathedra> actual = cathedraRepository.findByName("Fantastic Cathedra");

        assertEquals(expected, actual);
    }
}
