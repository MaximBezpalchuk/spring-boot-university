package com.foxminded.university.dao.hibernate;

import com.foxminded.university.config.TestConfig;
import com.foxminded.university.dao.GroupDao;
import com.foxminded.university.model.Cathedra;
import com.foxminded.university.model.Group;
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
public class HibernateGroupDaoTest {

    @Autowired
    private SessionFactory sessionFactory;
    @Autowired
    private GroupDao groupDao;

    @Test
    void whenFindAll_thenAllExistingGroupsFound() {
        int expected = (int) (long) sessionFactory.getCurrentSession().createQuery("SELECT COUNT(g) FROM Group g").getSingleResult();
        List<Group> actual = groupDao.findAll();

        assertEquals(actual.size(), expected);
    }

    @Test
    void givenExistingGroup_whenFindById_thenGroupFound() {
        Optional<Group> expected = Optional.of(Group.builder()
                .id(1)
                .name("Killers")
                .cathedra(sessionFactory.getCurrentSession().get(Cathedra.class, 1))
                .build());
        Optional<Group> actual = groupDao.findById(1);

        assertEquals(expected, actual);

    }

    @Test
    void givenNotExistingGroup_whenFindById_thenReturnEmptyOptional() {
        assertEquals(groupDao.findById(100), Optional.empty());
    }

    @Test
    void givenNewGroup_whenSaveGroup_thenAllExistingGroupsFound() {
        Group expected = Group.builder()
                .name("Test Name")
                .cathedra(sessionFactory.getCurrentSession().get(Cathedra.class, 1))
                .build();
        groupDao.save(expected);
        Group actual = sessionFactory.getCurrentSession().get(Group.class, 3);

        assertEquals(expected, actual);
    }

    @Test
    void givenExistingGroup_whenSaveWithChanges_thenChangesApplied() {
        Group expected = sessionFactory.getCurrentSession().get(Group.class, 1);
        expected.setName("Test Name");
        groupDao.save(expected);
        Group actual = sessionFactory.getCurrentSession().get(Group.class, 1);

        assertEquals(expected, actual);
    }

    @Test
    void whenDeleteExistingGroup_thenGroupDeleted() {
        groupDao.delete(Group.builder().id(2).build());
        Group actual = sessionFactory.getCurrentSession().get(Group.class, 2);

        assertNull(actual);
    }

    @Test
    void givenGroupName_whenFindByName_thenGroupFound() {
        Optional<Group> expected = Optional.of(Group.builder()
                .id(1)
                .name("Killers")
                .cathedra(sessionFactory.getCurrentSession().get(Cathedra.class, 1))
                .build());
        Optional<Group> actual = groupDao.findByName("Killers");

        assertEquals(expected, actual);
    }
}