package com.foxminded.university.dao.hibernate;

import com.foxminded.university.config.TestConfig;
import com.foxminded.university.dao.CathedraDao;
import com.foxminded.university.model.Cathedra;
import com.foxminded.university.model.Holiday;
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
public class HibernateCathedraDaoTest {

    @Autowired
    private SessionFactory sessionFactory;
    @Autowired
    private CathedraDao cathedraDao;

    @Test
    void whenFindAll_thenAllExistingCathedrasFound() {
        int expected = (int) (long) sessionFactory.getCurrentSession().createQuery("SELECT COUNT(c) FROM Cathedra c").getSingleResult();
        List<Cathedra> actual = cathedraDao.findAll();

        assertEquals(actual.size(), expected);
    }

    @Test
    void givenExistingCathedra_whenFindById_thenCathedraFound() {
        Optional<Cathedra> expected = Optional.of(Cathedra.builder()
                .id(1)
                .name("Fantastic Cathedra")
                .build());
        Optional<Cathedra> actual = cathedraDao.findById(1);

        assertEquals(expected, actual);
    }

    @Test
    void givenNotExistingCathedra_whenFindById_thenReturnEmptyOptional() {
        assertEquals(cathedraDao.findById(100), Optional.empty());
    }

    @Test
    void givenNewCathedra_whenSaveCathedra_thenAllExistingCathedrasFound() {
        Cathedra expected = Cathedra.builder().name("Fantastic Cathedra 2").build();
        cathedraDao.save(expected);
        Cathedra actual = sessionFactory.getCurrentSession().get(Cathedra.class, 2);

        assertEquals(expected, actual);
    }

    @Test
    void givenExistingCathedra_whenSaveWithChanges_thenChangesApplied() {
        Cathedra expected = sessionFactory.getCurrentSession().get(Cathedra.class, 1);
        expected.setName("Test name");
        cathedraDao.save(expected);
        Cathedra actual = sessionFactory.getCurrentSession().get(Cathedra.class, 1);

        assertEquals(expected, actual);
    }

    @Test
    void whenDeleteExistingCathedra_thenCathedraDeleted() {
        cathedraDao.deleteById(1);
        Cathedra actual = sessionFactory.getCurrentSession().get(Cathedra.class, 1);

        assertNull(actual);
    }

    @Test
    void givenCathedraName_whenFindByName_thenCathedraFound() {
        Optional<Cathedra> expected = Optional.of(Cathedra.builder()
                .id(1)
                .name("Fantastic Cathedra")
                .build());
        Optional<Cathedra> actual = cathedraDao.findByName("Fantastic Cathedra");

        assertEquals(expected, actual);
    }
}
