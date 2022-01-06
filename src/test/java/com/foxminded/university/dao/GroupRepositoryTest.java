package com.foxminded.university.dao;

import com.foxminded.university.model.Cathedra;
import com.foxminded.university.model.Group;
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
public class GroupRepositoryTest {

    @Autowired
    private EntityManager entityManager;
    @Autowired
    private GroupRepository groupRepository;

    @Test
    void whenFindAll_thenAllExistingGroupsFound() {
        int expected = (int) (long) entityManager.createQuery("SELECT COUNT(g) FROM Group g").getSingleResult();
        List<Group> actual = groupRepository.findAll();

        assertEquals(actual.size(), expected);
    }

    @Test
    void givenExistingGroup_whenFindById_thenGroupFound() {
        Optional<Group> expected = Optional.of(Group.builder()
            .id(1)
            .name("Killers")
            .cathedra(entityManager.find(Cathedra.class, 1))
            .build());
        Optional<Group> actual = groupRepository.findById(1);

        assertEquals(expected, actual);

    }

    @Test
    void givenNotExistingGroup_whenFindById_thenReturnEmptyOptional() {
        assertEquals(groupRepository.findById(100), Optional.empty());
    }

    @Test
    void givenNewGroup_whenSaveGroup_thenAllExistingGroupsFound() {
        Group expected = Group.builder()
            .name("Test Name")
            .cathedra(entityManager.find(Cathedra.class, 1))
            .build();
        groupRepository.save(expected);
        Group actual = entityManager.find(Group.class, 3);

        assertEquals(expected, actual);
    }

    @Test
    void givenExistingGroup_whenSaveWithChanges_thenChangesApplied() {
        Group expected = entityManager.find(Group.class, 1);
        expected.setName("Test Name");
        groupRepository.save(expected);
        Group actual = entityManager.find(Group.class, 1);

        assertEquals(expected, actual);
    }

    @Test
    void whenDeleteExistingGroup_thenGroupDeleted() {
        groupRepository.delete(Group.builder().id(2).build());
        Group actual = entityManager.find(Group.class, 2);

        assertNull(actual);
    }

    @Test
    void givenGroupName_whenFindByName_thenGroupFound() {
        Optional<Group> expected = Optional.of(Group.builder()
            .id(1)
            .name("Killers")
            .cathedra(entityManager.find(Cathedra.class, 1))
            .build());
        Optional<Group> actual = groupRepository.findByName("Killers");

        assertEquals(expected, actual);
    }
}